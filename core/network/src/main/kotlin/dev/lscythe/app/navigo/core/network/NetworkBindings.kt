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

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
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
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@ContributesTo(AppScope::class)
@BindingContainer
object NetworkBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(
        @BaseUrl baseUrl: String,
        tokenProvider: TokenProvider,
        ktorLogger: KtorLogger,
    ): HttpClient =
        HttpClient(OkHttp) {
            expectSuccess = true

            defaultRequest { url(baseUrl) }

            install(HttpTimeout) {
                requestTimeoutMillis = NetworkConstant.REQUEST_TIMEOUT_MILLIS
                connectTimeoutMillis = NetworkConstant.CONNECT_TIMEOUT_MILLIS
                socketTimeoutMillis = NetworkConstant.SOCKET_TIMEOUT_MILLIS
            }

            install(ContentNegotiation) {
                val json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
                json(json)
                // RFC 9457 Problem Details responses use this content type, not plain
                // application/json.
                json(json, contentType = ContentType("application", "problem+json"))
            }

            install(Logging) {
                logger = ktorLogger
                level = LogLevel.INFO
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        // ponytail: no refresh-token flow wired yet, refresh once the backend
                        // supports it via refreshTokens { }
                        tokenProvider.getToken()?.let { BearerTokens(it, "") }
                    }
                }
            }
        }
}
