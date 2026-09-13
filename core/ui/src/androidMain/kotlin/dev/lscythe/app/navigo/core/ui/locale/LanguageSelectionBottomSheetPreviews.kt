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

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.lscythe.app.navigo.core.common.locale.SupportedLanguage
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview

@OptIn(ExperimentalMaterial3Api::class)
@NavigoThemePreview
@Composable
private fun LanguageSelectionBottomSheetPreview() {
    var showSheet by remember { mutableStateOf(false) }

    NavigoPreview {
        Scaffold { padding ->
            NavigoButton(onClick = { showSheet = true }, modifier = Modifier.padding(padding)) {
                Text("Open language selector")
            }
            if (showSheet) {
                LanguageSelectionBottomSheet(
                    selectedLanguage = SupportedLanguage.Indonesian,
                    onLanguageSelected = {},
                    onApply = { showSheet = false },
                    onDismissRequest = { showSheet = false },
                )
            }
        }
    }
}
