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
package dev.lscythe.app.navigo.core.network.graphql

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import dev.lscythe.app.navigo.core.network.ApiResponse

fun <D : Operation.Data> ApolloResponse<D>.toApiResponse(): ApiResponse<D> {
    val currentData = data
    if (currentData != null) {
        return ApiResponse.Success(currentData)
    }

    exception?.let {
        return ApiResponse.Error.NetworkError(message = it.message, cause = it)
    }

    val firstError = errors?.firstOrNull()
    return ApiResponse.Error.GraphQLError(
        code = firstError?.extensions?.get("code") as? String,
        retryable = firstError?.extensions?.get("retryable") as? Boolean,
        message = firstError?.message,
    )
}
