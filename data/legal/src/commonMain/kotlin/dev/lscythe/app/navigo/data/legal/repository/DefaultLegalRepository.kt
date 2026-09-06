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
import dev.lscythe.app.navigo.api.legal.dto.LegalDocumentMetadata
import dev.lscythe.app.navigo.api.legal.dto.LegalDocumentResult
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.persistence.LegalDocumentCachePreference
import dev.lscythe.app.navigo.core.persistence.datasource.LegalDocumentCacheDataSource
import dev.lscythe.app.navigo.domain.legal.model.LegalDocument
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSet
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSource
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentStatus
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentType
import dev.lscythe.app.navigo.domain.legal.repository.LegalFailure
import dev.lscythe.app.navigo.domain.legal.repository.LegalRepository
import dev.lscythe.app.navigo.domain.legal.repository.LegalResult
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

interface LegalDocumentCache {
    suspend fun get(type: String, language: String): LegalDocumentCachePreference?

    suspend fun put(record: LegalDocumentCachePreference)
}

@Inject
@ContributesBinding(AppScope::class)
class PersistentLegalDocumentCache(private val source: LegalDocumentCacheDataSource) :
    LegalDocumentCache {
    override suspend fun get(type: String, language: String) = source.get(type, language)

    override suspend fun put(record: LegalDocumentCachePreference) = source.put(record)
}

interface BundledLegalDocumentSource {
    suspend fun read(type: LegalDocumentType, language: AppLanguage): String?
}

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DefaultLegalRepository(
    private val api: LegalApi,
    private val cache: LegalDocumentCache,
    private val bundled: BundledLegalDocumentSource,
) : LegalRepository {
    override suspend fun getDocuments(language: AppLanguage): LegalResult<LegalDocumentSet> {
        if (language == AppLanguage.System)
            return LegalResult.Failure(LegalFailure.InvalidContent("Explicit language required"))
        return try {
            val terms = load(LegalDocumentType.Terms, language)
            val privacy = load(LegalDocumentType.Privacy, language)
            if (terms is Loaded.Success && privacy is Loaded.Success) {
                LegalResult.Success(LegalDocumentSet(terms.document, privacy.document))
            } else {
                LegalResult.Failure(
                    (terms as? Loaded.Failure)?.failure ?: (privacy as Loaded.Failure).failure
                )
            }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            LegalResult.Failure(LegalFailure.Unknown(error.message))
        }
    }

    private suspend fun load(type: LegalDocumentType, language: AppLanguage): Loaded {
        val cached = cache.get(type.wireValue, language.wireValue)
        val response =
            when (type) {
                LegalDocumentType.Terms -> api.getTerms(cached?.etag)
                LegalDocumentType.Privacy -> api.getPrivacyPolicy(cached?.etag)
            }
        when (response) {
            is ApiResponse.Success ->
                when (val result = response.data) {
                    is LegalDocumentResult.Modified -> runCatching {
                            result.document.toDomain(
                                type,
                                language,
                                LegalDocumentSource.Network,
                            )
                        }
                            .getOrNull()
                            ?.let { document ->
                                cache.put(result.document.toCache(result.metadata))
                                return Loaded.Success(document)
                            }
                    is LegalDocumentResult.NotModified ->
                        cached?.toDomain(type, language, LegalDocumentSource.Cache)?.let { document
                            ->
                            cache.put(
                                cached.copy(
                                    etag = result.metadata.etag,
                                    contentLanguage = result.metadata.contentLanguage,
                                    cacheControl = result.metadata.cacheControl,
                                )
                            )
                            return Loaded.Success(document)
                        }
                }
            is ApiResponse.Error -> Unit
        }
        cached?.toDomain(type, language, LegalDocumentSource.Cache)?.let {
            return Loaded.Success(it)
        }
        bundled.read(type, language)?.let { raw ->
            runCatching {
                json
                    .decodeFromString<dev.lscythe.app.navigo.api.legal.dto.LegalDocument>(raw)
                    .toDomain(type, language, LegalDocumentSource.Bundled)
            }
                .getOrNull()
                ?.let {
                    return Loaded.Success(it)
                }
        }
        return Loaded.Failure(response.toFailure())
    }

    private sealed interface Loaded {
        data class Success(val document: LegalDocument) : Loaded

        data class Failure(val failure: LegalFailure) : Loaded
    }

    private companion object {
        val json = Json { ignoreUnknownKeys = false }
    }
}

val LegalDocumentType.wireValue
    get() =
        when (this) {
            LegalDocumentType.Terms -> "terms"
            LegalDocumentType.Privacy -> "privacy"
        }
val AppLanguage.wireValue
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

private fun dev.lscythe.app.navigo.api.legal.dto.LegalDocument.toDomain(
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

private fun dev.lscythe.app.navigo.api.legal.dto.LegalDocument.toCache(
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

private fun LegalDocumentCachePreference.toDomain(
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

private fun ApiResponse<*>.toFailure(): LegalFailure =
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
                    else -> null
                }
            )
        is ApiResponse.Success ->
            LegalFailure.CacheInconsistency("Response could not produce valid content")
    }
