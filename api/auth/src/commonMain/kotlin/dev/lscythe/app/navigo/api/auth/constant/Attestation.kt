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
package dev.lscythe.app.navigo.api.auth.constant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AttestationProvider {
    @SerialName("play-integrity") PlayIntegrity,
    @SerialName("huawei-sys-integrity") HuaweiSysIntegrity,
    @SerialName("apple-app-attest") AppleAppAttest,
    @SerialName("android-key-attestation") AndroidKeyAttestation,
    @SerialName("development") Development,
}

@Serializable
enum class AttestationAction {
    @SerialName("enroll-installation") EnrollInstallation,
    @SerialName("create-session") CreateSession,
}
