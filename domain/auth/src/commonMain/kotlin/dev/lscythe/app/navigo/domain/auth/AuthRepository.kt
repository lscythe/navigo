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
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.AuthSession
import dev.lscythe.app.navigo.domain.auth.model.EnrollmentEvidence
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import kotlinx.coroutines.flow.Flow

/** Application-facing boundary for attestation and session lifecycle operations. */
interface AuthRepository {
    val session: Flow<AuthSession?>

    suspend fun beginAttestation(
        provider: AuthProvider,
        action: AuthAction,
        packageName: String,
    ): AuthResult<AuthChallenge>

    suspend fun enrollInstallation(
        challengeId: String,
        evidence: EnrollmentEvidence,
    ): AuthResult<String>

    suspend fun createSession(
        challengeId: String,
        evidence: SessionEvidence,
    ): AuthResult<AuthSession>

    suspend fun refreshSession(): AuthResult<AuthSession>

    suspend fun signOut(): AuthResult<Unit>
}
