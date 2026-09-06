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

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.token.MaterialKolorConfig
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme
import dev.lscythe.app.navigo.core.testing.screenshot.captureMultiTheme
import dev.lscythe.app.navigo.feature.home.impl.content.HomeTopBar
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class HomeTopBarScreenshotTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun homeTopBar_multipleThemes() {
        composeTestRule.captureMultiTheme(
            name = "HomeTopBar",
            theme = { isDark, alternate, content ->
                NavigoTheme(
                    isDarkTheme = isDark,
                    disableDynamicTheming = true,
                    materialKolor = MaterialKolorConfig().takeIf { alternate },
                    content = content,
                )
            },
        ) {
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
