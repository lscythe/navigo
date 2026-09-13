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

import dev.lscythe.app.navigo.core.common.platform.AppPlatform
import dev.lscythe.app.navigo.domain.legal.model.LegalDocument
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSet
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSource
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentStatus
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentType
import dev.lscythe.app.navigo.domain.legal.repository.LegalRepository
import dev.lscythe.app.navigo.domain.legal.repository.LegalResult
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingResult
import dev.lscythe.app.navigo.feature.onboarding.domain.model.ValidatedOnboardingCompletion
import dev.lscythe.app.navigo.feature.onboarding.domain.repository.OnboardingCompletionRepository
import dev.lscythe.app.navigo.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import dev.lscythe.app.navigo.feature.onboarding.domain.usecase.LoadOnboardingLegalDocumentsUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest :
    BehaviorSpec({
        val dispatcher = StandardTestDispatcher()
        beforeTest { Dispatchers.setMain(dispatcher) }
        afterTest { Dispatchers.resetMain() }

        Given("an onboarding ViewModel") {
            When("navigation and controlled-field intents are dispatched") {
                Then("it reduces them into one state snapshot") {
                    val viewModel = viewModel()

                    viewModel.onIntent(OnboardingIntent.IntroductionContinued)
                    viewModel.onIntent(OnboardingIntent.PermissionChoiceSelected)
                    viewModel.onIntent(OnboardingIntent.PermissionsCompleted)
                    viewModel.onIntent(OnboardingIntent.NameChanged("Nara"))
                    viewModel.onIntent(OnboardingIntent.AvatarColorSelected(7u))
                    viewModel.onIntent(OnboardingIntent.AnalyticsConsentChanged(true))

                    viewModel.state.value.stage shouldBe OnboardingStage.Profile
                    viewModel.state.value.displayName shouldBe "Nara"
                    viewModel.state.value.avatarColorArgb shouldBe 7u
                    viewModel.state.value.analyticsEnabled.shouldBeTrue()
                }
            }

            When("profile stage is entered") {
                Then("it prefetches legal documents") {
                    runTest(dispatcher) {
                        val legal = FakeLegalRepository()
                        val viewModel = viewModel(legal = legal)

                        viewModel.onIntent(OnboardingIntent.IntroductionContinued)
                        viewModel.onIntent(OnboardingIntent.PermissionChoiceSelected)
                        viewModel.onIntent(OnboardingIntent.PermissionsCompleted)
                        advanceUntilIdle()

                        legal.requests shouldBe listOf(AppLanguage.English)
                        viewModel.state.value.legalDocuments?.terms?.version shouldBe "terms-v1"
                    }
                }
            }

            When("language changes on profile stage") {
                Then("it immediately prefetches the selected language") {
                    runTest(dispatcher) {
                        val legal = FakeLegalRepository()
                        val viewModel = viewModel(legal = legal)
                        viewModel.onIntent(OnboardingIntent.IntroductionContinued)
                        viewModel.onIntent(OnboardingIntent.PermissionChoiceSelected)
                        viewModel.onIntent(OnboardingIntent.PermissionsCompleted)
                        advanceUntilIdle()

                        viewModel.onIntent(
                            OnboardingIntent.LanguageSelected(OnboardingLanguage.Indonesian)
                        )
                        advanceUntilIdle()

                        legal.requests shouldBe listOf(AppLanguage.English, AppLanguage.Indonesian)
                        viewModel.state.value.legalDocuments?.terms?.languageTag shouldBe "id"
                    }
                }
            }

            When("a new language is selected") {
                Then("it changes language immediately") {
                    val viewModel = viewModel()

                    viewModel.onIntent(
                        OnboardingIntent.LanguageSelected(OnboardingLanguage.English)
                    )

                    viewModel.state.value.language shouldBe OnboardingLanguage.English
                }
            }

            When("system language is selected on an Indonesian system") {
                Then("it keeps system preference and resolves Indonesian content") {
                    val viewModel = viewModel()

                    viewModel.onIntent(
                        OnboardingIntent.LanguageSelected(
                            language = OnboardingLanguage.System,
                            effectiveLanguage = OnboardingLanguage.Indonesian,
                        )
                    )

                    viewModel.state.value.language shouldBe OnboardingLanguage.System
                    viewModel.state.value.effectiveLanguage shouldBe OnboardingLanguage.Indonesian
                }
            }

            When("system language is unsupported") {
                Then("it falls back to English content") {
                    val viewModel = viewModel()

                    viewModel.onIntent(
                        OnboardingIntent.LanguageSelected(
                            language = OnboardingLanguage.System,
                            effectiveLanguage = OnboardingLanguage.English,
                        )
                    )

                    viewModel.state.value.language shouldBe OnboardingLanguage.System
                    viewModel.state.value.effectiveLanguage shouldBe OnboardingLanguage.English
                }
            }

            When("language changes after legal acceptance") {
                Then("it invalidates loaded documents and acceptance") {
                    runTest(dispatcher) {
                        val viewModel = viewModel()
                        viewModel.onIntent(OnboardingIntent.LegalDocumentsRequested)
                        advanceUntilIdle()
                        viewModel.onIntent(OnboardingIntent.LegalDocumentsAccepted)

                        viewModel.onIntent(
                            OnboardingIntent.LanguageSelected(OnboardingLanguage.English)
                        )

                        viewModel.state.value.language shouldBe OnboardingLanguage.English
                        viewModel.state.value.legalDocuments shouldBe null
                        viewModel.state.value.legalAccepted.shouldBeFalse()
                    }
                }
            }

            When("legal documents load successfully") {
                Then("it publishes immutable legal UI state") {
                    runTest(dispatcher) {
                        val viewModel = viewModel()

                        viewModel.onIntent(OnboardingIntent.LegalDocumentsRequested)
                        advanceUntilIdle()

                        viewModel.state.value.legalLoading.shouldBeFalse()
                        viewModel.state.value.legalDocuments?.terms?.version shouldBe "terms-v1"
                        viewModel.state.value.legalDocuments?.privacy?.version shouldBe "privacy-v1"
                    }
                }
            }

            When("open-map completion succeeds") {
                Then("it persists entered identity and emits one navigation effect") {
                    runTest(dispatcher) {
                        val completion = FakeCompletionRepository()
                        val viewModel = viewModel(completion)
                        viewModel.onIntent(OnboardingIntent.NameChanged("  Nara  "))
                        viewModel.onIntent(OnboardingIntent.AvatarColorSelected(7u))
                        viewModel.onIntent(OnboardingIntent.LegalDocumentsRequested)
                        advanceUntilIdle()
                        viewModel.onIntent(OnboardingIntent.LegalDocumentsAccepted)
                        val effect = async { viewModel.effects.first() }

                        viewModel.onIntent(OnboardingIntent.OpenMapClicked)
                        advanceUntilIdle()

                        completion.value?.profile?.displayName shouldBe "Nara"
                        completion.value?.profile?.avatarColorArgb shouldBe 7u
                        effect.await() shouldBe OnboardingEffect.NavigateHome
                        viewModel.state.value.completionLoading.shouldBeFalse()
                    }
                }
            }

            When("guest completion uses Indonesian") {
                Then("it replaces entered identity with localized guest defaults") {
                    runTest(dispatcher) {
                        val completion = FakeCompletionRepository()
                        val viewModel = viewModel(completion)
                        viewModel.onIntent(
                            OnboardingIntent.LanguageSelected(OnboardingLanguage.Indonesian)
                        )
                        viewModel.onIntent(OnboardingIntent.NameChanged("Nara"))
                        viewModel.onIntent(OnboardingIntent.AvatarColorSelected(7u))
                        viewModel.onIntent(OnboardingIntent.LegalDocumentsRequested)
                        advanceUntilIdle()
                        viewModel.onIntent(OnboardingIntent.LegalDocumentsAccepted)

                        viewModel.onIntent(OnboardingIntent.RideAsGuestClicked)
                        advanceUntilIdle()

                        completion.value?.profile?.displayName shouldBe "Tamu"
                        completion.value?.profile?.avatarColorArgb shouldBe DefaultAvatarColorArgb
                    }
                }
            }

            When("completion fails") {
                Then("it preserves form state and emits no navigation effect") {
                    runTest(dispatcher) {
                        val completion = FakeCompletionRepository(fail = true)
                        val viewModel = viewModel(completion)
                        viewModel.onIntent(OnboardingIntent.NameChanged("Nara"))
                        viewModel.onIntent(OnboardingIntent.LegalDocumentsRequested)
                        advanceUntilIdle()
                        viewModel.onIntent(OnboardingIntent.LegalDocumentsAccepted)

                        viewModel.onIntent(OnboardingIntent.OpenMapClicked)
                        advanceUntilIdle()

                        viewModel.state.value.displayName shouldBe "Nara"
                        viewModel.state.value.completionFailureMessage shouldBe "disk full"
                        viewModel.effects.firstOrNullImmediately().shouldBeNull()
                    }
                }
            }
        }
    })

private fun viewModel(
    completion: FakeCompletionRepository = FakeCompletionRepository(),
    legal: FakeLegalRepository = FakeLegalRepository(),
): OnboardingViewModel {
    return OnboardingViewModel(
        LoadOnboardingLegalDocumentsUseCase(legal),
        CompleteOnboardingUseCase(completion),
        AppPlatform.Android,
    )
}

private class FakeLegalRepository : LegalRepository {
    val requests = mutableListOf<AppLanguage>()

    override suspend fun getDocuments(language: AppLanguage): LegalResult<LegalDocumentSet> {
        requests += language
        return LegalResult.Success(documents(language))
    }
}

private class FakeCompletionRepository(private val fail: Boolean = false) :
    OnboardingCompletionRepository {
    var value: ValidatedOnboardingCompletion? = null

    override suspend fun complete(
        completion: ValidatedOnboardingCompletion
    ): OnboardingResult<Unit> {
        value = completion
        return if (fail) {
            OnboardingResult.Failure(
                dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingFailure
                    .Persistence("disk full")
            )
        } else OnboardingResult.Success(Unit)
    }
}

private fun documents(language: AppLanguage) =
    LegalDocumentSet(
        document(LegalDocumentType.Terms, language, "terms-v1"),
        document(LegalDocumentType.Privacy, language, "privacy-v1"),
    )

private fun document(type: LegalDocumentType, language: AppLanguage, version: String) =
    LegalDocument(
        type = type,
        language = language,
        status = LegalDocumentStatus.Published,
        version = version,
        effectiveAt = LocalDate(2026, 1, 1),
        title = type.name,
        readingTimeMinutes = 1,
        summaryHtml = "summary",
        bodyHtml = "body",
        canonicalUrl = "https://example.com/$version",
        source = LegalDocumentSource.Network,
    )

private suspend fun kotlinx.coroutines.flow.Flow<OnboardingEffect>.firstOrNullImmediately():
    OnboardingEffect? = kotlinx.coroutines.withTimeoutOrNull(1) { first() }
