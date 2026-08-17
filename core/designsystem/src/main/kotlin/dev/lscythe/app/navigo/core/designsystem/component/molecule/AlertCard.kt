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
 * Displays alert metadata and a prominent message in a card.
 *
 * @param source the source of this alert
 * @param category the category of this alert
 * @param message the alert message
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
 * @param messageStyle the style of the message
 */
@Composable
fun NavigoAlertCard(
    source: String,
    category: String,
    message: String,
    timestamp: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.primaryFixed,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryFixed,
    border: BorderStroke? = null,
    contentPadding: Dp = NavigoSpacing.cardPadding,
    iconSize: Dp = 24.dp,
    metadataStyle: TextStyle = MaterialTheme.typography.labelLarge,
    timestampStyle: TextStyle = MaterialTheme.typography.labelSmall,
    messageStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    val content: @Composable () -> Unit = {
        AlertCardContent(
            source = source,
            category = category,
            message = message,
            timestamp = timestamp,
            icon = icon,
            contentPadding = contentPadding,
            iconSize = iconSize,
            metadataStyle = metadataStyle,
            timestampStyle = timestampStyle,
            messageStyle = messageStyle,
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
    message: String,
    timestamp: String,
    icon: ImageVector,
    contentPadding: Dp,
    iconSize: Dp,
    metadataStyle: TextStyle,
    timestampStyle: TextStyle,
    messageStyle: TextStyle,
) {
    Column(
        modifier = Modifier.padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.chipGap),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.chipGap),
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(iconSize))
            Text(
                text = "$source · $category",
                modifier = Modifier.weight(1f),
                style = metadataStyle,
                maxLines = 1,
            )
            Text(text = timestamp, style = timestampStyle, maxLines = 1)
        }
        Text(text = message, style = messageStyle)
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
    Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.cardPadding)) {
        NavigoAlertCard(
            source = "NAVIGO",
            category = "STOP ALARM",
            message = "Get off at Jalan Melati — next stop",
            timestamp = "now",
            icon = NavigoIcons.Notification,
            modifier = Modifier.fillMaxWidth(),
        )
        NavigoAlertCard(
            source = "OPERATOR",
            category = "DIVERSION",
            message = "Line 14 skipping Pasar Baru until 18:00",
            timestamp = "14:02",
            icon = NavigoIcons.ExclamationCircle,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        )
    }
}
