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
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Switches between checked and unchecked states.
 *
 * @param checked whether this switch is checked
 * @param onCheckedChange called when this switch is clicked, or `null` for a read-only switch
 * @param modifier the [Modifier] to apply to this switch
 * @param enabled whether this switch responds to user input
 */
@Composable
fun NavigoSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
    )
}

@NavigoThemePreview
@Composable
private fun NavigoSwitchPreview() {
    NavigoPreview {
        var checked by remember { mutableStateOf(false) }

        Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
            NavigoSwitch(
                checked = checked,
                onCheckedChange = { checked = it },
            )
            NavigoSwitch(
                checked = true,
                onCheckedChange = null,
            )
            NavigoSwitch(
                checked = false,
                onCheckedChange = null,
                enabled = false,
            )
            NavigoSwitch(
                checked = true,
                onCheckedChange = null,
                enabled = false,
            )
        }
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoSwitchMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        NavigoSwitch(checked = true, onCheckedChange = null)
    }
}
