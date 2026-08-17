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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays an avatar containing text.
 *
 * @param text the text displayed in this avatar
 * @param modifier the [Modifier] to apply to this avatar
 * @param shape the shape of this avatar
 * @param size the width and height of this avatar
 * @param containerColor the background color of this avatar
 * @param contentColor the color of the text
 * @param textStyle the style of the text
 */
@Composable
fun NavigoAvatar(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    size: Dp = 56.dp,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    Box(
        modifier = modifier.size(size).clip(shape).background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = contentColor,
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

/**
 * Displays an avatar containing an image.
 *
 * @param painter the image displayed in this avatar
 * @param contentDescription text describing the image, or `null` when decorative
 * @param modifier the [Modifier] to apply to this avatar
 * @param shape the shape of this avatar
 * @param size the width and height of this avatar
 */
@Composable
fun NavigoAvatar(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    size: Dp = 56.dp,
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier.size(size).clip(shape),
    )
}

@NavigoThemePreview
@Composable
private fun NavigoAvatarPreview() {
    NavigoPreview {
        AvatarPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoAvatarMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        AvatarPreviewContent()
    }
}

@Composable
private fun AvatarPreviewContent() {
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.chipGap)) {
        NavigoAvatar(text = "AK", shape = MaterialTheme.shapes.extraLarge)
        NavigoAvatar(text = "27B")
        NavigoAvatar(
            painter = ColorPainter(MaterialTheme.colorScheme.tertiaryContainer),
            contentDescription = "Avatar image",
        )
    }
}
