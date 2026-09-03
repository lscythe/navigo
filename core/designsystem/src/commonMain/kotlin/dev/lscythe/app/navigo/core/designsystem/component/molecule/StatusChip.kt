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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
 * Displays a compact status label with a colored dot.
 *
 * @param label the status text
 * @param dotColor the color of the status dot
 * @param modifier the [Modifier] to apply to this chip
 * @param shape the shape of this chip
 * @param containerColor the background color of this chip
 * @param contentColor the color of the label
 * @param dotSize the width and height of the status dot
 * @param horizontalPadding the horizontal space around this chip's content
 * @param verticalPadding the vertical space around this chip's content
 * @param textStyle the style of the label
 */
@Composable
fun NavigoStatusChip(
    label: String,
    dotColor: Color,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    dotSize: Dp = 12.dp,
    horizontalPadding: Dp = NavigoSpacing.container,
    verticalPadding: Dp = NavigoSpacing.element,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        ) {
            NavigoDot(color = dotColor, size = dotSize)
            Text(text = label, style = textStyle, maxLines = 1)
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoStatusChipPreview() {
    NavigoPreview {
        StatusChipPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoStatusChipMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        StatusChipPreviewContent()
    }
}

@Composable
private fun StatusChipPreviewContent() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
    ) {
        NavigoStatusChip(
            label = "Live · 20s",
            dotColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        NavigoStatusChip(
            label = "Stale · 6 min",
            dotColor = MaterialTheme.colorScheme.tertiary,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
        NavigoStatusChip(
            label = "No reports",
            dotColor = MaterialTheme.colorScheme.outline,
        )
        NavigoStatusChip(
            label = "Offline",
            dotColor = MaterialTheme.colorScheme.error,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        )
    }
}
