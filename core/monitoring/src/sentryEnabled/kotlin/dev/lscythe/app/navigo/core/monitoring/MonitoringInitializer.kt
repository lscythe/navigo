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
package dev.lscythe.app.navigo.core.monitoring

import android.content.Context
import io.sentry.android.core.SentryAndroid

fun initializeMonitoring(context: Context, dsn: String, environment: MonitoringEnvironment) {
    SentryAndroid.init(context) { options ->
        options.dsn = dsn
        options.environment = environment.name.lowercase()
        options.isEnablePerformanceV2 = true
        @Suppress("UnstableApiUsage")
        options.logs.isEnabled = true
        options.tracesSampleRate = if (environment == MonitoringEnvironment.Prod) 0.2 else 1.0
    }
}
