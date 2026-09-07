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
package dev.lscythe.app.navigo.core.ui.locale

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.lscythe.app.navigo.core.common.locale.SupportedLanguage
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LanguageSelectionBottomSheetTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun languageSelectionAndApplyDelegateIndependently() {
        var selected: SupportedLanguage? = SupportedLanguage.Indonesian
        var applies = 0
        composeTestRule.setContent {
            NavigoTheme(disableDynamicTheming = true) {
                LanguageSelectionBottomSheet(
                    selectedLanguage = selected,
                    onLanguageSelected = { selected = it },
                    onApply = { applies++ },
                    onDismissRequest = {},
                    sheetState = rememberBottomSheetState(SheetValue.Expanded),
                )
            }
        }

        composeTestRule.onNodeWithText("English").performClick()
        composeTestRule.onNodeWithText("Apply").performClick()

        assertEquals(SupportedLanguage.English, selected)
        assertEquals(1, applies)
    }
}
