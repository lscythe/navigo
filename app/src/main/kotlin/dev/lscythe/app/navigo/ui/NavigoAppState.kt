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
package dev.lscythe.app.navigo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import dev.lscythe.app.navigo.core.navigation.NavigationState
import dev.lscythe.app.navigo.core.navigation.Navigator
import dev.lscythe.app.navigo.core.navigation.rememberNavigationState
import dev.lscythe.app.navigo.core.ui.TrackDisposableJank

@Composable
fun rememberNavigoAppState(startKey: NavKey): NavigoAppState {
    val navigationState = rememberNavigationState(startKey)
    NavigationTrackingSideEffect(navigationState)
    return remember(navigationState) { NavigoAppState(navigationState) }
}

@Stable
class NavigoAppState(val navigationState: NavigationState) {
    val navigator = Navigator(navigationState)
}

/** Stores information about navigation events to be used with JankStats */
@Composable
private fun NavigationTrackingSideEffect(navigationState: NavigationState) {
    TrackDisposableJank(navigationState.currentKey) { metricsHolder ->
        metricsHolder.state?.putState("Navigation", navigationState.currentKey.toString())
        onDispose {}
    }
}
