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

import dev.lscythe.app.navigo.api.legal.constant.LegalEndpoint
import dev.lscythe.app.navigo.api.legal.dto.LegalDocument
import dev.lscythe.app.navigo.api.legal.dto.LegalDocumentMetadata
import dev.lscythe.app.navigo.api.legal.dto.LegalDocumentResult
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.network.PublicClient
import dev.lscythe.app.navigo.core.network.safeResponseRequest
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class LegalApiImpl(@PublicClient private val httpClient: HttpClient) : LegalApi {
    override suspend fun getTerms(etag: String?): ApiResponse<LegalDocumentResult> =
        getLegalDocument(endpoint = LegalEndpoint.TERMS, etag = etag)

    override suspend fun getPrivacyPolicy(etag: String?): ApiResponse<LegalDocumentResult> =
        getLegalDocument(endpoint = LegalEndpoint.PRIVACY, etag = etag)

    private suspend fun getLegalDocument(
        endpoint: String,
        etag: String?,
    ): ApiResponse<LegalDocumentResult> =
        safeResponseRequest(
            block = {
                httpClient.get(endpoint) {
                    etag?.let { header(HttpHeaders.IfNoneMatch, it) }
                }
            },
            transform = { it.toLegalDocumentResult() },
        )
}

private suspend fun HttpResponse.toLegalDocumentResult(): LegalDocumentResult {
    val metadata =
        LegalDocumentMetadata(
            contentLanguage =
                requireNotNull(headers[HttpHeaders.ContentLanguage]) {
                    "Missing Content-Language header"
                },
            etag = requireNotNull(headers[HttpHeaders.ETag]) { "Missing ETag header" },
            cacheControl =
                requireNotNull(headers[HttpHeaders.CacheControl]) {
                    "Missing Cache-Control header"
                },
        )
    return if (status == HttpStatusCode.NotModified) {
        LegalDocumentResult.NotModified(metadata)
    } else {
        LegalDocumentResult.Modified(body<LegalDocument>(), metadata)
    }
}
