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
package dev.lscythe.app.navigo.core.ui.error

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class ErrorBottomSheetTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun retrySheetDisplaysContentAndDelegatesActions() {
        var primaryActions = 0
        var cancellations = 0
        composeTestRule.setContent {
            NavigoTheme(isDarkTheme = false, disableDynamicTheming = true) {
                NavigoErrorBottomSheet(
                    title = "Connection lost",
                    message = "Check your connection and try again.",
                    illustration = { Box(Modifier.size(160.dp).testTag("illustration")) },
                    primaryActionLabel = "Retry",
                    onPrimaryAction = { primaryActions++ },
                    onCancel = { cancellations++ },
                    cancelLabel = "Cancel",
                )
            }
        }

        composeTestRule.onNodeWithText("Connection lost").assertIsDisplayed()
        composeTestRule.onNodeWithText("Check your connection and try again.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").performClick()
        composeTestRule.onNodeWithText("Cancel").performClick()

        assertEquals(1, primaryActions)
        assertEquals(1, cancellations)
    }

    @Test
    fun continueSheetDelegatesContinueAction() {
        var primaryActions = 0
        composeTestRule.setContent {
            NavigoTheme(isDarkTheme = false, disableDynamicTheming = true) {
                NavigoErrorBottomSheet(
                    title = "Live data unavailable",
                    message = "Continue with saved data.",
                    illustration = { Box(Modifier.size(160.dp)) },
                    primaryActionLabel = "Continue",
                    onPrimaryAction = { primaryActions++ },
                    onCancel = {},
                    cancelLabel = "Cancel",
                    primaryAction = ErrorPrimaryAction.Continue,
                )
            }
        }

        composeTestRule.onNodeWithText("Continue").performClick()

        assertEquals(1, primaryActions)
    }
}
