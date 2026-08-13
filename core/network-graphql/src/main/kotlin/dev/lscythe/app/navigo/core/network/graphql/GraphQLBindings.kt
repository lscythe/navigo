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

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import dev.lscythe.app.navigo.core.network.NetworkConstant
import dev.lscythe.app.navigo.core.network.NetworkLogger
import dev.lscythe.app.navigo.core.network.TokenProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@ContributesTo(AppScope::class)
@BindingContainer
object GraphQLBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideGraphQLOkHttpClient(
        tokenProvider: TokenProvider,
        networkLogger: NetworkLogger,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(NetworkConstant.CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(NetworkConstant.SOCKET_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .addInterceptor(
                Interceptor { chain ->
                    val token = runBlocking { tokenProvider.getToken() }
                    val request =
                        if (token == null) {
                            chain.request()
                        } else {
                            chain
                                .request()
                                .newBuilder()
                                .header("Authorization", "Bearer $token")
                                .build()
                        }
                    chain.proceed(request)
                }
            )
            .addInterceptor(
                HttpLoggingInterceptor { message -> networkLogger.log(message) }
                    .apply { level = HttpLoggingInterceptor.Level.BASIC }
            )
            .build()

    @Provides
    @SingleIn(AppScope::class)
    fun provideApolloClient(
        @GraphQLEndpoint endpoint: String,
        okHttpClient: OkHttpClient,
    ): ApolloClient = ApolloClient.Builder().serverUrl(endpoint).okHttpClient(okHttpClient).build()
}
