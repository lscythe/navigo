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
package dev.lscythe.app.navigo.domain.auth.model

import kotlin.time.Instant

/** Attestation provider selected for authentication. */
enum class AuthProvider {
    PlayIntegrity,
    HuaweiSysIntegrity,
    AppleAppAttest,
    AndroidKeyAttestation,
    Development,
}

/** Server action authorized by an attestation challenge. */
enum class AuthAction {
    EnrollInstallation,
    CreateSession,
}

/** Short-lived server challenge supplied to a platform evidence provider. */
data class AuthChallenge(
    val id: String,
    val nonce: String,
    val provider: AuthProvider,
    val action: AuthAction,
    val protocolVersion: String,
    val expiresAt: Instant,
)

/** Evidence used to enroll a persistent platform installation key. */
sealed interface EnrollmentEvidence {
    data class AppleAppAttest(val keyId: String, val attestationObject: String) : EnrollmentEvidence

    data class AndroidKeyAttestation(
        val certificateChain: List<String>,
        val publicKeyId: String,
    ) : EnrollmentEvidence
}

/** Exactly one provider-specific proof used to create a session. */
sealed interface SessionEvidence {
    data class PlayIntegrity(val token: String) : SessionEvidence

    data class HuaweiSysIntegrity(val jws: String) : SessionEvidence

    data class AppleAppAttest(
        val enrollmentId: String,
        val clientData: String,
        val assertion: String,
    ) : SessionEvidence

    data class AndroidKeyAttestation(
        val enrollmentId: String,
        val clientData: String,
        val signature: String,
    ) : SessionEvidence

    data class Development(
        val payload: String,
        val signature: String,
        val keyId: String,
    ) : SessionEvidence
}
