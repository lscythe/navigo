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
package dev.lscythe.app.navigo.desktop

import co.touchlab.kermit.Severity
import dev.lscythe.app.navigo.app.AxerAppLogger
import dev.lscythe.app.navigo.core.monitoring.AppLogger
import io.github.orioneee.Axer
import io.github.orioneee.installErrorHandler

internal fun configureAxerDiagnostics() {
    Axer.configure {
        enableRequestMonitor = true
        enableExceptionMonitor = true
        enableLogMonitor = true
        enableDatabaseMonitor = false
        isRecordingLogs = true
        isSendNotification = true
    }
    Axer.installErrorHandler()
}

internal fun axerAppLogger(delegate: AppLogger): AppLogger =
    AxerAppLogger(delegate) { severity, message ->
        when (severity) {
            Severity.Verbose -> Axer.v(tag = "Navigo", message = message)
            Severity.Debug -> Axer.d(tag = "Navigo", message = message)
            Severity.Info -> Axer.i(tag = "Navigo", message = message)
            Severity.Warn -> Axer.w(tag = "Navigo", message = message)
            Severity.Error -> Axer.e(tag = "Navigo", message = message)
            Severity.Assert -> Axer.wtf(tag = "Navigo", message = message)
        }
    }
