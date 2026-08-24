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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.iconSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.action.Plus
import dev.lscythe.app.navigo.core.designsystem.icon.status.ExclamationCircle
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays a short informational disclosure with an icon.
 *
 * @param text the disclosure text
 * @param icon the icon displayed before the text
 * @param modifier the [Modifier] to apply to this note
 * @param shape the shape of this note
 * @param containerColor the background color of this note
 * @param contentColor the color of the icon and text
 * @param contentPadding the space around this note's content
 * @param iconSize the width and height of the icon
 * @param iconContainerShape the optional shape behind the icon
 * @param iconContainerColor the optional background color behind the icon
 * @param iconContainerSize the width and height of the icon container
 * @param textStyle the style of the disclosure text
 */
@Composable
fun NavigoDisclosureNote(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    contentPadding: Dp = NavigoSpacing.container,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    iconSize: Dp = 24.dp,
    iconTint: Color = Color.White,
    iconContainerShape: Shape? = null,
    iconContainerColor: Color = Color.Transparent,
    iconContainerSize: Dp = 40.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Row(
            modifier = Modifier.padding(contentPadding),
            verticalAlignment = alignment,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
        ) {
            if (iconContainerShape != null) {
                Box(
                    modifier =
                        Modifier.size(iconContainerSize)
                            .background(iconContainerColor, iconContainerShape),
                    contentAlignment = Alignment.Center,
                ) {
                    DisclosureIcon(icon = icon, size = iconSize, tint = iconTint)
                }
            } else {
                DisclosureIcon(icon = icon, size = iconSize, tint = iconTint)
            }
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = textStyle,
            )
        }
    }
}

@Composable
private fun DisclosureIcon(
    icon: ImageVector,
    size: Dp,
    tint: Color,
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(size),
    )
}

@NavigoThemePreview
@Composable
private fun NavigoDisclosureNotePreview() {
    NavigoPreview {
        DisclosureNotePreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoDisclosureNoteMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        DisclosureNotePreviewContent()
    }
}

@Composable
private fun DisclosureNotePreviewContent() {
    Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.container)) {
        NavigoDisclosureNote(
            text = "Reports are anonymous. Location is shared only while you're on a bus.",
            icon = NavigoIcons.ExclamationCircle,
            modifier = Modifier.fillMaxWidth(),
        )
        NavigoDisclosureNote(
            text = "Save history and favourites later — no account needed to ride.",
            icon = NavigoIcons.Plus,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            iconContainerShape = MaterialTheme.shapes.small,
            iconContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        )
    }
}
