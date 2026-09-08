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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import dev.lscythe.app.navigo.core.analytics.AnalyticsHelper
import dev.lscythe.app.navigo.core.analytics.LocalAnalyticsHelper
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme
import dev.lscythe.app.navigo.feature.home.api.HomeNavKey
import dev.lscythe.app.navigo.feature.home.impl.navigation.homeEntry
import dev.lscythe.app.navigo.feature.onboarding.api.OnboardingNavKey
import dev.lscythe.app.navigo.feature.onboarding.impl.navigation.onboardingEntry
import dev.lscythe.app.navigo.ui.NavigoApp
import dev.lscythe.app.navigo.ui.rememberNavigoAppState
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal fun navigoSerializersModule() = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(HomeNavKey.serializer())
        subclass(OnboardingNavKey.serializer())
    }
}

@Composable
fun NavigoRoot(
    analyticsHelper: AnalyticsHelper,
    viewModelFactory: MetroViewModelFactory,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val startupState by mainViewModel.state.collectAsState()
    NavigoTheme {
        when (val state = startupState) {
            StartupState.Initializing ->
                Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            is StartupState.AuthRequired ->
                if (state.destination == StartupDestination.Onboarding) {
                    NavigoNavigation(
                        analyticsHelper,
                        viewModelFactory,
                        mainViewModel,
                        OnboardingNavKey,
                        modifier,
                    )
                } else {
                    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(onClick = mainViewModel::retryAuthentication) { Text("Retry") }
                    }
                }
            is StartupState.Ready ->
                NavigoNavigation(
                    analyticsHelper,
                    viewModelFactory,
                    mainViewModel,
                    if (state.destination == StartupDestination.Home) HomeNavKey
                    else OnboardingNavKey,
                    modifier,
                )
        }
    }
}

@Composable
private fun NavigoNavigation(
    analyticsHelper: AnalyticsHelper,
    viewModelFactory: MetroViewModelFactory,
    mainViewModel: MainViewModel,
    initialRoute: NavKey,
    modifier: Modifier,
) {
    val serializersModule = remember { navigoSerializersModule() }
    val appState = rememberNavigoAppState(initialRoute, serializersModule)
    val entryProvider = entryProvider {
        onboardingEntry(mainViewModel::retryAuthentication)
        homeEntry(appState.navigator)
    }

    CompositionLocalProvider(
        LocalAnalyticsHelper provides analyticsHelper,
        LocalMetroViewModelFactory provides viewModelFactory,
    ) {
        NavigoApp(
            appState = appState,
            entryProvider = entryProvider,
            modifier = modifier,
        )
    }
}
