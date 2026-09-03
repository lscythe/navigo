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
package dev.lscythe.app.navigo.feature.onboarding.impl.legal

import dev.lscythe.app.navigo.core.common.locale.SupportedLanguage
import dev.lscythe.app.navigo.feature.onboarding.impl.generated.resources.Res
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
internal enum class LegalDocumentType(val assetDirectory: String) {
    @SerialName("terms") Terms("terms"),
    @SerialName("privacy") Privacy("privacy"),
}

@Serializable
private data class BundledLegalDocument(
    val slug: LegalDocumentType,
    val language: String,
    val version: String,
    val title: String,
    val readingTimeMinutes: Int,
    val summaryHtml: String,
    val bodyHtml: String,
)

internal fun legalAssetPath(
    type: LegalDocumentType,
    language: SupportedLanguage?,
): String =
    "${type.assetDirectory}/${language?.languageTag ?: SupportedLanguage.English.languageTag}.json"

private val json = Json { ignoreUnknownKeys = true }

internal suspend fun loadBundledLegalDocument(
    type: LegalDocumentType,
    language: SupportedLanguage?,
): LegalDocumentUiModel =
    json
        .decodeFromString<BundledLegalDocument>(
            Res.readBytes("files/${legalAssetPath(type, language)}").decodeToString()
        )
        .toUiModel()

private fun BundledLegalDocument.toUiModel(): LegalDocumentUiModel =
    LegalDocumentUiModel(
        type = slug,
        languageTag = language,
        version = version,
        title = title,
        readingTimeMinutes = readingTimeMinutes,
        summaryHtml = summaryHtml,
        bodyHtml = bodyHtml,
    )
