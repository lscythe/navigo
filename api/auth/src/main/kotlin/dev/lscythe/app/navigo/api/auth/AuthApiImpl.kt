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
package dev.lscythe.app.navigo.api.auth

import dev.lscythe.app.navigo.api.auth.constant.AuthEndpoint
import dev.lscythe.app.navigo.api.auth.dto.IntegrityChallengeRequest
import dev.lscythe.app.navigo.api.auth.dto.IntegrityChallengeResponse
import dev.lscythe.app.navigo.api.auth.dto.SessionRefreshRequest
import dev.lscythe.app.navigo.api.auth.dto.SessionRefreshResponse
import dev.lscythe.app.navigo.api.auth.dto.SessionRequest
import dev.lscythe.app.navigo.api.auth.dto.SessionResponse
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.network.safeNoContentRequest
import dev.lscythe.app.navigo.core.network.safeRequest
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class AuthApiImpl(private val httpClient: HttpClient) : AuthApi {
    override suspend fun createIntegrityChallenge(
        request: IntegrityChallengeRequest
    ): ApiResponse<IntegrityChallengeResponse> = safeRequest {
        httpClient.post(AuthEndpoint.INTEGRITY_CHALLENGES) { setBody(request) }
    }

    override suspend fun createSession(request: SessionRequest): ApiResponse<SessionResponse> =
        safeRequest {
            httpClient.post(AuthEndpoint.SESSIONS) { setBody(request) }
        }

    override suspend fun refreshSession(
        request: SessionRefreshRequest
    ): ApiResponse<SessionRefreshResponse> = safeRequest {
        httpClient.post(AuthEndpoint.SESSION_REFRESHES) { setBody(request) }
    }

    override suspend fun deleteCurrentSession(): ApiResponse<Unit> = safeNoContentRequest {
        httpClient.delete(AuthEndpoint.CURRENT_SESSION)
    }
}
