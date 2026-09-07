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
package dev.lscythe.app.navigo.domain.auth

import dev.lscythe.app.navigo.domain.auth.model.AuthAction
import dev.lscythe.app.navigo.domain.auth.model.AuthChallenge
import dev.lscythe.app.navigo.domain.auth.model.AuthSession
import dev.lscythe.app.navigo.domain.auth.model.EnrollmentEvidence
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class BootstrapSessionUseCase(
    private val repository: AuthRepository,
    private val evidenceProvider: AuthEvidenceProvider,
) {
    private val bootstrapMutex = Mutex()

    suspend operator fun invoke(packageName: String): AuthResult<AuthSession> =
        bootstrapMutex.withLock {
            repository.session.first()?.let {
                return@withLock AuthResult.Success(it)
            }

            if (evidenceProvider.needsEnrollment()) {
                when (val enrollment = enroll(packageName)) {
                    is AuthResult.Failure -> return@withLock enrollment
                    is AuthResult.Success -> Unit
                }
            }

            when (val firstAttempt = createSession(packageName)) {
                is AuthResult.Success -> firstAttempt
                is AuthResult.Failure ->
                    when (firstAttempt.failure) {
                        is AuthFailure.EnrollmentRevoked -> {
                            evidenceProvider.enrollmentRevoked()
                            when (val enrollment = enroll(packageName)) {
                                is AuthResult.Failure -> enrollment
                                is AuthResult.Success -> createSession(packageName)
                            }
                        }
                        is AuthFailure.InvalidEvidence -> createSession(packageName)
                        else -> firstAttempt
                    }
            }
        }

    private suspend fun enroll(packageName: String): AuthResult<Unit> {
        val challenge = begin(AuthAction.EnrollInstallation, packageName)
        if (challenge is AuthResult.Failure) return challenge
        challenge as AuthResult.Success

        val evidence = evidenceProvider.createEnrollmentEvidence(challenge.value)
        if (evidence is AuthResult.Failure) return evidence
        evidence as AuthResult.Success<EnrollmentEvidence>

        return when (
            val result = repository.enrollInstallation(challenge.value.id, evidence.value)
        ) {
            is AuthResult.Failure -> result
            is AuthResult.Success -> {
                evidenceProvider.enrollmentCreated(result.value)
                AuthResult.Success(Unit)
            }
        }
    }

    private suspend fun createSession(packageName: String): AuthResult<AuthSession> {
        val challenge = begin(AuthAction.CreateSession, packageName)
        if (challenge is AuthResult.Failure) return challenge
        challenge as AuthResult.Success<AuthChallenge>

        val evidence = evidenceProvider.createSessionEvidence(challenge.value)
        if (evidence is AuthResult.Failure) return evidence
        evidence as AuthResult.Success<SessionEvidence>

        return repository.createSession(challenge.value.id, evidence.value)
    }

    private suspend fun begin(action: AuthAction, packageName: String): AuthResult<AuthChallenge> =
        repository.beginAttestation(evidenceProvider.provider, action, packageName)
}
