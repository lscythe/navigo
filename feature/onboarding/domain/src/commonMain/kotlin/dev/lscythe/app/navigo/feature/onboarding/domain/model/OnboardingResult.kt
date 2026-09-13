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
package dev.lscythe.app.navigo.feature.onboarding.domain.model

import dev.lscythe.app.navigo.domain.legal.repository.LegalFailure

sealed interface OnboardingFailure {
    data object InvalidProfile : OnboardingFailure

    data object UnresolvedLanguage : OnboardingFailure

    data object LegalLanguageMismatch : OnboardingFailure

    data object AcceptanceMismatch : OnboardingFailure

    data class Legal(val failure: LegalFailure) : OnboardingFailure

    data class Persistence(val message: String? = null) : OnboardingFailure

    data class Unknown(val message: String? = null) : OnboardingFailure
}

sealed interface OnboardingResult<out T> {
    data class Success<T>(val value: T) : OnboardingResult<T>

    data class Failure(val failure: OnboardingFailure) : OnboardingResult<Nothing>
}
