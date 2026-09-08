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

import dev.lscythe.app.navigo.core.persistence.Language
import dev.lscythe.app.navigo.core.persistence.LegalAcceptancePreference
import dev.lscythe.app.navigo.core.persistence.OnboardingPreferenceCompletion
import dev.lscythe.app.navigo.core.persistence.datasource.NavigoPreferenceDataSource
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingFailure
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingResult
import dev.lscythe.app.navigo.feature.onboarding.domain.model.ValidatedOnboardingCompletion
import dev.lscythe.app.navigo.feature.onboarding.domain.repository.OnboardingCompletionRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlin.coroutines.cancellation.CancellationException

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DefaultOnboardingCompletionRepository(private val source: NavigoPreferenceDataSource) :
    OnboardingCompletionRepository {
    override suspend fun complete(
        completion: ValidatedOnboardingCompletion
    ): OnboardingResult<Unit> =
        try {
            source.completeOnboarding(completion.toPreference())
            OnboardingResult.Success(Unit)
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            OnboardingResult.Failure(OnboardingFailure.Persistence(error.message))
        }
}

private fun ValidatedOnboardingCompletion.toPreference() =
    OnboardingPreferenceCompletion(
        displayName = profile.displayName,
        avatarColorArgb = profile.avatarColorArgb,
        language = language.toPreference(),
        analyticsEnabled = privacy.analyticsEnabled,
        crashReportsEnabled = privacy.crashReportsEnabled,
        acceptedTerms =
            LegalAcceptancePreference(
                acceptance.terms.version,
                acceptance.terms.language.toPreference(),
            ),
        acceptedPrivacy =
            LegalAcceptancePreference(
                acceptance.privacy.version,
                acceptance.privacy.language.toPreference(),
            ),
    )

private fun AppLanguage.toPreference() =
    when (this) {
        AppLanguage.System -> Language.System
        AppLanguage.English -> Language.English
        AppLanguage.Indonesian -> Language.Indonesian
    }
