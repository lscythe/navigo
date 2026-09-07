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
package dev.lscythe.app.navigo.feature.onboarding.impl

import androidx.compose.runtime.Immutable
import dev.lscythe.app.navigo.core.common.locale.SupportedLanguage
import dev.lscythe.app.navigo.domain.legal.model.LegalDocument
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSet
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage

internal const val DefaultAvatarColorArgb: UInt = 0xFF17473Cu

internal enum class OnboardingStage {
    Introduction,
    Permissions,
    Profile,
}

internal enum class OnboardingLanguage {
    English,
    Indonesian,
}

internal val OnboardingLanguage.languageTag: String
    get() =
        when (this) {
            OnboardingLanguage.English -> "en"
            OnboardingLanguage.Indonesian -> "id"
        }

@Immutable
internal data class OnboardingLegalDocumentUiState(
    val languageTag: String,
    val version: String,
    val title: String,
    val readingTimeMinutes: Int,
    val summaryHtml: String,
    val bodyHtml: String,
)

@Immutable
internal data class OnboardingLegalDocumentsUiState(
    val terms: OnboardingLegalDocumentUiState,
    val privacy: OnboardingLegalDocumentUiState,
)

internal fun LegalDocumentSet.toUiModel() =
    OnboardingLegalDocumentsUiState(
        terms = terms.toUiModel(),
        privacy = privacy.toUiModel(),
    )

private fun LegalDocument.toUiModel() =
    OnboardingLegalDocumentUiState(
        languageTag = if (language == AppLanguage.Indonesian) "id" else "en",
        version = version,
        title = title,
        readingTimeMinutes = readingTimeMinutes,
        summaryHtml = summaryHtml,
        bodyHtml = bodyHtml,
    )

internal fun OnboardingLanguage.toSupportedLanguage() =
    when (this) {
        OnboardingLanguage.English -> SupportedLanguage.English
        OnboardingLanguage.Indonesian -> SupportedLanguage.Indonesian
    }

internal fun SupportedLanguage.toOnboardingLanguage() =
    when (this) {
        SupportedLanguage.English -> OnboardingLanguage.English
        SupportedLanguage.Indonesian -> OnboardingLanguage.Indonesian
    }

internal sealed interface OnboardingIntent {
    data object IntroductionContinued : OnboardingIntent

    data object PermissionChoiceSelected : OnboardingIntent

    data object BackClicked : OnboardingIntent

    data object SkipClicked : OnboardingIntent

    data class LanguageSelected(val language: OnboardingLanguage) : OnboardingIntent

    data class NameChanged(val value: String) : OnboardingIntent

    data class AvatarColorSelected(val argb: UInt) : OnboardingIntent

    data class AnalyticsConsentChanged(val enabled: Boolean) : OnboardingIntent

    data class CrashConsentChanged(val enabled: Boolean) : OnboardingIntent

    data object LegalDocumentsRequested : OnboardingIntent

    data object LegalDocumentsAccepted : OnboardingIntent

    data object OpenMapClicked : OnboardingIntent

    data object RideAsGuestClicked : OnboardingIntent

    data object FailureDismissed : OnboardingIntent
}

internal sealed interface OnboardingEffect {
    data object NavigateHome : OnboardingEffect
}

@Immutable
internal data class OnboardingUiState(
    val stage: OnboardingStage = OnboardingStage.Introduction,
    val language: OnboardingLanguage = OnboardingLanguage.English,
    val displayName: String = "",
    val avatarColorArgb: UInt = DefaultAvatarColorArgb,
    val analyticsEnabled: Boolean = false,
    val crashReportsEnabled: Boolean = false,
    val legalDocuments: OnboardingLegalDocumentsUiState? = null,
    val legalAccepted: Boolean = false,
    val legalLoading: Boolean = false,
    val legalFailureMessage: String? = null,
    val completionLoading: Boolean = false,
    val completionFailureMessage: String? = null,
)
