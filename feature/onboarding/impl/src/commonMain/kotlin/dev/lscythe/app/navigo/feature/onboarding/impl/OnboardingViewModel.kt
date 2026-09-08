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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.lscythe.app.navigo.domain.legal.model.AcceptedLegalDocument
import dev.lscythe.app.navigo.domain.legal.model.LegalAcceptance
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSet
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.domain.settings.model.PrivacySettings
import dev.lscythe.app.navigo.domain.user.model.UserProfile
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingCompletion
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingFailure
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingResult
import dev.lscythe.app.navigo.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import dev.lscythe.app.navigo.feature.onboarding.domain.usecase.LoadOnboardingLegalDocumentsUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class OnboardingViewModel(
    private val loadOnboardingLegalDocuments: LoadOnboardingLegalDocumentsUseCase,
    private val completeOnboarding: CompleteOnboardingUseCase,
) : ViewModel() {
    private val mutableState = MutableStateFlow(OnboardingUiState())
    internal val state: StateFlow<OnboardingUiState> = mutableState.asStateFlow()
    private val effectChannel = Channel<OnboardingEffect>(Channel.BUFFERED)
    internal val effects: Flow<OnboardingEffect> = effectChannel.receiveAsFlow()
    private var loadedDocuments: LegalDocumentSet? = null

    internal fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            OnboardingIntent.IntroductionContinued ->
                update { copy(stage = OnboardingStage.Permissions) }
            OnboardingIntent.PermissionChoiceSelected,
            OnboardingIntent.SkipClicked -> {
                update { copy(stage = OnboardingStage.Profile) }
                loadLegalDocuments()
            }
            OnboardingIntent.BackClicked ->
                update {
                    copy(
                        stage =
                            when (stage) {
                                OnboardingStage.Introduction -> OnboardingStage.Introduction
                                OnboardingStage.Permissions -> OnboardingStage.Introduction
                                OnboardingStage.Profile -> OnboardingStage.Permissions
                            }
                    )
                }
            is OnboardingIntent.LanguageSelected ->
                selectLanguage(intent.language, intent.effectiveLanguage)
            is OnboardingIntent.NameChanged -> update { copy(displayName = intent.value) }
            is OnboardingIntent.AvatarColorSelected ->
                update { copy(avatarColorArgb = intent.argb) }
            is OnboardingIntent.AnalyticsConsentChanged ->
                update { copy(analyticsEnabled = intent.enabled) }
            is OnboardingIntent.CrashConsentChanged ->
                update { copy(crashReportsEnabled = intent.enabled) }
            OnboardingIntent.LegalDocumentsRequested -> loadLegalDocuments()
            OnboardingIntent.LegalDocumentsAccepted ->
                if (loadedDocuments != null) update { copy(legalAccepted = true) }
            OnboardingIntent.OpenMapClicked -> complete(asGuest = false)
            OnboardingIntent.RideAsGuestClicked -> complete(asGuest = true)
            OnboardingIntent.FailureDismissed ->
                update { copy(legalFailureMessage = null, completionFailureMessage = null) }
        }
    }

    private fun selectLanguage(
        language: OnboardingLanguage,
        effectiveLanguage: OnboardingLanguage,
    ) {
        require(effectiveLanguage != OnboardingLanguage.System)
        loadedDocuments = null
        update {
            copy(
                language = language,
                effectiveLanguage = effectiveLanguage,
                legalDocuments = null,
                legalAccepted = false,
                legalLoading = false,
                legalFailureMessage = null,
            )
        }
        if (mutableState.value.stage == OnboardingStage.Profile) loadLegalDocuments()
    }

    private fun loadLegalDocuments() {
        val requestLanguage = mutableState.value.effectiveLanguage
        if (mutableState.value.legalLoading || loadedDocuments != null) return
        update { copy(legalLoading = true, legalFailureMessage = null) }
        viewModelScope.launch {
            try {
                when (val result = loadOnboardingLegalDocuments(requestLanguage.toDomain())) {
                    is OnboardingResult.Success -> {
                        if (mutableState.value.effectiveLanguage != requestLanguage) return@launch
                        loadedDocuments = result.value
                        update {
                            copy(
                                legalDocuments = result.value.toUiModel(),
                                legalAccepted = false,
                                legalLoading = false,
                            )
                        }
                    }
                    is OnboardingResult.Failure -> {
                        if (mutableState.value.effectiveLanguage != requestLanguage) return@launch
                        update {
                            copy(
                                legalLoading = false,
                                legalFailureMessage = result.failure.userMessage(),
                            )
                        }
                    }
                }
            } catch (error: CancellationException) {
                throw error
            }
        }
    }

    private fun complete(asGuest: Boolean) {
        val snapshot = mutableState.value
        val documents = loadedDocuments
        if (snapshot.completionLoading) return
        if (!snapshot.legalAccepted || documents == null) {
            update { copy(completionFailureMessage = "Read and accept the legal documents first.") }
            return
        }
        update { copy(completionLoading = true, completionFailureMessage = null) }
        viewModelScope.launch {
            try {
                val displayName =
                    if (asGuest) {
                        when (snapshot.effectiveLanguage) {
                            OnboardingLanguage.System ->
                                error("Effective language must be resolved")
                            OnboardingLanguage.English -> "Guest"
                            OnboardingLanguage.Indonesian -> "Tamu"
                        }
                    } else snapshot.displayName
                val profile =
                    UserProfile(
                        displayName,
                        if (asGuest) DefaultAvatarColorArgb else snapshot.avatarColorArgb,
                    )
                when (
                    val result =
                        completeOnboarding(
                            OnboardingCompletion(
                                profile = profile,
                                language = snapshot.language.toDomain(),
                                effectiveLanguage = snapshot.effectiveLanguage.toDomain(),
                                privacy =
                                    PrivacySettings(
                                        snapshot.analyticsEnabled,
                                        snapshot.crashReportsEnabled,
                                    ),
                                documents = documents,
                                acceptance = documents.acceptance(),
                            )
                        )
                ) {
                    is OnboardingResult.Success -> {
                        update { copy(completionLoading = false) }
                        effectChannel.send(OnboardingEffect.NavigateHome)
                    }
                    is OnboardingResult.Failure ->
                        update {
                            copy(
                                completionLoading = false,
                                completionFailureMessage = result.failure.userMessage(),
                            )
                        }
                }
            } catch (error: CancellationException) {
                throw error
            }
        }
    }

    private inline fun update(transform: OnboardingUiState.() -> OnboardingUiState) {
        mutableState.value = mutableState.value.transform()
    }
}

private fun OnboardingLanguage.toDomain() =
    when (this) {
        OnboardingLanguage.System -> AppLanguage.System
        OnboardingLanguage.English -> AppLanguage.English
        OnboardingLanguage.Indonesian -> AppLanguage.Indonesian
    }

private fun LegalDocumentSet.acceptance() =
    LegalAcceptance(
        AcceptedLegalDocument(terms.version, terms.language),
        AcceptedLegalDocument(privacy.version, privacy.language),
    )

private fun OnboardingFailure.userMessage(): String =
    when (this) {
        OnboardingFailure.InvalidProfile -> "Enter a display name."
        OnboardingFailure.UnresolvedLanguage -> "Choose a language."
        OnboardingFailure.LegalLanguageMismatch,
        OnboardingFailure.AcceptanceMismatch -> "Legal documents changed. Review them again."
        is OnboardingFailure.Legal -> "Unable to load legal documents."
        is OnboardingFailure.Persistence -> message ?: "Unable to save onboarding."
        is OnboardingFailure.Unknown -> message ?: "Something went wrong."
    }
