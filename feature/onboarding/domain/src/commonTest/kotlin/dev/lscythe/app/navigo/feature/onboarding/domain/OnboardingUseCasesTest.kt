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
package dev.lscythe.app.navigo.feature.onboarding.domain

import dev.lscythe.app.navigo.domain.legal.model.*
import dev.lscythe.app.navigo.domain.legal.repository.*
import dev.lscythe.app.navigo.domain.settings.model.*
import dev.lscythe.app.navigo.domain.user.model.UserProfile
import dev.lscythe.app.navigo.feature.onboarding.domain.model.*
import dev.lscythe.app.navigo.feature.onboarding.domain.repository.*
import dev.lscythe.app.navigo.feature.onboarding.domain.usecase.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class OnboardingUseCasesTest :
    FunSpec({
        test("legal loader rejects system language before repository call") {
            val repository = FakeLegalRepository(documents(AppLanguage.English))
            LoadOnboardingLegalDocumentsUseCase(repository)(AppLanguage.System) shouldBe
                OnboardingResult.Failure(OnboardingFailure.UnresolvedLanguage)
            repository.calls shouldBe 0
        }
        test("legal loader rejects language mismatch") {
            val repository = FakeLegalRepository(documents(AppLanguage.English))
            LoadOnboardingLegalDocumentsUseCase(repository)(AppLanguage.Indonesian) shouldBe
                OnboardingResult.Failure(OnboardingFailure.LegalLanguageMismatch)
        }
        test("completion normalizes and validates before repository call") {
            val repository = FakeCompletionRepository()
            val docs = documents(AppLanguage.English)
            val input =
                OnboardingCompletion(
                    UserProfile("  Nara  ", 1u),
                    AppLanguage.English,
                    PrivacySettings(true, false),
                    docs,
                    acceptance(docs),
                )
            CompleteOnboardingUseCase(repository)(input) shouldBe OnboardingResult.Success(Unit)
            repository.value?.profile?.displayName shouldBe "Nara"
        }
        test("completion rejects mismatched acceptance before repository call") {
            val repository = FakeCompletionRepository()
            val docs = documents(AppLanguage.English)
            val bad =
                acceptance(docs).copy(terms = AcceptedLegalDocument("old", AppLanguage.English))
            CompleteOnboardingUseCase(repository)(
                OnboardingCompletion(
                    UserProfile("Nara", 1u),
                    AppLanguage.English,
                    PrivacySettings(true, false),
                    docs,
                    bad,
                )
            ) shouldBe OnboardingResult.Failure(OnboardingFailure.AcceptanceMismatch)
            repository.calls shouldBe 0
        }
    })

private class FakeLegalRepository(private val set: LegalDocumentSet) : LegalRepository {
    var calls = 0

    override suspend fun getDocuments(language: AppLanguage): LegalResult<LegalDocumentSet> {
        calls++
        return LegalResult.Success(set)
    }
}

private class FakeCompletionRepository : OnboardingCompletionRepository {
    var calls = 0
    var value: ValidatedOnboardingCompletion? = null

    override suspend fun complete(
        completion: ValidatedOnboardingCompletion
    ): OnboardingResult<Unit> {
        calls++
        value = completion
        return OnboardingResult.Success(Unit)
    }
}

private fun documents(language: AppLanguage): LegalDocumentSet =
    LegalDocumentSet(
        document(LegalDocumentType.Terms, language),
        document(LegalDocumentType.Privacy, language),
    )

private fun document(type: LegalDocumentType, language: AppLanguage) =
    LegalDocument(
        type,
        language,
        LegalDocumentStatus.Published,
        "1",
        LocalDate(2026, 1, 1),
        "Title",
        1,
        "summary",
        "body",
        "https://example.com",
        LegalDocumentSource.Network,
    )

private fun acceptance(set: LegalDocumentSet) =
    LegalAcceptance(
        AcceptedLegalDocument(set.terms.version, set.terms.language),
        AcceptedLegalDocument(set.privacy.version, set.privacy.language),
    )
