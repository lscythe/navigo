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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays a compact status dot.
 *
 * @param modifier the [Modifier] to apply to this dot
 * @param color the fill color of this dot
 * @param size the width and height of this dot
 * @param shape the shape of this dot
 * @param borderColor the border color of this dot
 * @param borderWidth the width of the border
 */
@Composable
fun NavigoDot(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: Dp = 12.dp,
    shape: Shape = CircleShape,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
) {
    Box(
        modifier =
            modifier
                .size(size)
                .clip(shape)
                .background(color)
                .then(
                    if (borderWidth > 0.dp) {
                        Modifier.border(borderWidth, borderColor, shape)
                    } else {
                        Modifier
                    }
                )
    )
}

/**
 * Displays a compact badge containing a text label.
 *
 * @param text the label displayed in this badge
 * @param modifier the [Modifier] to apply to this badge
 * @param shape the shape of this badge
 * @param containerColor the background color of this badge
 * @param contentColor the color of the label
 * @param textStyle the style of the label
 * @param horizontalPadding the horizontal space around the label
 * @param verticalPadding the vertical space around the label
 * @param minHeight the minimum height of this badge
 */
@Composable
fun NavigoBadge(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    horizontalPadding: Dp = 12.dp,
    verticalPadding: Dp = 4.dp,
    minHeight: Dp = 24.dp,
) {
    Box(
        modifier =
            modifier
                .defaultMinSize(minHeight = minHeight)
                .clip(shape)
                .background(containerColor)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = contentColor,
            style = textStyle,
            maxLines = 1,
        )
    }
}

@NavigoThemePreview
@Composable
private fun NavigoDotPreview() {
    NavigoPreview {
        DotPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoDotMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        DotPreviewContent()
    }
}

@Composable
private fun DotPreviewContent() {
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.cardPadding)) {
        NavigoDot(color = MaterialTheme.colorScheme.primary)
        NavigoDot(color = MaterialTheme.colorScheme.secondary)
        NavigoDot(color = MaterialTheme.colorScheme.tertiary)
        NavigoDot(
            color = Color.Transparent,
            borderColor = MaterialTheme.colorScheme.outline,
            borderWidth = 2.dp,
        )
    }
}

@NavigoThemePreview
@Composable
private fun NavigoBadgePreview() {
    NavigoPreview {
        BadgePreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoBadgeMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        BadgePreviewContent()
    }
}

@Composable
private fun BadgePreviewContent() {
    Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.chipGap)) {
        Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.cardPadding)) {
            NavigoBadge(text = "3")
            NavigoBadge(
                text = "12",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
        NavigoBadge(
            text = "REQUIRED",
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            textStyle = MaterialTheme.typography.labelSmall,
        )
    }
}
