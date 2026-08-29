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
package dev.lscythe.app.navigo.di

import dev.lscythe.app.navigo.api.auth.PublicAuthApi
import dev.lscythe.app.navigo.api.auth.dto.SessionRefreshRequest
import dev.lscythe.app.navigo.api.auth.dto.SessionResponse
import dev.lscythe.app.navigo.core.datastore.SessionPreference
import dev.lscythe.app.navigo.core.datastore.datasource.SessionPreferenceDataSource
import dev.lscythe.app.navigo.core.datastore.sessionPreference
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.network.SessionManager
import dev.lscythe.app.navigo.core.network.SessionTokens
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.first

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class AppSessionManager(
    private val dataSource: SessionPreferenceDataSource,
    private val publicAuthApi: PublicAuthApi,
) : SessionManager {
    override suspend fun loadTokens(): SessionTokens? =
        dataSource.data.first().toBearerTokensOrNull()

    override suspend fun refreshTokens(): SessionTokens? {
        val current = dataSource.data.first()
        if (current.accessToken.isEmpty() || current.refreshToken.isEmpty()) return null

        return when (
            val response =
                publicAuthApi.refreshSession(
                    SessionRefreshRequest(refreshToken = current.refreshToken)
                )
        ) {
            is ApiResponse.Success -> {
                val session = response.data.toPreference()
                dataSource.setSession(session)
                session.toBearerTokensOrNull()
            }
            is ApiResponse.Error.ClientError -> {
                if (response.code == 401) dataSource.clear()
                null
            }
            is ApiResponse.Error -> null
        }
    }
}

private fun SessionResponse.toPreference(): SessionPreference = sessionPreference {
    id = this@toPreference.id
    accessToken = this@toPreference.accessToken
    refreshToken = this@toPreference.refreshToken
    accessExpiresAtEpochSeconds = this@toPreference.accessExpiresAt.epochSeconds
    refreshExpiresAtEpochSeconds = this@toPreference.refreshExpiresAt.epochSeconds
    installationId = this@toPreference.installationId
}

private fun SessionPreference.toBearerTokensOrNull(): SessionTokens? =
    accessToken.takeIf(String::isNotEmpty)?.let { SessionTokens(it, refreshToken) }
