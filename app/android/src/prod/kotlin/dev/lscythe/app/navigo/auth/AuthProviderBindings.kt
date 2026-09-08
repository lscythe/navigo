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
package dev.lscythe.app.navigo.auth

import dev.lscythe.app.navigo.domain.auth.AuthEvidenceProvider
import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.model.AuthChallenge
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.EnrollmentEvidence
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
@BindingContainer
object AuthProviderBindings {
    @Provides fun provideAuthEvidenceProvider(): AuthEvidenceProvider = UnsupportedProvider
}

private object UnsupportedProvider : AuthEvidenceProvider {
    override val provider = AuthProvider.AndroidKeyAttestation

    override suspend fun needsEnrollment() = false

    override suspend fun createEnrollmentEvidence(
        challenge: AuthChallenge
    ): AuthResult<EnrollmentEvidence> = AuthResult.Failure(AuthFailure.UnsupportedProvider())

    override suspend fun enrollmentCreated(enrollmentId: String) = Unit

    override suspend fun enrollmentRevoked() = Unit

    override suspend fun createSessionEvidence(
        challenge: AuthChallenge
    ): AuthResult<SessionEvidence> = AuthResult.Failure(AuthFailure.UnsupportedProvider())
}
