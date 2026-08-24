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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import dev.lscythe.app.navigo.core.designsystem.icon.status.ExclamationCircle
import dev.lscythe.app.navigo.core.designsystem.icon.status.Notification
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays alert metadata, a title, and optional description in a card.
 *
 * @param source the source of this alert
 * @param category the category of this alert
 * @param title the alert title
 * @param description the optional alert description
 * @param timestamp the time associated with this alert
 * @param icon the icon displayed before the alert metadata
 * @param modifier the [Modifier] to apply to this card
 * @param onClick called when this card is clicked, or `null` for a non-interactive card
 * @param shape the shape of this card
 * @param containerColor the background color of this card
 * @param contentColor the color of this card's content
 * @param border the border drawn around this card
 * @param contentPadding the space around this card's content
 * @param iconSize the width and height of the icon
 * @param metadataStyle the style of the source and category
 * @param timestampStyle the style of the timestamp
 * @param titleStyle the style of the title
 * @param descriptionStyle the style of the description
 */
@Composable
fun NavigoAlertCard(
    source: String,
    category: String,
    title: String,
    description: String = "",
    timestamp: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.primaryFixed,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryFixed,
    border: BorderStroke? = null,
    contentPadding: Dp = NavigoSpacing.container,
    iconSize: Dp = 24.dp,
    metadataStyle: TextStyle = MaterialTheme.typography.labelLarge,
    timestampStyle: TextStyle = MaterialTheme.typography.labelSmall,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    descriptionStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val content: @Composable () -> Unit = {
        AlertCardContent(
            source,
            category,
            title,
            description,
            timestamp,
            icon,
            contentPadding,
            iconSize,
            metadataStyle,
            timestampStyle,
            titleStyle,
            descriptionStyle,
        )
    }
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            border = border,
            content = content,
        )
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            border = border,
            content = content,
        )
    }
}

@Composable
private fun AlertCardContent(
    source: String,
    category: String,
    title: String,
    description: String,
    timestamp: String,
    icon: ImageVector,
    contentPadding: Dp,
    iconSize: Dp,
    metadataStyle: TextStyle,
    timestampStyle: TextStyle,
    titleStyle: TextStyle,
    descriptionStyle: TextStyle,
) {
    Column(
        Modifier.padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(iconSize))
            Text("$source · $category", Modifier.weight(1f), style = metadataStyle, maxLines = 1)
            Text(timestamp, style = timestampStyle, maxLines = 1)
        }
        Text(title, style = titleStyle)
        if (description.isNotEmpty()) Text(description, style = descriptionStyle)
    }
}

@NavigoThemePreview
@Composable
private fun NavigoAlertCardPreview() {
    NavigoPreview {
        AlertCardPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoAlertCardMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        AlertCardPreviewContent()
    }
}

@Composable
private fun AlertCardPreviewContent() {
    Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.container)) {
        NavigoAlertCard(
            source = "NAVIGO",
            category = "STOP ALARM",
            title = "Get off at Jalan Melati — next stop",
            timestamp = "now",
            icon = NavigoIcons.Notification,
            modifier = Modifier.fillMaxWidth(),
        )
        NavigoAlertCard(
            source = "OPERATOR",
            category = "DIVERSION",
            title = "Line 14 skipping Pasar Baru until 18:00",
            timestamp = "14:02",
            icon = NavigoIcons.ExclamationCircle,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        )
    }
}
