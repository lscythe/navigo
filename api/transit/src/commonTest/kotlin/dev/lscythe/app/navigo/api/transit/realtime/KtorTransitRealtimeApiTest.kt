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

import app.cash.turbine.test
import dev.lscythe.app.navigo.api.transit.constant.TransitEndpoint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.yield
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class KtorTransitRealtimeApiTest :
    FunSpec({
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
        val subscription =
            ViewportSubscription(
                requestId = "request-1",
                viewport = ViewportBounds(-6.3, 106.7, -6.1, 106.9),
                routeCodes = listOf("1"),
                includeSnapshot = true,
            )

        test("opens one socket sends the subscription and emits ordered events") {
            val session = FakeViewportWebSocketSession()
            session.frames.send(
                ViewportIncomingFrame.Text(
                    """{"type":"subscribed","requestId":"request-1","subscriptionId":"subscription-1"}"""
                )
            )
            session.frames.send(
                ViewportIncomingFrame.Text(
                    """{"type":"status","subscriptionId":"subscription-1","state":"ready","reason":"","topicCount":6}"""
                )
            )
            val factory = FakeViewportWebSocketSessionFactory(session)
            val api = KtorTransitRealtimeApi(factory, ViewportStreamCodec(json))

            api.stream(flowOf(subscription)).test {
                awaitItem() shouldBe ViewportStreamEvent.Subscribed("request-1", "subscription-1")
                awaitItem() shouldBe
                    ViewportStreamEvent.Status(
                        "subscription-1",
                        ViewportStreamState.Ready,
                        "",
                        6,
                    )
                cancelAndIgnoreRemainingEvents()
            }

            factory.openCount shouldBe 1
            json.parseToJsonElement(session.sent.single()) shouldBe
                json.parseToJsonElement(
                    """{"type":"subscribe","requestId":"request-1","viewport":{"south":-6.3,"west":106.7,"north":-6.1,"east":106.9},"routeCodes":["1"],"includeSnapshot":true}"""
                )
            session.closed shouldBe true
        }

        test("sends repeated subscriptions over one socket") {
            val subscriptions = MutableSharedFlow<ViewportSubscription>(extraBufferCapacity = 2)
            val session = FakeViewportWebSocketSession()
            val factory = FakeViewportWebSocketSessionFactory(session)
            val api = KtorTransitRealtimeApi(factory, ViewportStreamCodec(json))

            api.stream(subscriptions).test {
                subscriptions.emit(subscription)
                subscriptions.emit(subscription.copy(requestId = "request-2"))
                session.awaitSentCount(2)
                cancelAndIgnoreRemainingEvents()
            }

            factory.openCount shouldBe 1
            session.sent.size shouldBe 2
            json
                .parseToJsonElement(session.sent[0])
                .jsonObject["requestId"]
                ?.jsonPrimitive
                ?.content shouldBe "request-1"
            json
                .parseToJsonElement(session.sent[1])
                .jsonObject["requestId"]
                ?.jsonPrimitive
                ?.content shouldBe "request-2"
        }

        test("rejects unsupported binary application frames") {
            val session = FakeViewportWebSocketSession()
            session.frames.send(ViewportIncomingFrame.Binary(byteArrayOf(1)))
            val api =
                KtorTransitRealtimeApi(
                    FakeViewportWebSocketSessionFactory(session),
                    ViewportStreamCodec(json),
                )

            api.stream(flowOf(subscription)).test {
                awaitError()::class shouldBe ViewportStreamFailure.UnsupportedFrame::class
            }
            session.closed shouldBe true
        }

        test("rejects missing and incorrect negotiated protocols") {
            listOf(null, "future.protocol").forEach { protocol ->
                val session = FakeViewportWebSocketSession(negotiatedProtocol = protocol)
                val api =
                    KtorTransitRealtimeApi(
                        FakeViewportWebSocketSessionFactory(session),
                        ViewportStreamCodec(json),
                    )

                shouldThrow<ViewportStreamFailure.Protocol> {
                    api.stream(flowOf(subscription)).collect {}
                }
                session.closed shouldBe true
            }
        }

        test("preserves typed upgrade failures") {
            val expected = ViewportStreamFailure.Upgrade(statusCode = 503, contentLanguage = "id")
            val api =
                KtorTransitRealtimeApi(
                    FakeViewportWebSocketSessionFactory(failure = expected),
                    ViewportStreamCodec(json),
                )

            shouldThrow<ViewportStreamFailure.Upgrade> {
                api.stream(flowOf(subscription)).collect {}
            } shouldBe expected
        }

        test("maps malformed payload and remote close to typed failures") {
            listOf(
                    ViewportIncomingFrame.Text("not-json") to ViewportStreamFailure.Protocol::class,
                    ViewportIncomingFrame.Closed(1001, "away") to
                        ViewportStreamFailure.RemoteClosed::class,
                )
                .forEach { (frame, expectedType) ->
                    val session = FakeViewportWebSocketSession()
                    session.frames.send(frame)
                    val api =
                        KtorTransitRealtimeApi(
                            FakeViewportWebSocketSessionFactory(session),
                            ViewportStreamCodec(json),
                        )

                    api.stream(flowOf(subscription)).test {
                        awaitError()::class shouldBe expectedType
                    }
                    session.closed shouldBe true
                }
        }

        test("maps send failures to transport failures") {
            val session =
                FakeViewportWebSocketSession(sendFailure = IllegalStateException("offline"))
            val api =
                KtorTransitRealtimeApi(
                    FakeViewportWebSocketSessionFactory(session),
                    ViewportStreamCodec(json),
                )

            api.stream(flowOf(subscription)).test {
                awaitError()::class shouldBe ViewportStreamFailure.Transport::class
            }
            session.closed shouldBe true
        }

        test("collector cancellation closes the session without wrapping cancellation") {
            val session = FakeViewportWebSocketSession()
            val api =
                KtorTransitRealtimeApi(
                    FakeViewportWebSocketSessionFactory(session),
                    ViewportStreamCodec(json),
                )

            api.stream(flowOf(subscription)).test { cancel() }

            session.closed shouldBe true
            (session.receiveCancellation.await() is CancellationException) shouldBe true
        }
    })

private class FakeViewportWebSocketSessionFactory(
    private val session: ViewportWebSocketSession? = null,
    private val failure: Throwable? = null,
) : ViewportWebSocketSessionFactory {
    var openCount = 0

    override suspend fun open(): ViewportWebSocketSession {
        openCount += 1
        failure?.let { throw it }
        return requireNotNull(session)
    }
}

private class FakeViewportWebSocketSession(
    override val negotiatedProtocol: String? = TransitEndpoint.VIEWPORT_STREAM_PROTOCOL,
    private val sendFailure: Throwable? = null,
) : ViewportWebSocketSession {
    val frames = Channel<ViewportIncomingFrame>(Channel.UNLIMITED)
    val sent = mutableListOf<String>()
    val receiveCancellation = CompletableDeferred<Throwable>()
    var closed = false

    override suspend fun sendText(text: String) {
        sendFailure?.let { throw it }
        sent += text
    }

    override suspend fun receive(): ViewportIncomingFrame =
        try {
            frames.receive()
        } catch (throwable: Throwable) {
            receiveCancellation.complete(throwable)
            throw throwable
        }

    override suspend fun close() {
        closed = true
        frames.cancel()
    }

    suspend fun awaitSentCount(expected: Int) {
        while (sent.size < expected) yield()
    }
}
