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
package dev.lscythe.app.navigo.api.legal

import dev.lscythe.app.navigo.api.legal.dto.LegalDocument
import dev.lscythe.app.navigo.api.legal.dto.LegalDocumentMetadata
import dev.lscythe.app.navigo.api.legal.dto.LegalDocumentResult
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.testing.readResource
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.LocalDate

class LegalApiTest :
    FunSpec({
        test("gets localized terms with conditional request metadata") {
            val engine = MockEngine { request ->
                request.method shouldBe HttpMethod.Get
                request.url.encodedPath shouldBe "/v1/legal-documents/terms"
                request.headers.contains(HttpHeaders.AcceptLanguage) shouldBe false
                request.headers[HttpHeaders.IfNoneMatch] shouldBe "\"terms-id-v1\""
                respond(
                    content = readResource("legal/legal-document.json"),
                    status = HttpStatusCode.OK,
                    headers =
                        headersOf(
                            HttpHeaders.ContentType to listOf("application/json"),
                            HttpHeaders.ContentLanguage to listOf("id"),
                            HttpHeaders.ETag to listOf("\"terms-id-v2\""),
                            HttpHeaders.CacheControl to
                                listOf("public, max-age=300, stale-while-revalidate=86400"),
                        ),
                )
            }
            val api: LegalApi =
                LegalApiImpl(HttpClient(engine) { install(ContentNegotiation) { json() } })

            api.getTerms(etag = "\"terms-id-v1\"") shouldBe
                ApiResponse.Success(
                    LegalDocumentResult.Modified(
                        document =
                            LegalDocument(
                                slug = "terms",
                                language = "id",
                                status = "published",
                                version = "1.0",
                                effectiveAt = LocalDate(2026, 8, 30),
                                title = "Ketentuan Layanan",
                                readingTimeMinutes = 7,
                                summaryHtml = "<p>Ringkasan.</p>",
                                bodyHtml = "<section><h2>1. Ketentuan</h2><p>Isi.</p></section>",
                                canonicalUrl = "https://navigo.app/id/terms",
                            ),
                        metadata =
                            LegalDocumentMetadata(
                                contentLanguage = "id",
                                etag = "\"terms-id-v2\"",
                                cacheControl = "public, max-age=300, stale-while-revalidate=86400",
                            ),
                    )
                )
        }

        test("maps privacy 304 without decoding a body") {
            val engine = MockEngine { request ->
                request.url.encodedPath shouldBe "/v1/legal-documents/privacy"
                request.headers.contains(HttpHeaders.AcceptLanguage) shouldBe false
                request.headers[HttpHeaders.IfNoneMatch] shouldBe "\"privacy-en-v1\""
                respond(
                    content = "",
                    status = HttpStatusCode.NotModified,
                    headers =
                        headersOf(
                            HttpHeaders.ContentLanguage to listOf("en"),
                            HttpHeaders.ETag to listOf("\"privacy-en-v1\""),
                            HttpHeaders.CacheControl to
                                listOf("public, max-age=300, stale-while-revalidate=86400"),
                        ),
                )
            }

            LegalApiImpl(HttpClient(engine)).getPrivacyPolicy(etag = "\"privacy-en-v1\"") shouldBe
                ApiResponse.Success(
                    LegalDocumentResult.NotModified(
                        LegalDocumentMetadata(
                            contentLanguage = "en",
                            etag = "\"privacy-en-v1\"",
                            cacheControl = "public, max-age=300, stale-while-revalidate=86400",
                        )
                    )
                )
        }
    })
