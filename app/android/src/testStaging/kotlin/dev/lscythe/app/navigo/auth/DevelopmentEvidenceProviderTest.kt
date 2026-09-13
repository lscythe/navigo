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

import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.model.AuthAction
import dev.lscythe.app.navigo.domain.auth.model.AuthChallenge
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Instant

class DevelopmentEvidenceProviderTest :
    FunSpec({
        test("signs backend development vector exactly") {
            val provider =
                DevelopmentEvidenceProvider(
                    packageName = "dev.lscythe.app.navigo.staging",
                    signerDigest = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                    keyId = "development-vector-key",
                    privateKeySeed = "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8",
                    evidenceExpiresAt = { Instant.parse("2026-09-08T12:01:00Z") },
                )

            provider.createSessionEvidence(
                AuthChallenge(
                    id = "challenge-development-001",
                    nonce = "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8",
                    provider = AuthProvider.Development,
                    action = AuthAction.CreateSession,
                    protocolVersion = "v2",
                    expiresAt = Instant.parse("2026-09-08T12:02:00Z"),
                )
            ) shouldBe
                AuthResult.Success(
                    SessionEvidence.Development(
                        payload =
                            "eyJjaGFsbGVuZ2UiOnsiSUQiOiJjaGFsbGVuZ2UtZGV2ZWxvcG1lbnQtMDAxIiwiTm9uY2UiOiJBQUVDQXdRRkJnY0lDUW9MREEwT0R4QVJFaE1VRlJZWEdCa2FHeHdkSGg4IiwiUmVxdWVzdEhhc2giOiIiLCJQYWNrYWdlTmFtZSI6ImRldi5sc2N5dGhlLmFwcC5uYXZpZ28uc3RhZ2luZyIsIlNpZ25lckRpZ2VzdCI6IkFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUEiLCJQcm92aWRlciI6ImRldmVsb3BtZW50IiwiQWN0aW9uIjoiY3JlYXRlLXNlc3Npb24iLCJFeHBpcmVzQXQiOiIyMDI2LTA5LTA4VDEyOjAyOjAwWiJ9LCJleHBpcmVzX2F0IjoiMjAyNi0wOS0wOFQxMjowMTowMFoifQ",
                        signature =
                            "q0SWWJbwmH2V5SvEpWbkuRcdZgHFSvJypu4AGEvfUHkSpjTAalRHQ52SaxHT1GyNGA-XvqmbtC7cNp2ZJbYkAQ",
                        keyId = "development-vector-key",
                    )
                )
        }

        test("rejects malformed private seed") {
            val provider =
                DevelopmentEvidenceProvider(
                    packageName = "package",
                    signerDigest = "digest",
                    keyId = "key",
                    privateKeySeed = "invalid",
                    evidenceExpiresAt = { Instant.parse("2026-09-08T12:01:00Z") },
                )

            provider.createSessionEvidence(challenge()) shouldBe
                AuthResult.Failure(dev.lscythe.app.navigo.domain.auth.AuthFailure.InvalidEvidence())
        }
    })

private fun challenge() =
    AuthChallenge(
        id = "challenge",
        nonce = "nonce",
        provider = AuthProvider.Development,
        action = AuthAction.CreateSession,
        protocolVersion = "v2",
        expiresAt = Instant.parse("2026-09-08T12:02:00Z"),
    )
