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

sealed interface ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>

    sealed interface Error : ApiResponse<Nothing> {
        data class ClientError(
            val code: Int,
            val problem: ProblemDetail? = null,
            val contentLanguage: String? = null,
            val message: String? = null,
        ) : Error

        data class ServerError(
            val code: Int,
            val problem: ProblemDetail? = null,
            val contentLanguage: String? = null,
            val message: String? = null,
        ) : Error

        data class NetworkError(
            val message: String? = null,
            val cause: Throwable? = null,
        ) : Error

        data class SerializationError(val message: String? = null) : Error

        data class GraphQLError(
            val code: String? = null,
            val retryable: Boolean? = null,
            val message: String? = null,
        ) : Error

        data class UnknownError(
            val message: String? = null,
            val cause: Throwable? = null,
        ) : Error
    }
}

internal fun ApiResponse.Error.toThrowable(): Throwable =
    when (this) {
        is ApiResponse.Error.ClientError -> throw Exception("Client error $code: $message")
        is ApiResponse.Error.NetworkError -> throw Exception(message ?: "Network error")
        is ApiResponse.Error.SerializationError -> throw Exception(message ?: "Serialization error")
        is ApiResponse.Error.GraphQLError -> throw Exception(message ?: "GraphQL error: $code")
        is ApiResponse.Error.ServerError -> throw Exception("Server Error: $code: $message")
        is ApiResponse.Error.UnknownError -> throw Exception(message ?: "Unknown error")
    }
