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
package dev.lscythe.app.navigo.api.auth.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class AppleEnrollmentEvidenceRequest(
    val keyId: String,
    val attestationObject: String,
)

@Serializable
data class AndroidEnrollmentEvidenceRequest(
    val certificateChain: List<String>,
    val publicKeyId: String,
)

@Serializable
data class AttestationEnrollmentRequest(
    val challengeId: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val appleAppAttest: AppleEnrollmentEvidenceRequest? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val androidKeyAttestation: AndroidEnrollmentEvidenceRequest? = null,
) {
    init {
        require((appleAppAttest == null) xor (androidKeyAttestation == null)) {
            "Exactly one enrollment evidence type is required"
        }
    }
}

@Serializable data class AttestationEnrollmentResponse(val id: String)
