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

import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingCompletion
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingFailure
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingResult
import dev.lscythe.app.navigo.feature.onboarding.domain.model.ValidatedOnboardingCompletion
import dev.lscythe.app.navigo.feature.onboarding.domain.repository.OnboardingCompletionRepository
import dev.zacsweers.metro.Inject

@Inject
class CompleteOnboardingUseCase(private val repository: OnboardingCompletionRepository) {
    suspend operator fun invoke(completion: OnboardingCompletion): OnboardingResult<Unit> {
        val displayName = completion.profile.displayName.trim()
        if (displayName.isEmpty()) return OnboardingResult.Failure(OnboardingFailure.InvalidProfile)
        if (completion.effectiveLanguage == AppLanguage.System)
            return OnboardingResult.Failure(OnboardingFailure.UnresolvedLanguage)
        val documents = completion.documents
        if (
            documents.terms.language != completion.effectiveLanguage ||
                documents.privacy.language != completion.effectiveLanguage
        )
            return OnboardingResult.Failure(OnboardingFailure.LegalLanguageMismatch)
        val acceptance = completion.acceptance
        if (
            acceptance.terms.version != documents.terms.version ||
                acceptance.terms.language != documents.terms.language ||
                acceptance.privacy.version != documents.privacy.version ||
                acceptance.privacy.language != documents.privacy.language
        )
            return OnboardingResult.Failure(OnboardingFailure.AcceptanceMismatch)
        return repository.complete(
            ValidatedOnboardingCompletion(
                completion.profile.copy(displayName = displayName),
                completion.language,
                completion.privacy,
                acceptance,
            )
        )
    }
}
