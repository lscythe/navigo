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
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders

class NetworkBindingsTest :
    FunSpec({
        test("public requests use the supported language header") {
            val engine = MockEngine { request ->
                request.headers[HttpHeaders.AcceptLanguage] shouldBe "id"
                respondOk()
            }
            val client =
                NetworkBindings.createPublicHttpClient(
                    engine = engine,
                    baseUrl = "https://api.navigo.app",
                    languageProvider = LanguageProvider { SupportedLanguage.Indonesian },
                    networkLogger = NetworkLogger {},
                )

            client.get("v1/routes")
        }

        test("public requests omit language when provider uses system default") {
            val engine = MockEngine { request ->
                request.headers.contains(HttpHeaders.AcceptLanguage) shouldBe false
                respondOk()
            }
            val client =
                NetworkBindings.createPublicHttpClient(
                    engine = engine,
                    baseUrl = "https://api.navigo.app",
                    languageProvider = LanguageProvider { null },
                    networkLogger = NetworkLogger {},
                )

            client.get("v1/routes")
        }
    })
