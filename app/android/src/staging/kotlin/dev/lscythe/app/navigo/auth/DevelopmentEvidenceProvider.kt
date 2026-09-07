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

import dev.lscythe.app.navigo.data.auth.evidence.developmentPayload
import dev.lscythe.app.navigo.domain.auth.AuthEvidenceProvider
import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.model.AuthChallenge
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.EnrollmentEvidence
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.Instant

private val ED25519_PKCS8_PREFIX =
    byteArrayOf(
        0x30,
        0x2e,
        0x02,
        0x01,
        0x00,
        0x30,
        0x05,
        0x06,
        0x03,
        0x2b,
        0x65,
        0x70,
        0x04,
        0x22,
        0x04,
        0x20,
    )

@OptIn(ExperimentalEncodingApi::class)
class DevelopmentEvidenceProvider(
    private val packageName: String,
    private val signerDigest: String,
    private val keyId: String,
    private val privateKeySeed: String,
    private val evidenceExpiresAt: () -> Instant,
) : AuthEvidenceProvider {
    override val provider = AuthProvider.Development

    override suspend fun needsEnrollment(): Boolean = false

    override suspend fun createEnrollmentEvidence(
        challenge: AuthChallenge
    ): AuthResult<EnrollmentEvidence> = AuthResult.Failure(AuthFailure.UnsupportedProvider())

    override suspend fun enrollmentCreated(enrollmentId: String) = Unit

    override suspend fun enrollmentRevoked() = Unit

    override suspend fun createSessionEvidence(
        challenge: AuthChallenge
    ): AuthResult<SessionEvidence> =
        try {
            val seed =
                Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).decode(privateKeySeed)
            require(seed.size == 32)
            val payload =
                developmentPayload(
                    challenge = challenge,
                    packageName = packageName,
                    signerDigest = signerDigest,
                    expiresAt = evidenceExpiresAt(),
                )
            val privateKey =
                KeyFactory.getInstance("Ed25519")
                    .generatePrivate(PKCS8EncodedKeySpec(ED25519_PKCS8_PREFIX + seed))
            val signature =
                Signature.getInstance("Ed25519").run {
                    initSign(privateKey)
                    update(payload)
                    sign()
                }
            val encoding = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT)
            AuthResult.Success(
                SessionEvidence.Development(
                    payload = encoding.encode(payload),
                    signature = encoding.encode(signature),
                    keyId = keyId,
                )
            )
        } catch (_: IllegalArgumentException) {
            AuthResult.Failure(AuthFailure.InvalidEvidence())
        }
}
