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
package dev.lscythe.app.navigo.feature.home.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.feature.home.impl.content.HomeMap
import dev.lscythe.app.navigo.feature.home.impl.content.HomeNearbyStopsLayer
import dev.lscythe.app.navigo.feature.home.impl.content.HomeTopBar

@Composable
internal fun HomeScreen(
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        HomeMap()
        HomeTopBar(
            profileInitials = "AK",
            notificationCount = 3,
            onSearchClick = onSearchClick,
            onNotificationsClick = onNotificationsClick,
            onProfileClick = onProfileClick,
            modifier =
                Modifier.padding(
                    horizontal = NavigoSpacing.screen,
                    vertical = NavigoSpacing.item,
                ),
        )
        HomeNearbyStopsLayer()
    }
}
