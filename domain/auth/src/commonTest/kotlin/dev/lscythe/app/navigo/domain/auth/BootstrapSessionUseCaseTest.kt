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
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlin.time.Instant
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class BootstrapSessionUseCaseTest :
    FunSpec({
        test("reuses persisted session without generating evidence") {
            val existing = session("existing")
            val repository = FakeAuthRepository(existing)
            val provider = FakeEvidenceProvider()

            BootstrapSessionUseCase(repository, provider)("package") shouldBe
                AuthResult.Success(existing)
            repository.operations shouldBe emptyList()
            provider.operations shouldBe emptyList()
        }

        test("creates session from one fresh challenge") {
            val repository = FakeAuthRepository()
            val provider = FakeEvidenceProvider()

            BootstrapSessionUseCase(repository, provider)("package") shouldBe
                AuthResult.Success(session("created"))
            repository.operations.shouldContainExactly(
                "challenge:CreateSession",
                "session:create-1",
            )
            provider.operations.shouldContainExactly("session:create-1")
        }

        test("enrolls installation before creating Android session") {
            val repository = FakeAuthRepository()
            val provider = FakeEvidenceProvider(needsEnrollment = true)

            BootstrapSessionUseCase(repository, provider)("package") shouldBe
                AuthResult.Success(session("created"))
            repository.operations.shouldContainExactly(
                "challenge:EnrollInstallation",
                "enroll:enroll-1",
                "challenge:CreateSession",
                "session:create-1",
            )
            provider.operations.shouldContainExactly(
                "enrollment:enroll-1",
                "stored:enrollment-id",
                "session:create-1",
            )
        }

        test("revoked enrollment clears and re-enrolls once") {
            val repository =
                FakeAuthRepository(
                    sessionResults =
                        ArrayDeque(
                            listOf(
                                AuthResult.Failure(AuthFailure.EnrollmentRevoked()),
                                AuthResult.Success(session("created")),
                            )
                        )
                )
            val provider = FakeEvidenceProvider(needsEnrollment = false)

            BootstrapSessionUseCase(repository, provider)("package") shouldBe
                AuthResult.Success(session("created"))
            provider.operations shouldBe
                listOf(
                    "session:create-1",
                    "revoked",
                    "enrollment:enroll-1",
                    "stored:enrollment-id",
                    "session:create-2",
                )
        }

        test("invalid evidence reconstructs once with a fresh challenge") {
            val repository =
                FakeAuthRepository(
                    sessionResults =
                        ArrayDeque(
                            listOf(
                                AuthResult.Failure(AuthFailure.InvalidEvidence()),
                                AuthResult.Success(session("created")),
                            )
                        )
                )
            val provider = FakeEvidenceProvider()

            BootstrapSessionUseCase(repository, provider)("package") shouldBe
                AuthResult.Success(session("created"))
            repository.operations.shouldContainExactly(
                "challenge:CreateSession",
                "session:create-1",
                "challenge:CreateSession",
                "session:create-2",
            )
        }

        test("does not retry ambiguous transport failure") {
            val failure = AuthResult.Failure(AuthFailure.Network("offline"))
            val repository = FakeAuthRepository(sessionResults = ArrayDeque(listOf(failure)))

            BootstrapSessionUseCase(repository, FakeEvidenceProvider())("package") shouldBe failure
            repository.operations.shouldContainExactly(
                "challenge:CreateSession",
                "session:create-1",
            )
        }

        test("serializes concurrent bootstrap attempts") {
            val repository = FakeAuthRepository(operationDelayMillis = 10)
            val useCase = BootstrapSessionUseCase(repository, FakeEvidenceProvider())

            List(2) { async { useCase("package") } }.awaitAll()

            repository.operations.shouldContainExactly(
                "challenge:CreateSession",
                "session:create-1",
            )
        }
    })

private class FakeEvidenceProvider(private var needsEnrollment: Boolean = false) :
    AuthEvidenceProvider {
    override val provider = AuthProvider.AndroidKeyAttestation
    val operations = mutableListOf<String>()

    override suspend fun needsEnrollment(): Boolean = needsEnrollment

    override suspend fun createEnrollmentEvidence(
        challenge: AuthChallenge
    ): AuthResult<EnrollmentEvidence> {
        operations += "enrollment:${challenge.id}"
        return AuthResult.Success(EnrollmentEvidence.AndroidKeyAttestation(listOf("cert"), "key"))
    }

    override suspend fun enrollmentCreated(enrollmentId: String) {
        operations += "stored:$enrollmentId"
        needsEnrollment = false
    }

    override suspend fun enrollmentRevoked() {
        operations += "revoked"
        needsEnrollment = true
    }

    override suspend fun createSessionEvidence(
        challenge: AuthChallenge
    ): AuthResult<SessionEvidence> {
        operations += "session:${challenge.id}"
        return AuthResult.Success(SessionEvidence.PlayIntegrity("evidence"))
    }
}

private class FakeAuthRepository(
    initialSession: AuthSession? = null,
    private val sessionResults: ArrayDeque<AuthResult<AuthSession>> = ArrayDeque(),
    private val operationDelayMillis: Long = 0,
) : AuthRepository {
    private val persistedSession = MutableStateFlow(initialSession)
    override val session: Flow<AuthSession?> = persistedSession
    val operations = mutableListOf<String>()
    private var createChallengeCount = 0
    private var enrollChallengeCount = 0

    override suspend fun beginAttestation(
        provider: AuthProvider,
        action: AuthAction,
        packageName: String,
    ): AuthResult<AuthChallenge> {
        operations += "challenge:$action"
        if (operationDelayMillis > 0) delay(operationDelayMillis)
        val number =
            when (action) {
                AuthAction.CreateSession -> ++createChallengeCount
                AuthAction.EnrollInstallation -> ++enrollChallengeCount
            }
        val prefix = if (action == AuthAction.CreateSession) "create" else "enroll"
        return AuthResult.Success(challenge("$prefix-$number", action, provider))
    }

    override suspend fun enrollInstallation(
        challengeId: String,
        evidence: EnrollmentEvidence,
    ): AuthResult<String> {
        operations += "enroll:$challengeId"
        return AuthResult.Success("enrollment-id")
    }

    override suspend fun createSession(
        challengeId: String,
        evidence: SessionEvidence,
    ): AuthResult<AuthSession> {
        operations += "session:$challengeId"
        val result = sessionResults.removeFirstOrNull() ?: AuthResult.Success(session("created"))
        if (result is AuthResult.Success) persistedSession.value = result.value
        return result
    }

    override suspend fun refreshSession(): AuthResult<AuthSession> = error("unused")

    override suspend fun signOut(): AuthResult<Unit> = error("unused")
}

private fun challenge(
    id: String,
    action: AuthAction,
    provider: AuthProvider,
) =
    AuthChallenge(
        id = id,
        nonce = "nonce",
        provider = provider,
        action = action,
        protocolVersion = "v2",
        expiresAt = Instant.parse("2026-09-08T12:00:00Z"),
    )

private fun session(id: String) =
    AuthSession(
        id = id,
        accessToken = "access",
        refreshToken = "refresh",
        accessExpiresAt = Instant.parse("2026-09-08T13:00:00Z"),
        refreshExpiresAt = Instant.parse("2026-09-09T12:00:00Z"),
        installationId = "installation",
    )
