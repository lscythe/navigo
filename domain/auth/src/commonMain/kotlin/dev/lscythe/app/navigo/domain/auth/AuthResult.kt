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
package dev.lscythe.app.navigo.domain.auth

/** Stable failure categories exposed independently from HTTP transport details. */
sealed interface AuthFailure {
    val message: String?

    data class InvalidEvidence(override val message: String? = null) : AuthFailure

    data class UnsupportedProvider(override val message: String? = null) : AuthFailure

    data class ExpiredChallenge(override val message: String? = null) : AuthFailure

    data class ChallengeReplayed(override val message: String? = null) : AuthFailure

    data class EnrollmentRevoked(override val message: String? = null) : AuthFailure

    data class CounterReplayed(override val message: String? = null) : AuthFailure

    data class RejectedEvidence(override val message: String? = null) : AuthFailure

    data class Unauthenticated(override val message: String? = null) : AuthFailure

    data class Network(override val message: String? = null) : AuthFailure

    data class Server(override val message: String? = null) : AuthFailure

    data class Serialization(override val message: String? = null) : AuthFailure

    data class Unknown(override val message: String? = null) : AuthFailure
}

/** Result of an auth lifecycle operation. */
sealed interface AuthResult<out T> {
    data class Success<T>(val value: T) : AuthResult<T>

    data class Failure(val failure: AuthFailure) : AuthResult<Nothing>
}
