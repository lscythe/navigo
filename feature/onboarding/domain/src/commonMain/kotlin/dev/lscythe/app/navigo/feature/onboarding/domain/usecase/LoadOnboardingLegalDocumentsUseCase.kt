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
package dev.lscythe.app.navigo.feature.onboarding.domain.usecase

import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSet
import dev.lscythe.app.navigo.domain.legal.repository.LegalRepository
import dev.lscythe.app.navigo.domain.legal.repository.LegalResult
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingFailure
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingResult
import dev.zacsweers.metro.Inject

@Inject
class LoadOnboardingLegalDocumentsUseCase(private val repository: LegalRepository) {
    suspend operator fun invoke(language: AppLanguage): OnboardingResult<LegalDocumentSet> {
        if (language == AppLanguage.System)
            return OnboardingResult.Failure(OnboardingFailure.UnresolvedLanguage)
        return when (val result = repository.getDocuments(language)) {
            is LegalResult.Failure ->
                OnboardingResult.Failure(OnboardingFailure.Legal(result.failure))
            is LegalResult.Success ->
                if (
                    result.value.terms.language == language &&
                        result.value.privacy.language == language
                )
                    OnboardingResult.Success(result.value)
                else OnboardingResult.Failure(OnboardingFailure.LegalLanguageMismatch)
        }
    }
}
