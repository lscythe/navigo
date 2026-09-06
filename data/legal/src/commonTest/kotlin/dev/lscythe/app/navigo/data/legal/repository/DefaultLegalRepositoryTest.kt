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
package dev.lscythe.app.navigo.data.legal.repository

import dev.lscythe.app.navigo.api.legal.LegalApi
import dev.lscythe.app.navigo.api.legal.dto.LegalDocument
import dev.lscythe.app.navigo.api.legal.dto.LegalDocumentMetadata
import dev.lscythe.app.navigo.api.legal.dto.LegalDocumentResult
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.persistence.LegalDocumentCachePreference
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSource
import dev.lscythe.app.navigo.domain.legal.repository.LegalFailure
import dev.lscythe.app.navigo.domain.legal.repository.LegalResult
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class DefaultLegalRepositoryTest :
    FunSpec({
        test("modified responses replace cache and return network documents") {
            val api = FakeLegalApi(modified("terms", "id"), modified("privacy", "id"))
            val cache = FakeLegalCache()
            val result =
                DefaultLegalRepository(api, cache, FakeBundledSource())
                    .getDocuments(AppLanguage.Indonesian)
            val set = (result as LegalResult.Success).value
            set.terms.source shouldBe LegalDocumentSource.Network
            set.privacy.source shouldBe LegalDocumentSource.Network
            cache.values.size shouldBe 2
            api.termsCalls shouldBe 1
            api.privacyCalls shouldBe 1
        }

        test("not modified responses use matching cache") {
            val cache =
                FakeLegalCache().apply {
                    put(record("terms", "en"))
                    put(record("privacy", "en"))
                }
            val metadata = LegalDocumentMetadata("en", "etag", "max-age=60")
            val api =
                FakeLegalApi(
                    ApiResponse.Success(LegalDocumentResult.NotModified(metadata)),
                    ApiResponse.Success(LegalDocumentResult.NotModified(metadata)),
                )
            val set =
                (DefaultLegalRepository(api, cache, FakeBundledSource())
                        .getDocuments(AppLanguage.English) as LegalResult.Success)
                    .value
            set.terms.source shouldBe LegalDocumentSource.Cache
            set.privacy.source shouldBe LegalDocumentSource.Cache
        }

        test("network failure falls back to cache then bundled content") {
            val cache = FakeLegalCache().apply { put(record("terms", "en")) }
            val bundled = FakeBundledSource(mapOf(("privacy" to "en") to json("privacy", "en")))
            val failure = ApiResponse.Error.NetworkError("offline")
            val set =
                (DefaultLegalRepository(FakeLegalApi(failure, failure), cache, bundled)
                        .getDocuments(AppLanguage.English) as LegalResult.Success)
                    .value
            set.terms.source shouldBe LegalDocumentSource.Cache
            set.privacy.source shouldBe LegalDocumentSource.Bundled
        }

        test("missing one document returns failure and never partial success") {
            val failure = ApiResponse.Error.NetworkError("offline")
            DefaultLegalRepository(
                    FakeLegalApi(failure, failure),
                    FakeLegalCache(),
                    FakeBundledSource(),
                )
                .getDocuments(AppLanguage.English) shouldBe
                LegalResult.Failure(LegalFailure.Network("offline"))
        }
    })

private class FakeLegalApi(
    private val terms: ApiResponse<LegalDocumentResult>,
    private val privacy: ApiResponse<LegalDocumentResult>,
) : LegalApi {
    var termsCalls = 0
    var privacyCalls = 0

    override suspend fun getTerms(etag: String?): ApiResponse<LegalDocumentResult> {
        termsCalls++
        return terms
    }

    override suspend fun getPrivacyPolicy(etag: String?): ApiResponse<LegalDocumentResult> {
        privacyCalls++
        return privacy
    }
}

private class FakeLegalCache : LegalDocumentCache {
    val values = mutableMapOf<Pair<String, String>, LegalDocumentCachePreference>()

    override suspend fun get(type: String, language: String) = values[type to language]

    override suspend fun put(record: LegalDocumentCachePreference) {
        values[record.type to record.language] = record
    }
}

private class FakeBundledSource(
    private val values: Map<Pair<String, String>, String> = emptyMap()
) : BundledLegalDocumentSource {
    override suspend fun read(
        type: dev.lscythe.app.navigo.domain.legal.model.LegalDocumentType,
        language: AppLanguage,
    ) = values[type.wireValue to language.wireValue]
}

private fun modified(type: String, language: String) =
    ApiResponse.Success(
        LegalDocumentResult.Modified(
            LegalDocument(
                type,
                language,
                "published",
                "1",
                LocalDate(2026, 1, 1),
                "Title",
                1,
                "summary",
                "body",
                "https://example.com",
            ),
            LegalDocumentMetadata(language, "etag", "max-age=60"),
        )
    )

private fun record(type: String, language: String) =
    LegalDocumentCachePreference(
        type,
        language,
        "published",
        "1",
        "2026-01-01",
        "Title",
        1,
        "summary",
        "body",
        "https://example.com",
        "etag",
        language,
        "max-age=60",
    )

private fun json(type: String, language: String) =
    """{"slug":"$type","language":"$language","status":"draft","version":"1","title":"Title","readingTimeMinutes":1,"summaryHtml":"summary","bodyHtml":"body","canonicalUrl":"https://example.com"}"""
