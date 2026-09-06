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

import dev.lscythe.app.navigo.core.monitoring.AppLogger
import dev.lscythe.app.navigo.core.monitoring.CrashReporter
import dev.lscythe.app.navigo.core.monitoring.MonitoringBackend
import dev.lscythe.app.navigo.core.monitoring.MonitoringConfig
import dev.lscythe.app.navigo.core.monitoring.MonitoringEnvironment
import dev.lscythe.app.navigo.core.monitoring.Tracer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.sentry.kotlin.multiplatform.Sentry

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class SentryMonitoringBackend : MonitoringBackend {
    override val crashReporter: CrashReporter = SentryCrashReporter()
    override val appLogger: AppLogger = SentryAppLogger()
    override val tracer: Tracer = SentryTracer()

    override fun initialize(config: MonitoringConfig) {
        Sentry.init { options ->
            options.dsn = config.dsn
            options.environment = config.environment.name.lowercase()
            options.logs.enabled = true
            options.tracesSampleRate =
                if (config.environment == MonitoringEnvironment.Prod) 0.2 else 1.0
        }
    }
}
