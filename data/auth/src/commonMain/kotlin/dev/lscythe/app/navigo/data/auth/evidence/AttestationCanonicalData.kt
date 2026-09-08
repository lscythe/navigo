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
package dev.lscythe.app.navigo.data.auth.evidence

import dev.lscythe.app.navigo.domain.auth.model.AuthAction
import dev.lscythe.app.navigo.domain.auth.model.AuthChallenge
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val canonicalJson = Json { encodeDefaults = true }

fun developmentPayload(
    challenge: AuthChallenge,
    packageName: String,
    signerDigest: String,
    expiresAt: Instant,
): ByteArray =
    canonicalJson
        .encodeToString(
            DevelopmentPayload(
                challenge =
                    DevelopmentChallenge(
                        id = challenge.id,
                        nonce = challenge.nonce,
                        requestHash = "",
                        packageName = packageName,
                        signerDigest = signerDigest,
                        provider = challenge.provider.wireValue,
                        action = challenge.action.wireValue,
                        expiresAt = challenge.expiresAt.toString(),
                    ),
                expiresAt = expiresAt.toString(),
            )
        )
        .encodeToByteArray()

fun androidAssertionData(
    challenge: AuthChallenge,
    enrollmentId: String,
): ByteArray =
    canonicalJson
        .encodeToString(
            AndroidAssertion(
                protocolVersion = challenge.protocolVersion,
                provider = challenge.provider.wireValue,
                action = challenge.action.wireValue,
                challengeId = challenge.id,
                nonce = challenge.nonce,
                enrollmentId = enrollmentId,
                expiresAt = challenge.expiresAt.toString(),
            )
        )
        .encodeToByteArray()

private val AuthProvider.wireValue: String
    get() =
        when (this) {
            AuthProvider.PlayIntegrity -> "play-integrity"
            AuthProvider.HuaweiSysIntegrity -> "huawei-sysintegrity"
            AuthProvider.AppleAppAttest -> "apple-app-attest"
            AuthProvider.AndroidKeyAttestation -> "android-key-attestation"
            AuthProvider.Development -> "development"
        }

private val AuthAction.wireValue: String
    get() =
        when (this) {
            AuthAction.EnrollInstallation -> "enroll-installation"
            AuthAction.CreateSession -> "create-session"
        }

@Serializable
private data class DevelopmentPayload(
    val challenge: DevelopmentChallenge,
    @SerialName("expires_at") val expiresAt: String,
)

@Serializable
private data class DevelopmentChallenge(
    @SerialName("ID") val id: String,
    @SerialName("Nonce") val nonce: String,
    @SerialName("RequestHash") val requestHash: String,
    @SerialName("PackageName") val packageName: String,
    @SerialName("SignerDigest") val signerDigest: String,
    @SerialName("Provider") val provider: String,
    @SerialName("Action") val action: String,
    @SerialName("ExpiresAt") val expiresAt: String,
)

@Serializable
private data class AndroidAssertion(
    val protocolVersion: String,
    val provider: String,
    val action: String,
    val challengeId: String,
    val nonce: String,
    val enrollmentId: String,
    val expiresAt: String,
)
