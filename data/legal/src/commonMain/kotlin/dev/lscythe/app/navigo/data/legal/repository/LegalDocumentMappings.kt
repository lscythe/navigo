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

import dev.lscythe.app.navigo.api.legal.dto.LegalDocumentMetadata
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.persistence.LegalDocumentCachePreference
import dev.lscythe.app.navigo.domain.legal.model.LegalDocument
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSource
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentStatus
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentType
import dev.lscythe.app.navigo.domain.legal.repository.LegalFailure
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import kotlinx.datetime.LocalDate

internal val LegalDocumentType.wireValue
    get() =
        when (this) {
            LegalDocumentType.Terms -> "terms"
            LegalDocumentType.Privacy -> "privacy"
        }

internal val AppLanguage.wireValue
    get() =
        when (this) {
            AppLanguage.English -> "en"
            AppLanguage.Indonesian -> "id"
            AppLanguage.System -> "system"
        }

private fun String.toLanguage() =
    when (this) {
        "en" -> AppLanguage.English
        "id" -> AppLanguage.Indonesian
        else -> error("Invalid legal language")
    }

private fun String.toStatus() =
    when (this) {
        "draft" -> LegalDocumentStatus.Draft
        "published" -> LegalDocumentStatus.Published
        else -> error("Invalid legal status")
    }

internal fun dev.lscythe.app.navigo.api.legal.dto.LegalDocument.toDomain(
    expectedType: LegalDocumentType,
    expectedLanguage: AppLanguage,
    source: LegalDocumentSource,
): LegalDocument {
    require(slug == expectedType.wireValue && language.toLanguage() == expectedLanguage)
    return LegalDocument(
        expectedType,
        expectedLanguage,
        status.toStatus(),
        version,
        effectiveAt,
        title,
        readingTimeMinutes,
        summaryHtml,
        bodyHtml,
        canonicalUrl,
        source,
    )
}

internal fun dev.lscythe.app.navigo.api.legal.dto.LegalDocument.toCache(
    metadata: LegalDocumentMetadata
) =
    LegalDocumentCachePreference(
        slug,
        language,
        status,
        version,
        effectiveAt?.toString(),
        title,
        readingTimeMinutes,
        summaryHtml,
        bodyHtml,
        canonicalUrl,
        metadata.etag,
        metadata.contentLanguage,
        metadata.cacheControl,
    )

internal fun LegalDocumentCachePreference.toDomain(
    expectedType: LegalDocumentType,
    expectedLanguage: AppLanguage,
    source: LegalDocumentSource,
): LegalDocument? = runCatching {
    require(
        type == expectedType.wireValue &&
            language.toLanguage() == expectedLanguage &&
            contentLanguage.toLanguage() == expectedLanguage
    )
    LegalDocument(
        expectedType,
        expectedLanguage,
        status.toStatus(),
        version,
        effectiveAt?.let(LocalDate::parse),
        title,
        readingTimeMinutes,
        summaryHtml,
        bodyHtml,
        canonicalUrl,
        source,
    )
}
    .getOrNull()

internal fun ApiResponse<*>.toFailure(): LegalFailure =
    when (this) {
        is ApiResponse.Error.NetworkError -> LegalFailure.Network(message)
        is ApiResponse.Error.ServerError -> LegalFailure.Server(message)
        is ApiResponse.Error.SerializationError -> LegalFailure.InvalidContent(message)
        is ApiResponse.Error ->
            LegalFailure.Unknown(
                when (this) {
                    is ApiResponse.Error.ClientError -> message
                    is ApiResponse.Error.GraphQLError -> message
                    is ApiResponse.Error.UnknownError -> message
                }
            )
        is ApiResponse.Success ->
            LegalFailure.CacheInconsistency("Response could not produce valid content")
    }
