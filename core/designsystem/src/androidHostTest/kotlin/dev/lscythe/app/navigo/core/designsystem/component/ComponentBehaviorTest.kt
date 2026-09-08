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
package dev.lscythe.app.navigo.core.designsystem.component

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoCheckbox
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoRadioButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoSwitch
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoChoiceChip
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoFilterChip
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ComponentBehaviorTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun buttonInvokesCallbackWhileDisabledButtonDoesNot() {
        var clicks = 0
        composeTestRule.setContent {
            NavigoTheme(disableDynamicTheming = true) {
                Column {
                    NavigoButton(onClick = { clicks++ }) { Text("Enabled") }
                    NavigoButton(onClick = { clicks++ }, enabled = false) { Text("Disabled") }
                }
            }
        }

        composeTestRule.onNodeWithText("Enabled").performClick()
        composeTestRule.onNodeWithText("Disabled").assertIsNotEnabled().performClick()

        assertEquals(1, clicks)
    }

    @Test
    fun selectionControlsExposeStateAndDelegateChanges() {
        var checkbox by mutableStateOf(false)
        var radioClicks = 0
        var switchValue = false
        composeTestRule.setContent {
            NavigoTheme(disableDynamicTheming = true) {
                Column {
                    NavigoCheckbox(checkbox, { checkbox = it })
                    NavigoRadioButton(selected = true, onClick = { radioClicks++ })
                    NavigoSwitch(checked = true, onCheckedChange = { switchValue = it })
                }
            }
        }

        composeTestRule
            .onAllNodes(
                SemanticsMatcher.keyIsDefined(
                    androidx.compose.ui.semantics.SemanticsProperties.Role
                )
            )
            .apply {
                get(0).performClick()
                get(1).assertIsSelected().performClick()
                get(2).assertIsOn().performClick()
            }

        assertEquals(true, checkbox)
        assertEquals(1, radioClicks)
        assertEquals(false, switchValue)
    }

    @Test
    fun chipsExposeSelectionAndDelegateClicks() {
        var choiceClicks = 0
        var filterClicks = 0
        composeTestRule.setContent {
            NavigoTheme(disableDynamicTheming = true) {
                Column {
                    NavigoChoiceChip(true, { choiceClicks++ }, "English")
                    NavigoFilterChip(false, { filterClicks++ }, "Buses", count = "12")
                }
            }
        }

        composeTestRule.onNodeWithText("English").assertIsSelected().performClick()
        composeTestRule.onNodeWithText("Buses").performClick()

        assertEquals(1, choiceClicks)
        assertEquals(1, filterClicks)
    }
}
