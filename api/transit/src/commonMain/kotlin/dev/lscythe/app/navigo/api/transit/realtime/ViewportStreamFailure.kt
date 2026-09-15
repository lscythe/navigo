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
package dev.lscythe.app.navigo.api.transit.realtime

import dev.lscythe.app.navigo.core.network.ProblemDetail

/** Failures that terminate a viewport stream collection. */
sealed class ViewportStreamFailure(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    class Upgrade(
        val statusCode: Int,
        val contentLanguage: String? = null,
        val problem: ProblemDetail? = null,
        message: String = "Viewport stream upgrade failed with HTTP $statusCode",
        cause: Throwable? = null,
    ) : ViewportStreamFailure(message, cause)

    class Authentication(
        val statusCode: Int = 401,
        val contentLanguage: String? = null,
        val problem: ProblemDetail? = null,
        message: String = "Viewport stream authentication failed",
        cause: Throwable? = null,
    ) : ViewportStreamFailure(message, cause)

    class Protocol(message: String, cause: Throwable? = null) :
        ViewportStreamFailure(message, cause)

    class UnsupportedFrame(message: String) : ViewportStreamFailure(message)

    class RemoteClosed(val code: Short?, val reason: String?) :
        ViewportStreamFailure("Viewport stream closed${code?.let { " with code $it" } ?: ""}")

    class Transport(message: String? = null, cause: Throwable? = null) :
        ViewportStreamFailure(message ?: "Viewport stream transport failed", cause)
}
