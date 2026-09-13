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
package dev.lscythe.app.navigo.app

import co.touchlab.kermit.Severity
import dev.lscythe.app.navigo.core.monitoring.AppLogger
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly

class AxerAppLoggerTest :
    FunSpec({
        test("delegates structured log before recording formatted Axer log") {
            val calls = mutableListOf<String>()
            val logger =
                AxerAppLogger(
                    delegate =
                        RecordingAppLogger { severity, message, attributes ->
                            calls += "delegate:$severity:$message:$attributes"
                        },
                    record = { severity, message -> calls += "axer:$severity:$message" },
                )

            logger.log(Severity.Warn, "Slow frame", mapOf("durationMs" to 24))

            calls.shouldContainExactly(
                "delegate:Warn:Slow frame:{durationMs=24}",
                "axer:Warn:Slow frame | durationMs=24",
            )
        }

        test("does not append separator when attributes are empty") {
            val calls = mutableListOf<String>()
            val logger =
                AxerAppLogger(
                    delegate = RecordingAppLogger { _, _, _ -> },
                    record = { severity, message -> calls += "$severity:$message" },
                )

            logger.log(Severity.Info, "Started")

            calls.shouldContainExactly("Info:Started")
        }
    })

private class RecordingAppLogger(private val block: (Severity, String, Map<String, Any?>) -> Unit) :
    AppLogger {
    override fun log(severity: Severity, message: String, attributes: Map<String, Any?>) {
        block(severity, message, attributes)
    }
}
