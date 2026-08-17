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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.status.Circle
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays an icon from an [ImageVector].
 *
 * @param imageVector the vector image to display
 * @param contentDescription text describing this icon, or `null` when decorative
 * @param modifier the [Modifier] to apply to this icon
 * @param size the width and height of this icon
 * @param tint the color applied to this icon
 */
@Composable
fun NavigoIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint,
    )
}

/**
 * Displays an icon from a [Painter].
 *
 * @param painter the image to display
 * @param contentDescription text describing this icon, or `null` when decorative
 * @param modifier the [Modifier] to apply to this icon
 * @param size the width and height of this icon
 * @param tint the color applied to this icon
 */
@Composable
fun NavigoIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint,
    )
}

@NavigoThemePreview
@Composable
private fun NavigoIconPreview() {
    NavigoPreview {
        IconPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoIconMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        IconPreviewContent()
    }
}

@Composable
private fun IconPreviewContent() {
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.cardPadding)) {
        NavigoIcon(imageVector = NavigoIcons.Circle, contentDescription = "Circle")
        NavigoIcon(
            imageVector = NavigoIcons.Circle,
            contentDescription = "Large circle",
            size = 32.dp,
        )
        NavigoIcon(
            imageVector = NavigoIcons.Circle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}
