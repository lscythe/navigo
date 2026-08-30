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

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.metrics.performance.JankStats
import androidx.navigation3.runtime.entryProvider
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme
import dev.lscythe.app.navigo.core.monitoring.StructuredLogger
import dev.lscythe.app.navigo.core.navigation.Navigator
import dev.lscythe.app.navigo.feature.home.impl.navigation.homeEntry
import dev.lscythe.app.navigo.feature.onboarding.api.OnboardingNavKey
import dev.lscythe.app.navigo.feature.onboarding.impl.navigation.onboardingEntry
import dev.lscythe.app.navigo.ui.NavigoApp
import dev.lscythe.app.navigo.ui.rememberNavigoAppState
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

@Inject
@ActivityKey
@ContributesIntoMap(AppScope::class, binding<Activity>())
class MainActivity(
    private val viewModelFactory: MetroViewModelFactory,
    private val logger: StructuredLogger,
) : ComponentActivity() {

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = viewModelFactory

    private lateinit var jankStats: JankStats

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val appState = rememberNavigoAppState(OnboardingNavKey)

            val navigator = remember { Navigator(appState.navigationState) }

            val entryProvider = entryProvider {
                onboardingEntry(navigator)
                homeEntry(navigator)
            }

            NavigoTheme {
                NavigoApp(
                    appState = appState,
                    entryProvider = entryProvider,
                )
            }
        }
        jankStats =
            JankStats.createAndTrack(window) { frameData ->
                if (frameData.isJank) {
                    logger.log(
                        priority = Log.WARN,
                        message = "Janky frame",
                        attributes = mapOf("frameData" to frameData.toString()),
                    )
                }
            }
    }

    override fun onDestroy() {
        if (::jankStats.isInitialized) jankStats.isTrackingEnabled = false
        super.onDestroy()
    }
}
