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
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme
import dev.lscythe.app.navigo.feature.home.impl.content.HomeTopBar
import io.kotest.matchers.ints.shouldBeExactly
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeTopBarTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun controlsInvokeIndependentActions() {
        var searchClicks = 0
        var notificationClicks = 0
        var profileClicks = 0
        composeTestRule.setContent {
            NavigoTheme {
                HomeTopBar(
                    profileInitials = "AK",
                    notificationCount = 3,
                    onSearchClick = { searchClicks++ },
                    onNotificationsClick = { notificationClicks++ },
                    onProfileClick = { profileClicks++ },
                )
            }
        }

        composeTestRule.onNodeWithText("Where to, friend?").assertHasClickAction().performClick()
        searchClicks.shouldBeExactly(1)
        notificationClicks.shouldBeExactly(0)
        profileClicks.shouldBeExactly(0)

        composeTestRule
            .onNodeWithContentDescription("Notifications")
            .assertHasClickAction()
            .performClick()
        searchClicks.shouldBeExactly(1)
        notificationClicks.shouldBeExactly(1)
        profileClicks.shouldBeExactly(0)

        composeTestRule
            .onNodeWithContentDescription("Profile")
            .assertHasClickAction()
            .performClick()
        searchClicks.shouldBeExactly(1)
        notificationClicks.shouldBeExactly(1)
        profileClicks.shouldBeExactly(1)
    }
}
