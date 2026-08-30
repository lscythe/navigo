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
package dev.lscythe.app.navigo.core.designsystem.component.molecule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.navigation.ChevronRight
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays a rounded single-choice control with an optional trailing icon.
 *
 * @param selected whether this choice is selected.
 * @param onClick called when this choice is clicked.
 * @param label text displayed by this choice.
 * @param modifier modifier applied to this choice.
 * @param enabled whether this choice responds to input.
 * @param shape shape of the choice container.
 * @param horizontalPadding horizontal space around the content.
 * @param verticalPadding vertical space around the content.
 * @param labelStyle typography used for the label.
 * @param trailingIcon optional content displayed after the label.
 */
@Composable
fun NavigoChoiceChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    horizontalPadding: Dp = NavigoSpacing.screen,
    verticalPadding: Dp = NavigoSpacing.item,
    labelStyle: TextStyle = MaterialTheme.typography.titleMedium,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val disabledAlpha = if (enabled) 1f else 0.38f
    val containerColor =
        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor =
        if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Surface(
        modifier =
            modifier.selectable(
                selected = selected,
                enabled = enabled,
                role = Role.RadioButton,
                onClick = onClick,
            ),
        shape = shape,
        color = containerColor.copy(alpha = containerColor.alpha * disabledAlpha),
        contentColor = contentColor.copy(alpha = contentColor.alpha * disabledAlpha),
        border =
            if (selected) null
            else
                BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = disabledAlpha),
                ),
    ) {
        Row(
            modifier =
                Modifier.padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        ) {
            Text(text = label, style = labelStyle)
            trailingIcon?.invoke()
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoChoiceChipPreview() {
    NavigoPreview { ChoiceChipPreviewContent() }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoChoiceChipMaterialKolorPreview() {
    NavigoMaterialKolorPreview { ChoiceChipPreviewContent() }
}

@Composable
private fun ChoiceChipPreviewContent() {
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
        NavigoChoiceChip(selected = true, onClick = {}, label = "Bahasa")
        NavigoChoiceChip(
            selected = false,
            onClick = {},
            label = "English",
            trailingIcon = {
                NavigoIcon(
                    imageVector = NavigoIcons.ChevronRight,
                    contentDescription = null,
                )
            },
        )
    }
}
