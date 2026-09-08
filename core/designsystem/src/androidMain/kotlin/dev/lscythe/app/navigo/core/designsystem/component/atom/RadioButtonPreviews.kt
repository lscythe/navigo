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
package dev.lscythe.app.navigo.core.designsystem.component.atom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.selected
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

@NavigoThemePreview
@Composable
private fun NavigoRadioButtonPreview() {
    NavigoPreview {
        var selected by remember { mutableStateOf(false) }

        Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
            NavigoRadioButton(
                selected = selected,
                onClick = { selected = !selected },
            )
            NavigoRadioButton(
                selected = true,
                onClick = null,
            )
            NavigoRadioButton(
                selected = false,
                onClick = null,
                enabled = false,
            )
            NavigoRadioButton(
                selected = true,
                onClick = null,
                enabled = false,
            )
        }
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoRadioButtonMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        NavigoRadioButton(selected = true, onClick = null)
    }
}
