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
package dev.lscythe.app.navigo.domain.legal.model

import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import kotlinx.datetime.LocalDate

enum class LegalDocumentType {
    Terms,
    Privacy,
}

enum class LegalDocumentStatus {
    Draft,
    Published,
}

enum class LegalDocumentSource {
    Network,
    Cache,
    Bundled,
}

data class LegalDocument(
    val type: LegalDocumentType,
    val language: AppLanguage,
    val status: LegalDocumentStatus,
    val version: String,
    val effectiveAt: LocalDate?,
    val title: String,
    val readingTimeMinutes: Int,
    val summaryHtml: String,
    val bodyHtml: String,
    val canonicalUrl: String,
    val source: LegalDocumentSource,
) {
    init {
        require(language != AppLanguage.System) { "Legal documents require an explicit language" }
        require(status != LegalDocumentStatus.Published || effectiveAt != null) {
            "Published legal documents require effectiveAt"
        }
        require(readingTimeMinutes >= 1) { "readingTimeMinutes must be positive" }
    }
}

data class LegalDocumentSet(val terms: LegalDocument, val privacy: LegalDocument) {
    init {
        require(terms.type == LegalDocumentType.Terms) { "terms must contain Terms" }
        require(privacy.type == LegalDocumentType.Privacy) { "privacy must contain Privacy" }
    }
}

data class AcceptedLegalDocument(val version: String, val language: AppLanguage)

data class LegalAcceptance(val terms: AcceptedLegalDocument, val privacy: AcceptedLegalDocument)
