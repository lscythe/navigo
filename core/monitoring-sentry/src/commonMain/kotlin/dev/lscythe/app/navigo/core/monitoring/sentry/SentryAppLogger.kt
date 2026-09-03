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
package dev.lscythe.app.navigo.core.monitoring.sentry

import co.touchlab.kermit.Severity
import dev.lscythe.app.navigo.core.monitoring.AppLogger
import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.log.SentryLogLevel

class SentryAppLogger : AppLogger {
    override fun log(severity: Severity, message: String, attributes: Map<String, Any?>) {
        Sentry.logger.log(severity.toSentryLogLevel(), message) {
            attributes.forEach { (key, value) ->
                when (value) {
                    is String -> this[key] = value
                    is Int -> this[key] = value
                    is Long -> this[key] = value
                    is Float -> this[key] = value
                    is Double -> this[key] = value
                    is Boolean -> this[key] = value
                    null -> this[key] = "null"
                    else -> this[key] = value.toString()
                }
            }
        }
    }
}

private fun Severity.toSentryLogLevel(): SentryLogLevel =
    when (this) {
        Severity.Verbose -> SentryLogLevel.TRACE
        Severity.Debug -> SentryLogLevel.DEBUG
        Severity.Info -> SentryLogLevel.INFO
        Severity.Warn -> SentryLogLevel.WARN
        Severity.Error -> SentryLogLevel.ERROR
        Severity.Assert -> SentryLogLevel.FATAL
    }
