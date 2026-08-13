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

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain

class ApiResponseTest :
    FunSpec({
        test("ClientError throws with code and message") {
            val error = ApiResponse.Error.ClientError(code = 404, message = "not found")

            val thrown = shouldThrow<Exception> { error.toThrowable() }

            thrown.message shouldContain "404"
            thrown.message shouldContain "not found"
        }

        test("ServerError throws with code and message") {
            val error = ApiResponse.Error.ServerError(code = 500, message = "boom")

            val thrown = shouldThrow<Exception> { error.toThrowable() }

            thrown.message shouldContain "500"
            thrown.message shouldContain "boom"
        }

        test("NetworkError throws with its message") {
            val error = ApiResponse.Error.NetworkError(message = "timeout")

            val thrown = shouldThrow<Exception> { error.toThrowable() }

            thrown.message shouldContain "timeout"
        }

        test("NetworkError falls back to default message when null") {
            val error = ApiResponse.Error.NetworkError()

            val thrown = shouldThrow<Exception> { error.toThrowable() }

            thrown.message shouldContain "Network error"
        }

        test("SerializationError falls back to default message when null") {
            val error = ApiResponse.Error.SerializationError()

            val thrown = shouldThrow<Exception> { error.toThrowable() }

            thrown.message shouldContain "Serialization error"
        }

        test("UnknownError falls back to default message when null") {
            val error = ApiResponse.Error.UnknownError()

            val thrown = shouldThrow<Exception> { error.toThrowable() }

            thrown.message shouldContain "Unknown error"
        }
    })
