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
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalEncodingApi::class)
class AttestationCanonicalDataTest :
    FunSpec({
        val vectors =
            Json.parseToJsonElement(
                    requireNotNull(
                            AttestationCanonicalDataTest::class
                                .java
                                .classLoader
                                .getResource("auth/attestation-v2-vectors.json")
                        )
                        .readText()
                )
                .jsonObject

        test("development payload matches backend vector bytes") {
            val vector = vectors.getValue("development").jsonObject
            val expectedPayload = vector.getValue("payload").jsonPrimitive.content
            val challenge = vector.getValue("challenge").jsonObject

            val actual =
                developmentPayload(
                    challenge =
                        AuthChallenge(
                            id = challenge.getValue("ID").jsonPrimitive.content,
                            nonce = challenge.getValue("Nonce").jsonPrimitive.content,
                            provider = AuthProvider.Development,
                            action = AuthAction.CreateSession,
                            protocolVersion = "v2",
                            expiresAt =
                                Instant.parse(
                                    challenge.getValue("ExpiresAt").jsonPrimitive.content
                                ),
                        ),
                    packageName = challenge.getValue("PackageName").jsonPrimitive.content,
                    signerDigest = challenge.getValue("SignerDigest").jsonPrimitive.content,
                    expiresAt = Instant.parse(vector.getValue("expiresAt").jsonPrimitive.content),
                )

            actual.decodeToString() shouldBe expectedPayload
            Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(actual) shouldBe
                vector.getValue("payloadBase64Url").jsonPrimitive.content
        }

        test("Android assertion preserves fractional RFC3339 timestamp") {
            val vector = vectors.getValue("androidKeyAssertion").jsonObject
            val challenge = vector.getValue("challenge").jsonObject

            val actual =
                androidAssertionData(
                    challenge =
                        AuthChallenge(
                            id = challenge.getValue("ID").jsonPrimitive.content,
                            nonce = challenge.getValue("Nonce").jsonPrimitive.content,
                            provider = AuthProvider.AndroidKeyAttestation,
                            action = AuthAction.CreateSession,
                            protocolVersion = "v2",
                            expiresAt =
                                Instant.parse(
                                    challenge.getValue("ExpiresAt").jsonPrimitive.content
                                ),
                        ),
                    enrollmentId = vector.getValue("enrollmentId").jsonPrimitive.content,
                )

            actual.decodeToString() shouldBe vector.getValue("canonical").jsonPrimitive.content
            Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(actual) shouldBe
                vector.getValue("clientData").jsonPrimitive.content
        }
    })
