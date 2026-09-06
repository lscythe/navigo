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
package dev.lscythe.app.navigo.api.legal.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class LegalDocument(
    val slug: String,
    val language: String,
    val status: String,
    val version: String,
    val effectiveAt: LocalDate? = null,
    val title: String,
    val readingTimeMinutes: Int,
    val summaryHtml: String,
    val bodyHtml: String,
    val canonicalUrl: String,
) {
    init {
        require(slug == "terms" || slug == "privacy") { "Invalid legal document slug" }
        require(language == "en" || language == "id") { "Invalid legal document language" }
        require(status == "draft" || status == "published") { "Invalid legal document status" }
        require(status != "published" || effectiveAt != null) {
            "Published legal documents require effectiveAt"
        }
        require(readingTimeMinutes >= 1) { "readingTimeMinutes must be positive" }
    }
}

data class LegalDocumentMetadata(
    val contentLanguage: String,
    val etag: String,
    val cacheControl: String,
)

sealed interface LegalDocumentResult {
    data class Modified(
        val document: LegalDocument,
        val metadata: LegalDocumentMetadata,
    ) : LegalDocumentResult

    data class NotModified(val metadata: LegalDocumentMetadata) : LegalDocumentResult
}
