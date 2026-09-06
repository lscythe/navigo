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

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme
import dev.lscythe.app.navigo.core.testing.screenshot.captureDesktopScreenshot
import dev.lscythe.app.navigo.feature.home.impl.content.HomeTopBar
import kotlin.test.Test

class HomeTopBarScreenshotTest {
    @Test
    fun homeTopBar() =
        captureDesktopScreenshot("src/test/screenshots-desktop/home-top-bar.png") {
            NavigoTheme(disableDynamicTheming = true) {
                HomeTopBar(
                    profileInitials = "AK",
                    notificationCount = 3,
                    onSearchClick = {},
                    onNotificationsClick = {},
                    onProfileClick = {},
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
}
