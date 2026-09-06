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
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSet
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingFailure
import dev.lscythe.app.navigo.feature.onboarding.domain.model.OnboardingResult
import dev.lscythe.app.navigo.feature.onboarding.domain.usecase.LoadOnboardingLegalDocumentsUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val legalDocuments: LegalDocumentSet? = null,
    val legalLoading: Boolean = false,
    val legalFailure: OnboardingFailure? = null,
)

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class OnboardingViewModel(
    private val loadOnboardingLegalDocuments: LoadOnboardingLegalDocumentsUseCase
) : ViewModel() {
    private val mutableState = MutableStateFlow(OnboardingUiState())
    val state: StateFlow<OnboardingUiState> = mutableState.asStateFlow()

    fun loadLegalDocuments(language: AppLanguage) {
        mutableState.value = mutableState.value.copy(legalLoading = true, legalFailure = null)
        viewModelScope.launch {
            mutableState.value =
                when (val result = loadOnboardingLegalDocuments(language)) {
                    is OnboardingResult.Success -> OnboardingUiState(legalDocuments = result.value)
                    is OnboardingResult.Failure -> OnboardingUiState(legalFailure = result.failure)
                }
        }
    }

    fun clearLegalDocuments() {
        mutableState.value = OnboardingUiState()
    }
}
