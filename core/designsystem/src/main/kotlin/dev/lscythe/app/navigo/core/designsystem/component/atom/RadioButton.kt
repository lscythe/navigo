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

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays a radio button that can be selected or unselected.
 *
 * @param selected whether this radio button is selected
 * @param onClick called when this radio button is clicked, or `null` for a read-only radio button
 * @param modifier the [Modifier] to apply to this radio button
 * @param enabled whether this radio button responds to user input
 */
@Composable
fun NavigoRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val indicatorModifier =
        if (selected) {
            Modifier.border(6.dp, MaterialTheme.colorScheme.primary, CircleShape)
        } else {
            Modifier.border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
        }

    Box(
        modifier =
            modifier
                .size(20.dp)
                .then(
                    if (onClick == null) {
                        Modifier.semantics {
                            role = Role.RadioButton
                            this.selected = selected
                            if (!enabled) disabled()
                        }
                    } else {
                        Modifier.selectable(
                            selected = selected,
                            enabled = enabled,
                            role = Role.RadioButton,
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick,
                        )
                    }
                )
                .alpha(if (enabled) 1f else 0.38f),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.size(24.dp).then(indicatorModifier))
    }
}

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
