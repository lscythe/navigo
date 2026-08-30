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
package dev.lscythe.app.navigo.core.network

import dev.lscythe.app.navigo.core.common.locale.SupportedLanguage
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger as KtorLogger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@ContributesTo(AppScope::class)
@BindingContainer
object NetworkBindings {
    private fun HttpClientConfig<*>.configurePublicClient(
        baseUrl: String,
        languageProvider: LanguageProvider,
        networkLogger: NetworkLogger,
    ) {
        expectSuccess = true
        defaultRequest {
            url(baseUrl)
            contentType(ContentType.Application.Json)
            languageProvider.currentLanguage()?.languageTag?.let {
                header(HttpHeaders.AcceptLanguage, it)
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = NetworkConstant.REQUEST_TIMEOUT_MILLIS
            connectTimeoutMillis = NetworkConstant.CONNECT_TIMEOUT_MILLIS
            socketTimeoutMillis = NetworkConstant.SOCKET_TIMEOUT_MILLIS
        }
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        install(ContentNegotiation) {
            json(json)
            json(json, contentType = ContentType("application", "problem+json"))
        }
        install(WebSockets) { contentConverter = KotlinxWebsocketSerializationConverter(json) }
        install(Logging) {
            logger =
                object : KtorLogger {
                    override fun log(message: String) = networkLogger.log(message)
                }
            level = LogLevel.INFO
        }
    }

    internal fun createPublicHttpClient(
        engine: HttpClientEngine,
        baseUrl: String,
        languageProvider: LanguageProvider,
        networkLogger: NetworkLogger,
    ): HttpClient =
        HttpClient(engine) {
            configurePublicClient(baseUrl, languageProvider, networkLogger)
        }

    @Provides
    @SingleIn(AppScope::class)
    fun provideLanguageProvider(): LanguageProvider = LanguageProvider { SupportedLanguage.English }

    @Provides
    @SingleIn(AppScope::class)
    @PublicClient
    fun providePublicHttpClient(
        @BaseUrl baseUrl: String,
        languageProvider: LanguageProvider,
        networkLogger: NetworkLogger,
    ): HttpClient =
        HttpClient(OkHttp) {
            configurePublicClient(baseUrl, languageProvider, networkLogger)
        }

    @Provides
    @AuthenticatedClient
    @SingleIn(AppScope::class)
    fun provideAuthenticatedHttpClient(
        @PublicClient publicClient: HttpClient,
        sessionManager: SessionManager,
    ): HttpClient = publicClient.config {
        install(Auth) {
            bearer {
                loadTokens {
                    sessionManager.loadTokens()?.let {
                        BearerTokens(it.accessToken, it.refreshToken)
                    }
                }
                refreshTokens {
                    sessionManager.refreshTokens()?.let {
                        BearerTokens(it.accessToken, it.refreshToken)
                    }
                }
            }
        }
    }
}
