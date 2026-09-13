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

import dev.lscythe.app.navigo.api.auth.constant.AttestationAction
import dev.lscythe.app.navigo.api.auth.constant.AttestationProvider
import dev.lscythe.app.navigo.api.auth.dto.AndroidAssertionEvidenceRequest
import dev.lscythe.app.navigo.api.auth.dto.AndroidEnrollmentEvidenceRequest
import dev.lscythe.app.navigo.api.auth.dto.AppleAssertionEvidenceRequest
import dev.lscythe.app.navigo.api.auth.dto.AppleEnrollmentEvidenceRequest
import dev.lscythe.app.navigo.api.auth.dto.AttestationChallengeResponse
import dev.lscythe.app.navigo.api.auth.dto.AttestationEnrollmentRequest
import dev.lscythe.app.navigo.api.auth.dto.DevelopmentEvidenceRequest
import dev.lscythe.app.navigo.api.auth.dto.HuaweiSysIntegrityEvidenceRequest
import dev.lscythe.app.navigo.api.auth.dto.PlayIntegrityRequest
import dev.lscythe.app.navigo.api.auth.dto.SessionRequest
import dev.lscythe.app.navigo.api.auth.dto.SessionResponse
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.persistence.SessionPreference
import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.model.AuthAction
import dev.lscythe.app.navigo.domain.auth.model.AuthChallenge
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.AuthSession
import dev.lscythe.app.navigo.domain.auth.model.EnrollmentEvidence
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import kotlin.time.Instant

internal fun AuthProvider.toDto(): AttestationProvider =
    when (this) {
        AuthProvider.PlayIntegrity -> AttestationProvider.PlayIntegrity
        AuthProvider.HuaweiSysIntegrity -> AttestationProvider.HuaweiSysIntegrity
        AuthProvider.AppleAppAttest -> AttestationProvider.AppleAppAttest
        AuthProvider.AndroidKeyAttestation -> AttestationProvider.AndroidKeyAttestation
        AuthProvider.Development -> AttestationProvider.Development
    }

internal fun AttestationProvider.toDomain(): AuthProvider =
    when (this) {
        AttestationProvider.PlayIntegrity -> AuthProvider.PlayIntegrity
        AttestationProvider.HuaweiSysIntegrity -> AuthProvider.HuaweiSysIntegrity
        AttestationProvider.AppleAppAttest -> AuthProvider.AppleAppAttest
        AttestationProvider.AndroidKeyAttestation -> AuthProvider.AndroidKeyAttestation
        AttestationProvider.Development -> AuthProvider.Development
    }

internal fun AuthAction.toDto(): AttestationAction =
    when (this) {
        AuthAction.EnrollInstallation -> AttestationAction.EnrollInstallation
        AuthAction.CreateSession -> AttestationAction.CreateSession
    }

internal fun AttestationAction.toDomain(): AuthAction =
    when (this) {
        AttestationAction.EnrollInstallation -> AuthAction.EnrollInstallation
        AttestationAction.CreateSession -> AuthAction.CreateSession
    }

internal fun AttestationChallengeResponse.toDomain() =
    AuthChallenge(id, nonce, provider.toDomain(), action.toDomain(), protocolVersion, expiresAt)

internal fun EnrollmentEvidence.toDto(challengeId: String) =
    when (this) {
        is EnrollmentEvidence.AppleAppAttest ->
            AttestationEnrollmentRequest(
                challengeId,
                appleAppAttest = AppleEnrollmentEvidenceRequest(keyId, attestationObject),
            )
        is EnrollmentEvidence.AndroidKeyAttestation ->
            AttestationEnrollmentRequest(
                challengeId,
                androidKeyAttestation =
                    AndroidEnrollmentEvidenceRequest(certificateChain, publicKeyId),
            )
    }

internal fun SessionEvidence.toDto(challengeId: String) =
    when (this) {
        is SessionEvidence.PlayIntegrity ->
            SessionRequest(challengeId, playIntegrity = PlayIntegrityRequest(token))
        is SessionEvidence.HuaweiSysIntegrity ->
            SessionRequest(challengeId, huaweiSysIntegrity = HuaweiSysIntegrityEvidenceRequest(jws))
        is SessionEvidence.AppleAppAttest ->
            SessionRequest(
                challengeId,
                appleAppAttest = AppleAssertionEvidenceRequest(enrollmentId, clientData, assertion),
            )
        is SessionEvidence.AndroidKeyAttestation ->
            SessionRequest(
                challengeId,
                androidKeyAttestation =
                    AndroidAssertionEvidenceRequest(enrollmentId, clientData, signature),
            )
        is SessionEvidence.Development ->
            SessionRequest(
                challengeId,
                development = DevelopmentEvidenceRequest(payload, signature, keyId),
            )
    }

internal fun SessionResponse.toDomain() =
    AuthSession(id, accessToken, refreshToken, accessExpiresAt, refreshExpiresAt, installationId)

internal fun SessionResponse.toPreference() =
    SessionPreference(
        id,
        accessToken,
        refreshToken,
        accessExpiresAt.epochSeconds,
        refreshExpiresAt.epochSeconds,
        installationId,
    )

internal fun SessionPreference.toDomainOrNull(): AuthSession? =
    if (accessToken.isEmpty() || refreshToken.isEmpty()) null
    else
        AuthSession(
            id,
            accessToken,
            refreshToken,
            Instant.fromEpochSeconds(accessExpiresAtEpochSeconds),
            Instant.fromEpochSeconds(refreshExpiresAtEpochSeconds),
            installationId,
        )

internal fun ApiResponse.Error.toAuthFailure(): AuthFailure =
    when (this) {
        is ApiResponse.Error.ClientError ->
            when (problem?.type?.substringAfterLast('/')) {
                "attestation-provider-unsupported" -> AuthFailure.UnsupportedProvider(message)
                "attestation-evidence-invalid" -> AuthFailure.InvalidEvidence(message)
                "attestation-challenge-replayed" -> AuthFailure.ChallengeReplayed(message)
                "attestation-enrollment-revoked" -> AuthFailure.EnrollmentRevoked(message)
                "attestation-counter-replayed" -> AuthFailure.CounterReplayed(message)
                else ->
                    when (code) {
                        401,
                        403 -> AuthFailure.Unauthenticated(message)
                        400,
                        409,
                        422 -> AuthFailure.InvalidEvidence(message)
                        else -> AuthFailure.Unknown(message)
                    }
            }
        is ApiResponse.Error.NetworkError -> AuthFailure.Network(message)
        is ApiResponse.Error.ServerError -> AuthFailure.Server(message)
        is ApiResponse.Error.SerializationError -> AuthFailure.Serialization(message)
        is ApiResponse.Error.GraphQLError -> AuthFailure.Unknown(message)
        is ApiResponse.Error.UnknownError -> AuthFailure.Unknown(message)
    }
