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
package dev.lscythe.app.navigo.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.BootstrapSessionUseCase
import dev.lscythe.app.navigo.domain.auth.model.AuthSession
import dev.lscythe.app.navigo.domain.settings.repository.SettingsRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

private val STARTUP_TIMEOUT = 10.seconds

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class MainViewModel(
    settingsRepository: SettingsRepository,
    bootstrapSession: BootstrapSessionUseCase,
    @AttestationPackageName attestationPackageName: String,
) : ViewModel() {
    private val coordinator =
        StartupCoordinator(
            scope = viewModelScope,
            loadCompletion = { settingsRepository.settings.first().hasCompletedOnboarding },
            bootstrap = { bootstrapSession(packageName = attestationPackageName) },
        )

    val state: StateFlow<StartupState> = coordinator.state

    fun retryAuthentication() = coordinator.retryAuthentication()
}

internal class StartupCoordinator(
    private val scope: CoroutineScope,
    private val loadCompletion: suspend () -> Boolean,
    private val bootstrap: suspend () -> AuthResult<AuthSession>,
) {
    constructor(
        hasCompletedOnboarding: Boolean,
        bootstrap: suspend () -> AuthResult<AuthSession>,
        scope: CoroutineScope,
    ) : this(scope, { hasCompletedOnboarding }, bootstrap)

    private val mutableState = MutableStateFlow<StartupState>(StartupState.Initializing)
    val state: StateFlow<StartupState> = mutableState.asStateFlow()
    private var attempt = 0L
    private var attemptJob: Job? = null

    init {
        startAuthentication()
    }

    fun retryAuthentication() {
        startAuthentication()
    }

    private fun startAuthentication() {
        val currentAttempt = ++attempt
        attemptJob?.cancel()
        mutableState.value = StartupState.Initializing
        attemptJob = scope.launch {
            val hasCompletedOnboarding = loadCompletion()
            val result = withTimeoutOrNull(STARTUP_TIMEOUT) { bootstrap() }
            if (currentAttempt != attempt) return@launch
            val destination =
                if (hasCompletedOnboarding) StartupDestination.Home
                else StartupDestination.Onboarding
            mutableState.value =
                when (result) {
                    is AuthResult.Success -> StartupState.Ready(destination)
                    is AuthResult.Failure -> StartupState.AuthRequired(destination, result.failure)
                    null -> StartupState.AuthRequired(destination, AuthFailure.Network())
                }
        }
    }
}
