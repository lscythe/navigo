/*
 * Copyright 2026 Lscythe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.lscythe.app.navigo.api.transit.realtime

import dev.lscythe.app.navigo.api.transit.constant.TransitEndpoint
import dev.lscythe.app.navigo.core.network.AuthenticatedClient
import dev.lscythe.app.navigo.core.network.BaseUrl
import dev.lscythe.app.navigo.core.network.ProblemDetail
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readRawBytes
import io.ktor.client.utils.HttpResponseReceived
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import io.ktor.util.AttributeKey
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class KtorTransitRealtimeApi(
    private val sessionFactory: ViewportWebSocketSessionFactory,
    private val codec: ViewportStreamCodec,
) : TransitRealtimeApi {
    @Inject
    constructor(
        @AuthenticatedClient httpClient: HttpClient,
        @BaseUrl baseUrl: String,
    ) : this(
        sessionFactory = KtorViewportWebSocketSessionFactory(httpClient, baseUrl),
        codec = ViewportStreamCodec(PROTOCOL_JSON),
    )

    override fun stream(subscriptions: Flow<ViewportSubscription>): Flow<ViewportStreamEvent> =
        flow {
            val session = openSession(sessionFactory)
            try {
                requireNegotiatedProtocol(session.negotiatedProtocol)
                coroutineScope {
                    val sender = launch {
                        subscriptions.collect { subscription ->
                            session.sendText(codec.encode(subscription))
                        }
                    }
                    try {
                        while (true) {
                            when (val frame = session.receive()) {
                                is ViewportIncomingFrame.Text -> emit(codec.decode(frame.value))
                                is ViewportIncomingFrame.Binary ->
                                    throw ViewportStreamFailure.UnsupportedFrame(
                                        "Binary viewport stream frame"
                                    )
                                is ViewportIncomingFrame.Closed ->
                                    throw ViewportStreamFailure.RemoteClosed(
                                        frame.code,
                                        frame.reason,
                                    )
                            }
                        }
                    } finally {
                        sender.cancelAndJoin()
                    }
                }
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (failure: ViewportStreamFailure) {
                throw failure
            } catch (throwable: Throwable) {
                throw ViewportStreamFailure.Transport(throwable.message, throwable)
            } finally {
                withContext(NonCancellable) { session.close() }
            }
        }

    private suspend fun openSession(
        factory: ViewportWebSocketSessionFactory
    ): ViewportWebSocketSession =
        try {
            factory.open()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (failure: ViewportStreamFailure) {
            throw failure
        } catch (throwable: Throwable) {
            throw ViewportStreamFailure.Transport(throwable.message, throwable)
        }

    private fun requireNegotiatedProtocol(protocol: String?) {
        if (protocol != TransitEndpoint.VIEWPORT_STREAM_PROTOCOL) {
            throw ViewportStreamFailure.Protocol(
                "Expected viewport stream protocol ${TransitEndpoint.VIEWPORT_STREAM_PROTOCOL}"
            )
        }
    }

    private companion object {
        val PROTOCOL_JSON = Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }
}

internal interface ViewportWebSocketSessionFactory {
    suspend fun open(): ViewportWebSocketSession
}

internal interface ViewportWebSocketSession {
    val negotiatedProtocol: String?

    suspend fun sendText(text: String)

    suspend fun receive(): ViewportIncomingFrame

    suspend fun close()
}

internal sealed interface ViewportIncomingFrame {
    data class Text(val value: String) : ViewportIncomingFrame

    data class Binary(val value: ByteArray) : ViewportIncomingFrame {
        override fun equals(other: Any?): Boolean =
            this === other || other is Binary && value.contentEquals(other.value)

        override fun hashCode(): Int = value.contentHashCode()
    }

    data class Closed(val code: Short?, val reason: String?) : ViewportIncomingFrame
}

internal data class ViewportStreamRequest(
    val method: HttpMethod,
    val url: String,
    val requestedProtocol: String,
)

internal fun viewportStreamRequest(baseUrl: String): ViewportStreamRequest {
    val webSocketUrl =
        URLBuilder(baseUrl).apply {
            protocol =
                when (protocol) {
                    URLProtocol.HTTP -> URLProtocol.WS
                    URLProtocol.HTTPS -> URLProtocol.WSS
                    URLProtocol.WS,
                    URLProtocol.WSS -> protocol
                    else -> error("Unsupported viewport stream base URL protocol")
                }
            encodedPath = "/${TransitEndpoint.VIEWPORT_STREAM}"
        }
    return ViewportStreamRequest(
        method = HttpMethod.Get,
        url = webSocketUrl.buildString(),
        requestedProtocol = TransitEndpoint.VIEWPORT_STREAM_PROTOCOL,
    )
}

private class KtorViewportWebSocketSessionFactory(
    httpClient: HttpClient,
    private val baseUrl: String,
) : ViewportWebSocketSessionFactory {
    private val responseBodies = UpgradeResponseBodies()
    private val upgradeClient =
        httpClient.config {
            followRedirects = false
            install(ResponseObserver) {
                filter { call -> call.request.attributes.contains(UPGRADE_RESPONSE_MARKER) }
                onResponse { response ->
                    val marker = response.call.request.attributes[UPGRADE_RESPONSE_MARKER]
                    responseBodies.complete(marker, response.readRawBytes().decodeToString())
                }
            }
        }

    override suspend fun open(): ViewportWebSocketSession =
        openViewportWebSocketSession(upgradeClient, baseUrl)

    private suspend fun openViewportWebSocketSession(
        httpClient: HttpClient,
        baseUrl: String,
    ): ViewportWebSocketSession {
        val responseMarker = Any()
        val responseBody = responseBodies.register(responseMarker)
        var receivedResponse: HttpResponse? = null
        val responseListener: (HttpResponse) -> Unit = { response ->
            if (response.isMarkedWith(responseMarker)) receivedResponse = response
        }
        httpClient.monitor.subscribe(HttpResponseReceived, responseListener)
        try {
            val request = viewportStreamRequest(baseUrl)
            val session =
                httpClient.webSocketSession(request.url) {
                    method = request.method
                    attributes.put(UPGRADE_RESPONSE_MARKER, responseMarker)
                    headers.append(HttpHeaders.SecWebSocketProtocol, request.requestedProtocol)
                }
            return KtorViewportWebSocketSession(session)
        } catch (exception: ResponseException) {
            throw exception.response.toUpgradeFailure(responseBody.awaitOrNull(), exception)
        } catch (exception: WebSocketException) {
            val response = receivedResponse ?: throw exception
            throw response.toUpgradeFailure(responseBody.awaitOrNull(), exception)
        } finally {
            httpClient.monitor.unsubscribe(HttpResponseReceived, responseListener)
            responseBodies.remove(responseMarker)
        }
    }

    private fun HttpResponse.isMarkedWith(marker: Any): Boolean =
        call.request.attributes.contains(UPGRADE_RESPONSE_MARKER) &&
            call.request.attributes[UPGRADE_RESPONSE_MARKER] === marker
}

private class UpgradeResponseBodies {
    private val mutex = Mutex()
    private val bodies = mutableMapOf<Any, CompletableDeferred<String>>()

    suspend fun register(marker: Any): CompletableDeferred<String> =
        mutex.withLock { CompletableDeferred<String>().also { bodies[marker] = it } }

    suspend fun complete(marker: Any, body: String) {
        mutex.withLock { bodies[marker] }?.complete(body)
    }

    suspend fun remove(marker: Any) {
        mutex.withLock { bodies.remove(marker) }?.cancel()
    }
}

private class KtorViewportWebSocketSession(private val session: DefaultClientWebSocketSession) :
    ViewportWebSocketSession {
    override val negotiatedProtocol: String?
        get() = session.call.response.headers[HttpHeaders.SecWebSocketProtocol]

    override suspend fun sendText(text: String) {
        session.send(text)
    }

    override suspend fun receive(): ViewportIncomingFrame =
        try {
            when (val frame = session.incoming.receive()) {
                is Frame.Text -> ViewportIncomingFrame.Text(frame.readText())
                is Frame.Binary -> ViewportIncomingFrame.Binary(frame.data)
                else ->
                    throw ViewportStreamFailure.UnsupportedFrame(
                        "Unsupported viewport stream frame ${frame.frameType}"
                    )
            }
        } catch (_: ClosedReceiveChannelException) {
            val reason = session.closeReason.await()
            ViewportIncomingFrame.Closed(reason?.code, reason?.message)
        }

    override suspend fun close() {
        session.close()
        session.incoming.cancel()
    }
}


private val UPGRADE_RESPONSE_MARKER = AttributeKey<Any>("ViewportStreamUpgradeResponseMarker")
private const val RESPONSE_BODY_TIMEOUT_MILLIS = 1_000L

private suspend fun CompletableDeferred<String>.awaitOrNull(): String? =
    withTimeoutOrNull(RESPONSE_BODY_TIMEOUT_MILLIS) { await() }

private fun HttpResponse.toUpgradeFailure(
    responseBody: String?,
    cause: Throwable,
): ViewportStreamFailure {
    val problem =
        responseBody?.let { body ->
            runCatching { PROBLEM_JSON.decodeFromString<ProblemDetail>(body) }.getOrNull()
        }
    return if (status == HttpStatusCode.Unauthorized) {
        ViewportStreamFailure.Authentication(
            statusCode = status.value,
            contentLanguage = headers[HttpHeaders.ContentLanguage],
            problem = problem,
            cause = cause,
        )
    } else {
        ViewportStreamFailure.Upgrade(
            statusCode = status.value,
            contentLanguage = headers[HttpHeaders.ContentLanguage],
            problem = problem,
            cause = cause,
        )
    }
}

private val PROBLEM_JSON = Json { ignoreUnknownKeys = true }
