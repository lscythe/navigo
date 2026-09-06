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
package dev.lscythe.app.navigo.feature.onboarding.data.repository

import dev.lscythe.app.navigo.core.persistence.*
import dev.lscythe.app.navigo.domain.legal.model.*
import dev.lscythe.app.navigo.domain.settings.model.*
import dev.lscythe.app.navigo.domain.user.model.UserProfile
import dev.lscythe.app.navigo.feature.onboarding.domain.model.*
import dev.lscythe.app.navigo.feature.onboarding.domain.repository.*
import dev.lscythe.app.navigo.feature.onboarding.domain.usecase.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.coroutines.cancellation.CancellationException

class DefaultOnboardingCompletionRepositoryTest :
    FunSpec({
        test("completion persists every field in one mutation") {
            val store = FakeCompletionStore()
            val repository = DefaultOnboardingCompletionRepository(store)
            repository.complete(validated()) shouldBe OnboardingResult.Success(Unit)
            store.calls shouldBe 1
            store.value.displayName shouldBe "Nara"
            store.value.avatarColorArgb shouldBe 7u
            store.value.language shouldBe Language.Indonesian
            store.value.analyticsEnabled shouldBe true
            store.value.crashReportsEnabled shouldBe false
            store.value.acceptedTerms shouldBe
                LegalAcceptancePreference("terms-v1", Language.Indonesian)
            store.value.acceptedPrivacy shouldBe
                LegalAcceptancePreference("privacy-v1", Language.Indonesian)
            store.value.hasCompletedOnboarding shouldBe true
            store.value.theme shouldBe ThemePreference(mode = ThemeMode.Dark)
        }
        test("persistence failure maps without changing record") {
            val initial = UserPreference(theme = ThemePreference(mode = ThemeMode.Dark))
            val store = FakeCompletionStore(initial, IllegalStateException("disk"))
            DefaultOnboardingCompletionRepository(store).complete(validated()) shouldBe
                OnboardingResult.Failure(OnboardingFailure.Persistence("disk"))
            store.value shouldBe initial
        }
        test("cancellation propagates") {
            val repository =
                DefaultOnboardingCompletionRepository(
                    FakeCompletionStore(failure = CancellationException())
                )
            shouldThrow<CancellationException> { repository.complete(validated()) }
        }
    })

private class FakeCompletionStore(
    initial: UserPreference = UserPreference(theme = ThemePreference(mode = ThemeMode.Dark)),
    private val failure: Throwable? = null,
) : OnboardingPreferenceStore {
    var value = initial
    var calls = 0

    override suspend fun complete(completion: OnboardingPreferenceCompletion) {
        calls++
        failure?.let { throw it }
        value =
            value.copy(
                displayName = completion.displayName,
                avatarColorArgb = completion.avatarColorArgb,
                language = completion.language,
                analyticsEnabled = completion.analyticsEnabled,
                crashReportsEnabled = completion.crashReportsEnabled,
                acceptedTerms = completion.acceptedTerms,
                acceptedPrivacy = completion.acceptedPrivacy,
                hasCompletedOnboarding = true,
            )
    }
}

private fun validated(): ValidatedOnboardingCompletion {
    val docs =
        LegalDocumentSet(
            LegalDocument(
                LegalDocumentType.Terms,
                AppLanguage.Indonesian,
                LegalDocumentStatus.Published,
                "terms-v1",
                kotlinx.datetime.LocalDate(2026, 1, 1),
                "Terms",
                1,
                "s",
                "b",
                "u",
                LegalDocumentSource.Network,
            ),
            LegalDocument(
                LegalDocumentType.Privacy,
                AppLanguage.Indonesian,
                LegalDocumentStatus.Published,
                "privacy-v1",
                kotlinx.datetime.LocalDate(2026, 1, 1),
                "Privacy",
                1,
                "s",
                "b",
                "u",
                LegalDocumentSource.Network,
            ),
        )
    val acceptance =
        LegalAcceptance(
            AcceptedLegalDocument("terms-v1", AppLanguage.Indonesian),
            AcceptedLegalDocument("privacy-v1", AppLanguage.Indonesian),
        )
    var captured: ValidatedOnboardingCompletion? = null
    val capturing =
        object : OnboardingCompletionRepository {
            override suspend fun complete(
                completion: ValidatedOnboardingCompletion
            ): OnboardingResult<Unit> {
                captured = completion
                return OnboardingResult.Success(Unit)
            }
        }
    kotlinx.coroutines.test.runTest {
        CompleteOnboardingUseCase(capturing)(
            OnboardingCompletion(
                UserProfile("Nara", 7u),
                AppLanguage.Indonesian,
                PrivacySettings(true, false),
                docs,
                acceptance,
            )
        )
    }
    return requireNotNull(captured)
}
