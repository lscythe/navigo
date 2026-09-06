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

import dev.lscythe.app.navigo.core.network.SessionManager
import dev.lscythe.app.navigo.core.network.SessionTokens
import dev.lscythe.app.navigo.domain.auth.AuthRepository
import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.model.AuthSession
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.first

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class AuthSessionManager(private val authRepository: AuthRepository) : SessionManager {
    override suspend fun loadTokens(): SessionTokens? = authRepository.session.first()?.toTokens()

    override suspend fun refreshTokens(): SessionTokens? =
        when (val result = authRepository.refreshSession()) {
            is AuthResult.Success -> result.value.toTokens()
            is AuthResult.Failure -> null
        }
}

private fun AuthSession.toTokens() = SessionTokens(accessToken, refreshToken)
