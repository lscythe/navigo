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
package dev.lscythe.app.navigo.di

import android.app.Application
import dev.lscythe.app.navigo.BuildConfig
import dev.lscythe.app.navigo.app.AttestationPackageName
import dev.lscythe.app.navigo.app.MainViewModel
import dev.lscythe.app.navigo.axerAppLogger
import dev.lscythe.app.navigo.core.monitoring.AppLogger
import dev.lscythe.app.navigo.core.monitoring.MonitoringBackend
import dev.lscythe.app.navigo.core.network.BaseUrl
import dev.lscythe.app.navigo.core.network.NetworkInspector
import dev.lscythe.app.navigo.core.network.NetworkLogger
import dev.lscythe.app.navigo.util.ProfileVerifierLogger
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@ContributesTo(AppScope::class)
@BindingContainer
object NavigoNetworkBindings {
    @Provides @BaseUrl fun provideBaseUrl(): String = BuildConfig.API_BASE_URL

    @Provides
    @AttestationPackageName
    fun provideAttestationPackageName(): String = BuildConfig.APPLICATION_ID.removeSuffix(".debug")

    @Provides
    fun provideAppLogger(backend: MonitoringBackend): AppLogger =
        if (BuildConfig.DEBUG) axerAppLogger(backend.appLogger) else backend.appLogger

    @Provides fun provideNetworkLogger(): NetworkLogger = NetworkLogger {}

    @Provides fun provideNetworkInspector(): NetworkInspector = AxerNetworkInspector()
}

@DependencyGraph(AppScope::class)
interface NavigoGraph : MetroAppComponentProviders, ViewModelGraph {
    val profileVerifierLogger: ProfileVerifierLogger
    val monitoringBackend: MonitoringBackend
    val mainViewModel: MainViewModel

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): NavigoGraph
    }
}
