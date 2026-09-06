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

import app.cash.turbine.test
import dev.lscythe.app.navigo.api.auth.PublicAuthApi
import dev.lscythe.app.navigo.api.auth.SessionApi
import dev.lscythe.app.navigo.api.auth.dto.AttestationChallengeRequest
import dev.lscythe.app.navigo.api.auth.dto.AttestationChallengeResponse
import dev.lscythe.app.navigo.api.auth.dto.AttestationEnrollmentRequest
import dev.lscythe.app.navigo.api.auth.dto.AttestationEnrollmentResponse
import dev.lscythe.app.navigo.api.auth.dto.SessionRefreshRequest
import dev.lscythe.app.navigo.api.auth.dto.SessionRequest
import dev.lscythe.app.navigo.api.auth.dto.SessionResponse
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.persistence.SessionPreference
import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.model.AuthAction
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Instant
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultAuthRepositoryTest :
    FunSpec({
        val response =
            SessionResponse(
                "new",
                "new-access",
                "new-refresh",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z"),
                "installation",
            )

        test("challenge delegates once and maps response") {
            val api = FakePublicAuthApi()
            val repository = DefaultAuthRepository(api, FakeSessionApi(), FakeAuthSessionStore())
            val result =
                repository.beginAttestation(
                    AuthProvider.PlayIntegrity,
                    AuthAction.CreateSession,
                    "package",
                )
            api.challengeCalls shouldBe 1
            (result as AuthResult.Success).value.provider shouldBe AuthProvider.PlayIntegrity
        }

        test("createSession persists before success") {
            val api = FakePublicAuthApi(sessionResponse = ApiResponse.Success(response))
            val store = FakeAuthSessionStore()
            val result =
                DefaultAuthRepository(api, FakeSessionApi(), store)
                    .createSession("challenge", SessionEvidence.PlayIntegrity("token"))
            result shouldBe AuthResult.Success(response.toDomain())
            store.value.id shouldBe "new"
        }

        test("session maps empty persistence record to null") {
            DefaultAuthRepository(FakePublicAuthApi(), FakeSessionApi(), FakeAuthSessionStore())
                .session
                .test {
                    awaitItem() shouldBe null
                    cancelAndIgnoreRemainingEvents()
                }
        }

        test("refresh clears invalid credentials") {
            val initial = SessionPreference("old", "access", "refresh", 1, 2, "installation")
            val store = FakeAuthSessionStore(initial)
            val api = FakePublicAuthApi(refreshResponse = ApiResponse.Error.ClientError(403))
            val result = DefaultAuthRepository(api, FakeSessionApi(), store).refreshSession()
            result shouldBe AuthResult.Failure(AuthFailure.Unauthenticated())
            store.value shouldBe SessionPreference()
            api.refreshCalls shouldBe 1
        }

        test("refresh preserves credentials on transient failure") {
            val initial = SessionPreference("old", "access", "refresh", 1, 2, "installation")
            val store = FakeAuthSessionStore(initial)
            val api = FakePublicAuthApi(refreshResponse = ApiResponse.Error.NetworkError("offline"))
            DefaultAuthRepository(api, FakeSessionApi(), store).refreshSession() shouldBe
                AuthResult.Failure(AuthFailure.Network("offline"))
            store.value shouldBe initial
        }

        test("signOut clears credentials after remote failure") {
            val store =
                FakeAuthSessionStore(
                    SessionPreference("old", "access", "refresh", 1, 2, "installation")
                )
            val result =
                DefaultAuthRepository(
                        FakePublicAuthApi(),
                        FakeSessionApi(ApiResponse.Error.ServerError(503)),
                        store,
                    )
                    .signOut()
            result shouldBe AuthResult.Failure(AuthFailure.Server())
            store.value shouldBe SessionPreference()
        }

        test("signOut clears credentials and propagates cancellation") {
            val store =
                FakeAuthSessionStore(
                    SessionPreference("old", "access", "refresh", 1, 2, "installation")
                )
            val repository =
                DefaultAuthRepository(
                    FakePublicAuthApi(),
                    FakeSessionApi(failure = CancellationException()),
                    store,
                )
            io.kotest.assertions.throwables.shouldThrow<CancellationException> {
                repository.signOut()
            }
            store.value shouldBe SessionPreference()
        }
    })

private class FakeAuthSessionStore(initial: SessionPreference = SessionPreference()) :
    AuthSessionStore {
    private val state = MutableStateFlow(initial)
    val value
        get() = state.value

    override val session = state

    override suspend fun save(session: SessionPreference) {
        state.value = session
    }

    override suspend fun clear() {
        state.value = SessionPreference()
    }
}

private class FakeSessionApi(
    private val response: ApiResponse<Unit> = ApiResponse.Success(Unit),
    private val failure: Throwable? = null,
) : SessionApi {
    override suspend fun deleteCurrentSession(): ApiResponse<Unit> =
        failure?.let { throw it } ?: response
}

private class FakePublicAuthApi(
    private val sessionResponse: ApiResponse<SessionResponse> = ApiResponse.Error.UnknownError(),
    private val refreshResponse: ApiResponse<SessionResponse> = ApiResponse.Error.UnknownError(),
) : PublicAuthApi {
    var challengeCalls = 0
    var refreshCalls = 0

    override suspend fun createAttestationChallenge(
        request: AttestationChallengeRequest
    ): ApiResponse<AttestationChallengeResponse> {
        challengeCalls++
        return ApiResponse.Success(
            AttestationChallengeResponse(
                "id",
                "nonce",
                request.provider,
                request.action,
                "v2",
                Instant.parse("2026-01-01T00:00:00Z"),
            )
        )
    }

    override suspend fun createAttestationEnrollment(
        request: AttestationEnrollmentRequest
    ): ApiResponse<AttestationEnrollmentResponse> =
        ApiResponse.Success(AttestationEnrollmentResponse("enrollment"))

    override suspend fun createSession(request: SessionRequest) = sessionResponse

    override suspend fun refreshSession(
        request: SessionRefreshRequest
    ): ApiResponse<SessionResponse> {
        refreshCalls++
        return refreshResponse
    }
}
