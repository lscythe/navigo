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

import dev.lscythe.app.navigo.api.auth.PublicAuthApi
import dev.lscythe.app.navigo.api.auth.SessionApi
import dev.lscythe.app.navigo.api.auth.dto.AttestationChallengeRequest
import dev.lscythe.app.navigo.api.auth.dto.SessionRefreshRequest
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.persistence.SessionPreference
import dev.lscythe.app.navigo.core.persistence.datasource.SessionPreferenceDataSource
import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.AuthRepository
import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.model.AuthAction
import dev.lscythe.app.navigo.domain.auth.model.AuthChallenge
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.AuthSession
import dev.lscythe.app.navigo.domain.auth.model.EnrollmentEvidence
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface AuthSessionStore {
    val session: Flow<SessionPreference>

    suspend fun save(session: SessionPreference)

    suspend fun clear()
}

@Inject
@ContributesBinding(AppScope::class)
class PersistentAuthSessionStore(private val dataSource: SessionPreferenceDataSource) :
    AuthSessionStore {
    override val session = dataSource.data

    override suspend fun save(session: SessionPreference) = dataSource.setSession(session)

    override suspend fun clear() = dataSource.clear()
}

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DefaultAuthRepository(
    private val publicAuthApi: PublicAuthApi,
    private val sessionApi: SessionApi,
    private val sessionStore: AuthSessionStore,
) : AuthRepository {
    override val session: Flow<AuthSession?> =
        sessionStore.session.map(SessionPreference::toDomainOrNull)

    override suspend fun beginAttestation(
        provider: AuthProvider,
        action: AuthAction,
        packageName: String,
    ): AuthResult<AuthChallenge> =
        publicAuthApi
            .createAttestationChallenge(
                AttestationChallengeRequest(provider.toDto(), action.toDto(), packageName)
            )
            .mapSuccess { it.toDomain() }

    override suspend fun enrollInstallation(
        challengeId: String,
        evidence: EnrollmentEvidence,
    ): AuthResult<String> =
        publicAuthApi.createAttestationEnrollment(evidence.toDto(challengeId)).mapSuccess { it.id }

    override suspend fun createSession(
        challengeId: String,
        evidence: SessionEvidence,
    ): AuthResult<AuthSession> =
        when (val response = publicAuthApi.createSession(evidence.toDto(challengeId))) {
            is ApiResponse.Success -> persist(response.data)
            is ApiResponse.Error -> AuthResult.Failure(response.toAuthFailure())
        }

    override suspend fun refreshSession(): AuthResult<AuthSession> {
        val refreshToken = sessionStore.session.first().refreshToken
        if (refreshToken.isEmpty()) return AuthResult.Failure(AuthFailure.Unauthenticated())
        return when (
            val response = publicAuthApi.refreshSession(SessionRefreshRequest(refreshToken))
        ) {
            is ApiResponse.Success -> persist(response.data)
            is ApiResponse.Error.ClientError -> {
                val failure = response.toAuthFailure()
                if (failure is AuthFailure.Unauthenticated) sessionStore.clear()
                AuthResult.Failure(failure)
            }
            is ApiResponse.Error -> AuthResult.Failure(response.toAuthFailure())
        }
    }

    override suspend fun signOut(): AuthResult<Unit> {
        var result: AuthResult<Unit> = AuthResult.Success(Unit)
        try {
            result =
                when (val response = sessionApi.deleteCurrentSession()) {
                    is ApiResponse.Success -> AuthResult.Success(Unit)
                    is ApiResponse.Error -> AuthResult.Failure(response.toAuthFailure())
                }
        } finally {
            withContext(NonCancellable) { sessionStore.clear() }
        }
        return result
    }

    private suspend fun persist(
        response: dev.lscythe.app.navigo.api.auth.dto.SessionResponse
    ): AuthResult<AuthSession> =
        try {
            sessionStore.save(response.toPreference())
            AuthResult.Success(response.toDomain())
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            AuthResult.Failure(AuthFailure.Unknown(error.message))
        }
}

private inline fun <T, R> ApiResponse<T>.mapSuccess(transform: (T) -> R): AuthResult<R> =
    when (this) {
        is ApiResponse.Success -> AuthResult.Success(transform(data))
        is ApiResponse.Error -> AuthResult.Failure(toAuthFailure())
    }
