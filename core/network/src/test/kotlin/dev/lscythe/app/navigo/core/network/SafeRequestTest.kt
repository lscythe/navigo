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
package dev.lscythe.app.navigo.core.network

import dev.lscythe.app.navigo.core.testing.readResource
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Serializable private data class TestPayload(val id: Int)

private fun clientOf(engine: MockEngine, timeoutMillis: Long = 5_000) =
    HttpClient(engine) {
        expectSuccess = true
        install(HttpTimeout) { requestTimeoutMillis = timeoutMillis }
        install(ContentNegotiation) {
            json()
            json(contentType = ContentType("application", "problem+json"))
        }
    }

class SafeRequestTest :
    FunSpec({
        test("2xx response maps to Success") {
            val engine = MockEngine {
                respond(
                    content = readResource("network/success-response.json"),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client = clientOf(engine)

            val result = safeRequest<TestPayload> { client.get("/") }

            result shouldBe ApiResponse.Success(TestPayload(1))
        }

        test("4xx response maps to ClientError") {
            val engine = MockEngine {
                respond(content = "bad request", status = HttpStatusCode.BadRequest)
            }
            val client = clientOf(engine)

            val result = safeRequest<TestPayload> { client.get("/") }

            val error = result.shouldBeInstanceOf<ApiResponse.Error.ClientError>()
            error.code shouldBe 400
        }

        test("4xx with RFC 9457 problem+json body parses into ClientError.problem") {
            val engine = MockEngine {
                respond(
                    content = readResource("network/problem-response.json"),
                    status = HttpStatusCode.Forbidden,
                    headers = headersOf(HttpHeaders.ContentType, "application/problem+json"),
                )
            }
            val client = clientOf(engine)

            val result = safeRequest<TestPayload> { client.get("/") }

            val error = result.shouldBeInstanceOf<ApiResponse.Error.ClientError>()
            error.code shouldBe 403
            error.problem shouldBe
                ProblemDetail(
                    type = "https://example.com/probs/out-of-credit",
                    title = "You do not have enough credit.",
                    status = 403,
                    detail = "Your current balance is 30, but that costs 50.",
                    instance = "/account/12345/msgs/abc",
                )
        }

        test("5xx response maps to ServerError") {
            val engine = MockEngine {
                respond(content = "boom", status = HttpStatusCode.InternalServerError)
            }
            val client = clientOf(engine)

            val result = safeRequest<TestPayload> { client.get("/") }

            val error = result.shouldBeInstanceOf<ApiResponse.Error.ServerError>()
            error.code shouldBe 500
        }

        test("malformed body maps to SerializationError") {
            val engine = MockEngine {
                respond(
                    content = readResource("network/malformed-response.json"),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client = clientOf(engine)

            val result = safeRequest<TestPayload> { client.get("/") }

            result.shouldBeInstanceOf<ApiResponse.Error.SerializationError>()
        }

        test("timeout maps to NetworkError") {
            val engine = MockEngine {
                delay(500.milliseconds)
                respond(
                    content = readResource("network/success-response.json"),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client = clientOf(engine, timeoutMillis = 10)

            val result = safeRequest<TestPayload> { client.get("/") }

            result.shouldBeInstanceOf<ApiResponse.Error.NetworkError>()
        }

        test("unexpected exception maps to UnknownError") {
            val result = safeRequest<TestPayload> { error("kaboom") }

            val error = result.shouldBeInstanceOf<ApiResponse.Error.UnknownError>()
            error.message shouldBe "kaboom"
        }
    })
