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
import dev.lscythe.app.navigo.core.persistence.datasource.SessionPreferenceDataSource
import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.model.AuthAction
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import eu.anifantakis.lib.ksafe.KSafe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Instant
import kotlinx.coroutines.CancellationException

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
            withSessionDataSource("challenge") { source ->
                val api = FakePublicAuthApi()
                val result =
                    DefaultAuthRepository(api, { FakeSessionApi() }, source)
                        .beginAttestation(
                            AuthProvider.PlayIntegrity,
                            AuthAction.CreateSession,
                            "package",
                        )
                api.challengeCalls shouldBe 1
                (result as AuthResult.Success).value.provider shouldBe AuthProvider.PlayIntegrity
            }
        }

        test("createSession persists before success") {
            withSessionDataSource("create") { source ->
                val api = FakePublicAuthApi(sessionResponse = ApiResponse.Success(response))
                val result =
                    DefaultAuthRepository(api, { FakeSessionApi() }, source)
                        .createSession("challenge", SessionEvidence.PlayIntegrity("token"))
                result shouldBe AuthResult.Success(response.toDomain())
                source.data.test {
                    awaitItem().id shouldBe "new"
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        test("session maps empty persistence record to null") {
            withSessionDataSource("empty") { source ->
                DefaultAuthRepository(FakePublicAuthApi(), { FakeSessionApi() }, source)
                    .session
                    .test {
                        awaitItem() shouldBe null
                        cancelAndIgnoreRemainingEvents()
                    }
            }
        }

        test("refresh clears invalid credentials") {
            val initial = SessionPreference("old", "access", "refresh", 1, 2, "installation")
            withSessionDataSource("invalid", initial) { source ->
                val api = FakePublicAuthApi(refreshResponse = ApiResponse.Error.ClientError(403))
                DefaultAuthRepository(api, { FakeSessionApi() }, source).refreshSession() shouldBe
                    AuthResult.Failure(AuthFailure.Unauthenticated())
                source.data.test {
                    awaitItem() shouldBe SessionPreference()
                    cancelAndIgnoreRemainingEvents()
                }
                api.refreshCalls shouldBe 1
            }
        }

        test("refresh preserves credentials on transient failure") {
            val initial = SessionPreference("old", "access", "refresh", 1, 2, "installation")
            withSessionDataSource("transient", initial) { source ->
                val api =
                    FakePublicAuthApi(refreshResponse = ApiResponse.Error.NetworkError("offline"))
                DefaultAuthRepository(api, { FakeSessionApi() }, source).refreshSession() shouldBe
                    AuthResult.Failure(AuthFailure.Network("offline"))
                source.data.test {
                    awaitItem() shouldBe initial
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        test("signOut clears credentials after remote failure") {
            val initial = SessionPreference("old", "access", "refresh", 1, 2, "installation")
            withSessionDataSource("signout_failure", initial) { source ->
                DefaultAuthRepository(
                        FakePublicAuthApi(),
                        { FakeSessionApi(ApiResponse.Error.ServerError(503)) },
                        source,
                    )
                    .signOut() shouldBe AuthResult.Failure(AuthFailure.Server())
                source.data.test {
                    awaitItem() shouldBe SessionPreference()
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        test("signOut clears credentials and propagates cancellation") {
            val initial = SessionPreference("old", "access", "refresh", 1, 2, "installation")
            withSessionDataSource("signout_cancel", initial) { source ->
                val repository =
                    DefaultAuthRepository(
                        FakePublicAuthApi(),
                        { FakeSessionApi(failure = CancellationException()) },
                        source,
                    )
                shouldThrow<CancellationException> { repository.signOut() }
                source.data.test {
                    awaitItem() shouldBe SessionPreference()
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    })

private suspend fun withSessionDataSource(
    suffix: String,
    initial: SessionPreference = SessionPreference(),
    block: suspend (SessionPreferenceDataSource) -> Unit,
) {
    val ksafe = KSafe(fileName = "navigo_test_auth_repository_$suffix")
    try {
        val source = SessionPreferenceDataSource(ksafe)
        if (initial != SessionPreference()) source.setSession(initial)
        block(source)
    } finally {
        ksafe.clearAll()
        ksafe.close()
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
