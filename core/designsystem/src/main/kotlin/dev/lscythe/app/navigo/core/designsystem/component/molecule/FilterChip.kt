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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoDot
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays a selectable filter with a filled or outlined marker and optional count.
 *
 * @param selected whether this filter is selected
 * @param onClick called when this filter is clicked
 * @param label the filter label
 * @param modifier the [Modifier] to apply to this chip
 * @param count the optional count displayed after the label
 * @param enabled whether this chip responds to user input
 * @param shape the shape of this chip
 * @param dotColor the marker color
 * @param dotSize the width and height of the marker
 * @param selectedContainerColor the background color when selected
 * @param selectedContentColor the label and count color when selected
 * @param unselectedContainerColor the background color when unselected
 * @param unselectedContentColor the label and count color when unselected
 * @param unselectedBorderColor the border color when unselected
 * @param horizontalPadding the horizontal space around this chip's content
 * @param verticalPadding the vertical space around this chip's content
 * @param labelStyle the style of the label
 * @param countStyle the style of the count
 */
@Composable
fun NavigoFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    count: String? = null,
    enabled: Boolean = true,
    shape: Shape = CircleShape,
    dotColor: Color = MaterialTheme.colorScheme.primary,
    dotSize: Dp = 12.dp,
    selectedContainerColor: Color = MaterialTheme.colorScheme.primary,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedContainerColor: Color = Color.Transparent,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
    unselectedBorderColor: Color = MaterialTheme.colorScheme.outline,
    horizontalPadding: Dp = NavigoSpacing.container,
    verticalPadding: Dp = NavigoSpacing.element,
    labelStyle: TextStyle = MaterialTheme.typography.titleMedium,
    countStyle: TextStyle = MaterialTheme.typography.titleMedium,
) {
    val containerColor = if (selected) selectedContainerColor else unselectedContainerColor
    val contentColor = if (selected) selectedContentColor else unselectedContentColor
    val disabledAlpha = if (enabled) 1f else 0.38f
    Surface(
        modifier =
            modifier.selectable(
                selected = selected,
                enabled = enabled,
                role = Role.Checkbox,
                onClick = onClick,
            ),
        shape = shape,
        color = containerColor.copy(alpha = containerColor.alpha * disabledAlpha),
        contentColor = contentColor.copy(alpha = contentColor.alpha * disabledAlpha),
        border =
            if (selected) null
            else BorderStroke(1.dp, unselectedBorderColor.copy(alpha = disabledAlpha)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        ) {
            NavigoDot(
                color =
                    if (selected) dotColor.copy(alpha = dotColor.alpha * disabledAlpha)
                    else Color.Transparent,
                size = dotSize,
                borderColor =
                    if (selected) Color.Transparent
                    else dotColor.copy(alpha = dotColor.alpha * disabledAlpha),
                borderWidth = if (selected) 0.dp else 2.dp,
            )
            Text(text = label, style = labelStyle, maxLines = 1)
            if (count != null) {
                Text(text = count, style = countStyle, maxLines = 1)
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoFilterChipPreview() {
    NavigoPreview {
        FilterChipPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoFilterChipMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        FilterChipPreviewContent()
    }
}

@Composable
private fun FilterChipPreviewContent() {
    var selected by remember { mutableStateOf(true) }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
    ) {
        NavigoFilterChip(
            selected = selected,
            onClick = { selected = !selected },
            label = "Buses",
            count = "12",
            dotColor = MaterialTheme.colorScheme.primaryFixed,
        )
        NavigoFilterChip(
            selected = false,
            onClick = {},
            label = "Stops",
            dotColor = MaterialTheme.colorScheme.outline,
        )
        NavigoFilterChip(
            selected = false,
            onClick = {},
            label = "Delays",
            count = "2",
            dotColor = MaterialTheme.colorScheme.tertiary,
        )
    }
}
