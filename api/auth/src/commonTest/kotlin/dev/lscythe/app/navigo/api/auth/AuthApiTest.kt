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
package dev.lscythe.app.navigo.api.auth

import dev.lscythe.app.navigo.api.auth.constant.AuthAction
import dev.lscythe.app.navigo.api.auth.dto.AndroidEnrollmentEvidenceRequest
import dev.lscythe.app.navigo.api.auth.dto.AttestationChallengeRequest
import dev.lscythe.app.navigo.api.auth.dto.AttestationChallengeResponse
import dev.lscythe.app.navigo.api.auth.dto.AttestationEnrollmentRequest
import dev.lscythe.app.navigo.api.auth.dto.AttestationEnrollmentResponse
import dev.lscythe.app.navigo.api.auth.dto.DevelopmentEvidenceRequest
import dev.lscythe.app.navigo.api.auth.dto.PlayIntegrityRequest
import dev.lscythe.app.navigo.api.auth.dto.SessionRefreshRequest
import dev.lscythe.app.navigo.api.auth.dto.SessionRequest
import dev.lscythe.app.navigo.api.auth.dto.SessionResponse
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.testing.readResource
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.Instant
import kotlinx.serialization.json.Json

class AuthApiTest :
    FunSpec({
        test("creates an attestation challenge") {
            val engine = MockEngine { request ->
                request.method shouldBe HttpMethod.Post
                request.url.encodedPath shouldBe "/v1/attestation-challenges"
                val body = request.body as TextContent
                body.contentType shouldBe ContentType.Application.Json
                Json.parseToJsonElement(body.text) shouldBe
                    Json.parseToJsonElement(readResource("auth/integrity-challenge-request.json"))
                respond(
                    content = readResource("auth/integrity-challenge-response.json"),
                    status = HttpStatusCode.Created,
                    headers = io.ktor.http.headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client =
                HttpClient(engine) {
                    defaultRequest { contentType(ContentType.Application.Json) }
                    install(ContentNegotiation) { json() }
                }
            val api: PublicAuthApi = PublicAuthApiImpl(client)

            val result =
                api.createAttestationChallenge(
                    AttestationChallengeRequest(
                        provider = "play-integrity",
                        action = AuthAction.CREATE_SESSION,
                        packageName = "dev.lscythe.app.navigo.staging",
                    )
                )

            result shouldBe
                ApiResponse.Success(
                    AttestationChallengeResponse(
                        id = "challenge-id",
                        nonce = "base64url-nonce",
                        provider = "play-integrity",
                        action = "create-session",
                        protocolVersion = "v2",
                        expiresAt = Instant.parse("2026-08-28T05:02:00Z"),
                    )
                )
        }

        test("creates an attestation enrollment") {
            val engine = MockEngine { request ->
                request.method shouldBe HttpMethod.Post
                request.url.encodedPath shouldBe "/v1/attestation-enrollments"
                val body = request.body as TextContent
                Json.parseToJsonElement(body.text) shouldBe
                    Json.parseToJsonElement(
                        readResource("auth/attestation-enrollment-request.json")
                    )
                respond(
                    content = readResource("auth/attestation-enrollment-response.json"),
                    status = HttpStatusCode.Created,
                    headers = io.ktor.http.headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client =
                HttpClient(engine) {
                    defaultRequest { contentType(ContentType.Application.Json) }
                    install(ContentNegotiation) { json() }
                }
            val api: PublicAuthApi = PublicAuthApiImpl(client)

            val result =
                api.createAttestationEnrollment(
                    AttestationEnrollmentRequest(
                        challengeId = "challenge-id",
                        androidKeyAttestation =
                            AndroidEnrollmentEvidenceRequest(
                                certificateChain = listOf("certificate-one", "certificate-two"),
                                publicKeyId = "public-key-id",
                            ),
                    )
                )

            result shouldBe ApiResponse.Success(AttestationEnrollmentResponse("enrollment-id"))
        }

        test("creates a session") {
            val engine = MockEngine { request ->
                request.method shouldBe HttpMethod.Post
                request.url.encodedPath shouldBe "/v1/sessions"
                val body = request.body as TextContent
                body.contentType shouldBe ContentType.Application.Json
                Json.parseToJsonElement(body.text) shouldBe
                    Json.parseToJsonElement(readResource("auth/session-request.json"))
                respond(
                    content = readResource("auth/session-response.json"),
                    status = HttpStatusCode.Created,
                    headers = io.ktor.http.headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client =
                HttpClient(engine) {
                    defaultRequest { contentType(ContentType.Application.Json) }
                    install(ContentNegotiation) { json() }
                }
            val api: PublicAuthApi = PublicAuthApiImpl(client)

            val result =
                api.createSession(
                    SessionRequest(
                        challengeId = "challenge-id",
                        playIntegrity = PlayIntegrityRequest(token = "provider-token"),
                    )
                )

            result shouldBe
                ApiResponse.Success(
                    SessionResponse(
                        id = "session-id",
                        accessToken = "access-token",
                        refreshToken = "refresh-token",
                        accessExpiresAt = Instant.parse("2026-08-28T05:15:00Z"),
                        refreshExpiresAt = Instant.parse("2026-08-29T05:00:00Z"),
                        installationId = "installation-id",
                    )
                )
        }

        test("creates a session with development evidence only") {
            val engine = MockEngine { request ->
                val body = request.body as TextContent
                Json.parseToJsonElement(body.text) shouldBe
                    Json.parseToJsonElement(readResource("auth/development-session-request.json"))
                respond(
                    content = readResource("auth/session-response.json"),
                    status = HttpStatusCode.Created,
                    headers = io.ktor.http.headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client =
                HttpClient(engine) {
                    defaultRequest { contentType(ContentType.Application.Json) }
                    install(ContentNegotiation) { json() }
                }

            PublicAuthApiImpl(client)
                .createSession(
                    SessionRequest(
                        challengeId = "challenge-id",
                        development =
                            DevelopmentEvidenceRequest(
                                payload = "base64url-payload",
                                signature = "base64url-signature",
                                keyId = "development-key",
                            ),
                    )
                )
        }

        test("requires exactly one session evidence type") {
            shouldThrow<IllegalArgumentException> {
                SessionRequest(challengeId = "challenge-id")
            }
            shouldThrow<IllegalArgumentException> {
                SessionRequest(
                    challengeId = "challenge-id",
                    playIntegrity = PlayIntegrityRequest("token"),
                    development = DevelopmentEvidenceRequest("payload", "signature", "key"),
                )
            }
        }

        test("refreshes a session") {
            val engine = MockEngine { request ->
                request.method shouldBe HttpMethod.Post
                request.url.encodedPath shouldBe "/v1/session-refreshes"
                val body = request.body as TextContent
                body.contentType shouldBe ContentType.Application.Json
                Json.parseToJsonElement(body.text) shouldBe
                    Json.parseToJsonElement(readResource("auth/session-refresh-request.json"))
                respond(
                    content = readResource("auth/session-refresh-response.json"),
                    status = HttpStatusCode.Created,
                    headers = io.ktor.http.headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client =
                HttpClient(engine) {
                    defaultRequest { contentType(ContentType.Application.Json) }
                    install(ContentNegotiation) { json() }
                }
            val api: PublicAuthApi = PublicAuthApiImpl(client)

            val result = api.refreshSession(SessionRefreshRequest(refreshToken = "refresh-token"))

            result shouldBe
                ApiResponse.Success(
                    SessionResponse(
                        id = "session-id",
                        accessToken = "new-access-token",
                        refreshToken = "new-refresh-token",
                        accessExpiresAt = Instant.parse("2019-08-24T14:15:22Z"),
                        refreshExpiresAt = Instant.parse("2019-08-24T14:15:22Z"),
                        installationId = "installation-id",
                    )
                )
        }

        test("deletes the current session") {
            val engine = MockEngine { request ->
                request.method shouldBe HttpMethod.Delete
                request.url.encodedPath shouldBe "/v1/sessions/current"
                respond(content = "", status = HttpStatusCode.NoContent)
            }
            val api: SessionApi = SessionApiImpl(HttpClient(engine))

            api.deleteCurrentSession() shouldBe ApiResponse.Success(Unit)
        }
    })
