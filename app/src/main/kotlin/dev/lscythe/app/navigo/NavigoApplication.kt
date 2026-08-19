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
package dev.lscythe.app.navigo

import android.app.Application
import dev.lscythe.app.navigo.core.monitoring.MonitoringEnvironment
import dev.lscythe.app.navigo.core.monitoring.initializeMonitoring
import dev.lscythe.app.navigo.di.NavigoGraph
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication
import timber.log.Timber

class NavigoApplication : Application(), MetroApplication {
    private lateinit var appGraph: NavigoGraph

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        initializeMonitoring(
            context = this,
            dsn = BuildConfig.SENTRY_DSN,
            environment = MonitoringEnvironment.valueOf(BuildConfig.MONITORING_ENVIRONMENT),
        )
        appGraph = createGraph<NavigoGraph>()
        appGraph.profileVerifierLogger()
    }

    override val appComponentProviders: MetroAppComponentProviders
        get() = appGraph
}
