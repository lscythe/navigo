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
import dev.lscythe.app.navigo.api.auth.dto.AttestationChallengeResponse
import dev.lscythe.app.navigo.api.auth.dto.SessionResponse
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.persistence.SessionPreference
import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.model.AuthAction
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.EnrollmentEvidence
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Instant

class AuthMappersTest :
    FunSpec({
        test("maps every provider and action explicitly") {
            AuthProvider.entries.map { it.toDto() } shouldBe AttestationProvider.entries
            AuthAction.entries.map { it.toDto() } shouldBe AttestationAction.entries
        }

        test("maps challenge and session responses") {
            AttestationChallengeResponse(
                    "id",
                    "nonce",
                    AttestationProvider.PlayIntegrity,
                    AttestationAction.CreateSession,
                    "v2",
                    Instant.parse("2026-01-01T00:00:00Z"),
                )
                .toDomain()
                .provider shouldBe AuthProvider.PlayIntegrity
            SessionResponse(
                    "session",
                    "access",
                    "refresh",
                    Instant.parse("2026-01-01T00:00:00Z"),
                    Instant.parse("2026-01-02T00:00:00Z"),
                    "installation",
                )
                .toPreference()
                .id shouldBe "session"
        }

        test("maps empty preference to no session") {
            SessionPreference().toDomainOrNull() shouldBe null
        }

        test("maps every evidence variant to one wire branch") {
            EnrollmentEvidence.AppleAppAttest("key", "object")
                .toDto("challenge")
                .appleAppAttest
                ?.keyId shouldBe "key"
            EnrollmentEvidence.AndroidKeyAttestation(listOf("cert"), "key")
                .toDto("challenge")
                .androidKeyAttestation
                ?.publicKeyId shouldBe "key"
            SessionEvidence.PlayIntegrity("token").toDto("challenge").playIntegrity?.token shouldBe
                "token"
            SessionEvidence.HuaweiSysIntegrity("jws")
                .toDto("challenge")
                .huaweiSysIntegrity
                ?.jws shouldBe "jws"
            SessionEvidence.AppleAppAttest("enrollment", "data", "assertion")
                .toDto("challenge")
                .appleAppAttest
                ?.assertion shouldBe "assertion"
            SessionEvidence.AndroidKeyAttestation("enrollment", "data", "signature")
                .toDto("challenge")
                .androidKeyAttestation
                ?.signature shouldBe "signature"
            SessionEvidence.Development("payload", "signature", "key")
                .toDto("challenge")
                .development
                ?.keyId shouldBe "key"
        }

        test("maps transport failures without localized text classification") {
            ApiResponse.Error.ClientError(401, message = "localized").toAuthFailure() shouldBe
                AuthFailure.Unauthenticated("localized")
            ApiResponse.Error.ClientError(422, message = "invalid").toAuthFailure() shouldBe
                AuthFailure.InvalidEvidence("invalid")
            ApiResponse.Error.NetworkError("offline").toAuthFailure() shouldBe
                AuthFailure.Network("offline")
            ApiResponse.Error.ServerError(503, message = "down").toAuthFailure() shouldBe
                AuthFailure.Server("down")
            ApiResponse.Error.SerializationError("bad").toAuthFailure() shouldBe
                AuthFailure.Serialization("bad")
        }
    })
