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

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.ContentConvertException

suspend inline fun <reified T> safeRequest(
    noinline block: suspend () -> HttpResponse
): ApiResponse<T> =
    try {
        val response = block()
        ApiResponse.Success(response.body())
    } catch (e: ClientRequestException) {
        val problem = runCatching { e.response.body<ProblemDetail>() }.getOrNull()
        ApiResponse.Error.ClientError(
            code = e.response.status.value,
            problem = problem,
            message = e.message,
        )
    } catch (e: ServerResponseException) {
        val problem = runCatching { e.response.body<ProblemDetail>() }.getOrNull()
        ApiResponse.Error.ServerError(
            code = e.response.status.value,
            problem = problem,
            message = e.message,
        )
    } catch (e: ContentConvertException) {
        ApiResponse.Error.SerializationError(message = e.message)
    } catch (e: HttpRequestTimeoutException) {
        ApiResponse.Error.NetworkError(message = "Request Timeout", cause = e)
    } catch (e: Exception) {
        ApiResponse.Error.UnknownError(message = e.message, cause = e)
    }

suspend fun safeNoContentRequest(block: suspend () -> HttpResponse): ApiResponse<Unit> =
    try {
        block()
        ApiResponse.Success(Unit)
    } catch (e: ClientRequestException) {
        val problem = runCatching { e.response.body<ProblemDetail>() }.getOrNull()
        ApiResponse.Error.ClientError(
            code = e.response.status.value,
            problem = problem,
            message = e.message,
        )
    } catch (e: ServerResponseException) {
        val problem = runCatching { e.response.body<ProblemDetail>() }.getOrNull()
        ApiResponse.Error.ServerError(
            code = e.response.status.value,
            problem = problem,
            message = e.message,
        )
    } catch (e: HttpRequestTimeoutException) {
        ApiResponse.Error.NetworkError(message = "Request Timeout", cause = e)
    } catch (e: Exception) {
        ApiResponse.Error.UnknownError(message = e.message, cause = e)
    }
