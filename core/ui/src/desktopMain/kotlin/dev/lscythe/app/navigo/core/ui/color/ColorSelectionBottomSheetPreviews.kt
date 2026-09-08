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
package dev.lscythe.app.navigo.core.ui.color

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.lscythe.app.navigo.core.designsystem.preview.DesktopMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.DesktopPreview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ColorSelectionBottomSheetPreview() {
    var color by remember { mutableStateOf(Color(0xFF5C8A3E)) }
    DesktopPreview {
        ColorSelectionBottomSheet(
            title = "Pick a colour",
            description = "Choose any colour.",
            selectedColor = color,
            onApply = { color = it },
            onDismissRequest = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ColorSelectionBottomSheetMaterialKolorPreview() {
    var color by remember { mutableStateOf(Color(0xFF5C8A3E)) }
    DesktopMaterialKolorPreview {
        ColorSelectionBottomSheet(
            title = "Pick a colour",
            description = "Choose any colour.",
            selectedColor = color,
            onApply = { color = it },
            onDismissRequest = {},
        )
    }
}
