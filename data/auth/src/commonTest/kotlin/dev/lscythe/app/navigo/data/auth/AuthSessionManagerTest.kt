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
package dev.lscythe.app.navigo.data.auth

import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.AuthRepository
import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.model.AuthAction
import dev.lscythe.app.navigo.domain.auth.model.AuthChallenge
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.AuthSession
import dev.lscythe.app.navigo.domain.auth.model.EnrollmentEvidence
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AuthSessionManagerTest :
    FunSpec({
        val session =
            AuthSession(
                "id",
                "access",
                "refresh",
                Instant.fromEpochSeconds(1),
                Instant.fromEpochSeconds(2),
                "installation",
            )
        test("loads persisted bearer tokens") {
            AuthSessionManager(FakeRepository(flowOf(session))).loadTokens()?.accessToken shouldBe
                "access"
        }
        test("maps missing session to missing tokens") {
            AuthSessionManager(FakeRepository(flowOf(null))).loadTokens() shouldBe null
        }
        test("returns refreshed tokens only after repository success") {
            AuthSessionManager(FakeRepository(flowOf(null), AuthResult.Success(session)))
                .refreshTokens()
                ?.refreshToken shouldBe "refresh"
            AuthSessionManager(
                    FakeRepository(flowOf(null), AuthResult.Failure(AuthFailure.Network()))
                )
                .refreshTokens() shouldBe null
        }
    })

private class FakeRepository(
    override val session: Flow<AuthSession?>,
    private val refreshResult: AuthResult<AuthSession> = AuthResult.Failure(AuthFailure.Unknown()),
) : AuthRepository {
    override suspend fun beginAttestation(
        provider: AuthProvider,
        action: AuthAction,
        packageName: String,
    ): AuthResult<AuthChallenge> = error("unused")

    override suspend fun enrollInstallation(
        challengeId: String,
        evidence: EnrollmentEvidence,
    ): AuthResult<String> = error("unused")

    override suspend fun createSession(
        challengeId: String,
        evidence: SessionEvidence,
    ): AuthResult<AuthSession> = error("unused")

    override suspend fun refreshSession() = refreshResult

    override suspend fun signOut(): AuthResult<Unit> = error("unused")
}
