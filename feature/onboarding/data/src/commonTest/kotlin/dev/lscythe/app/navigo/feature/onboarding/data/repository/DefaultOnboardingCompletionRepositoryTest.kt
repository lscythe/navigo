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

import app.cash.turbine.test
import dev.lscythe.app.navigo.core.persistence.Language
import dev.lscythe.app.navigo.core.persistence.ThemeMode
import dev.lscythe.app.navigo.core.persistence.ThemePreference
import dev.lscythe.app.navigo.core.persistence.datasource.NavigoPreferenceDataSource
import dev.lscythe.app.navigo.domain.legal.model.AcceptedLegalDocument
import dev.lscythe.app.navigo.domain.legal.model.LegalAcceptance
import dev.lscythe.app.navigo.domain.legal.model.LegalDocument
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSet
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSource
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentStatus
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentType
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.domain.settings.model.PrivacySettings
import dev.lscythe.app.navigo.domain.user.model.UserProfile
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingCompletion
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingResult
import dev.lscythe.app.navigo.feature.onboarding.domain.model.ValidatedOnboardingCompletion
import dev.lscythe.app.navigo.feature.onboarding.domain.repository.OnboardingCompletionRepository
import dev.lscythe.app.navigo.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import eu.anifantakis.lib.ksafe.KSafe
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class DefaultOnboardingCompletionRepositoryTest :
    FunSpec({
        test("completion persists every field atomically and preserves unrelated preferences") {
            withRepository("completion") { repository, source ->
                val theme = ThemePreference(mode = ThemeMode.Dark)
                source.setTheme(theme)

                repository.complete(validated()) shouldBe OnboardingResult.Success(Unit)

                source.data.test {
                    awaitItem().let { preference ->
                        preference.displayName shouldBe "Nara"
                        preference.avatarColorArgb shouldBe 7u
                        preference.language shouldBe Language.Indonesian
                        preference.analyticsEnabled shouldBe true
                        preference.crashReportsEnabled shouldBe false
                        preference.acceptedTerms?.version shouldBe "terms-v1"
                        preference.acceptedTerms?.language shouldBe Language.Indonesian
                        preference.acceptedPrivacy?.version shouldBe "privacy-v1"
                        preference.acceptedPrivacy?.language shouldBe Language.Indonesian
                        preference.hasCompletedOnboarding shouldBe true
                        preference.theme shouldBe theme
                    }
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    })

private suspend fun withRepository(
    suffix: String,
    block: suspend (DefaultOnboardingCompletionRepository, NavigoPreferenceDataSource) -> Unit,
) {
    val ksafe = KSafe(fileName = "navigo_test_onboarding_repository_${suffix.lowercase()}")
    try {
        val source = NavigoPreferenceDataSource(ksafe)
        block(DefaultOnboardingCompletionRepository(source), source)
    } finally {
        ksafe.clearAll()
        ksafe.close()
    }
}

private suspend fun validated(): ValidatedOnboardingCompletion {
    val documents =
        LegalDocumentSet(
            document(LegalDocumentType.Terms, "terms-v1"),
            document(LegalDocumentType.Privacy, "privacy-v1"),
        )
    var validated: ValidatedOnboardingCompletion? = null
    val repository =
        object : OnboardingCompletionRepository {
            override suspend fun complete(
                completion: ValidatedOnboardingCompletion
            ): OnboardingResult<Unit> {
                validated = completion
                return OnboardingResult.Success(Unit)
            }
        }
    CompleteOnboardingUseCase(repository)(
        OnboardingCompletion(
            profile = UserProfile("Nara", 7u),
            language = AppLanguage.Indonesian,
            effectiveLanguage = AppLanguage.Indonesian,
            privacy = PrivacySettings(analyticsEnabled = true, crashReportsEnabled = false),
            documents = documents,
            acceptance =
                LegalAcceptance(
                    terms = AcceptedLegalDocument("terms-v1", AppLanguage.Indonesian),
                    privacy = AcceptedLegalDocument("privacy-v1", AppLanguage.Indonesian),
                ),
        )
    )
    return requireNotNull(validated)
}

private fun document(type: LegalDocumentType, version: String) =
    LegalDocument(
        type = type,
        language = AppLanguage.Indonesian,
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
