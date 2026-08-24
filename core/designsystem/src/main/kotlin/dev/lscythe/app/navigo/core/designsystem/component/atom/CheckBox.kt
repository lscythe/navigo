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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.action.Check
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays a checkbox that can be checked or unchecked.
 *
 * @param checked whether this checkbox is checked
 * @param onCheckedChange called when this checkbox is clicked, or `null` for a read-only checkbox
 * @param modifier the [Modifier] to apply to this checkbox
 * @param enabled whether this checkbox responds to user input
 */
@Composable
fun NavigoCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val containerColor = if (checked) MaterialTheme.colorScheme.primary else Color.Transparent
    val borderColor =
        if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Box(
        modifier =
            modifier
                .size(48.dp)
                .then(
                    if (onCheckedChange == null) {
                        Modifier.semantics {
                            role = Role.Checkbox
                            toggleableState = ToggleableState(checked)
                            if (!enabled) disabled()
                        }
                    } else {
                        Modifier.toggleable(
                            value = checked,
                            enabled = enabled,
                            role = Role.Checkbox,
                            interactionSource = interactionSource,
                            indication = null,
                            onValueChange = onCheckedChange,
                        )
                    }
                )
                .alpha(if (enabled) 1f else 0.38f),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier.size(24.dp)
                    .background(containerColor, MaterialTheme.shapes.extraSmall)
                    .border(2.dp, borderColor, MaterialTheme.shapes.extraSmall),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                Icon(
                    imageVector = NavigoIcons.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoCheckboxPreview() {
    NavigoPreview {
        var checked by remember { mutableStateOf(false) }

        Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
            NavigoCheckbox(
                checked = checked,
                onCheckedChange = { checked = it },
            )
            NavigoCheckbox(
                checked = true,
                onCheckedChange = null,
            )
            NavigoCheckbox(
                checked = false,
                onCheckedChange = null,
                enabled = false,
            )
            NavigoCheckbox(
                checked = true,
                onCheckedChange = null,
                enabled = false,
            )
        }
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoCheckboxMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        NavigoCheckbox(checked = true, onCheckedChange = null)
    }
}
