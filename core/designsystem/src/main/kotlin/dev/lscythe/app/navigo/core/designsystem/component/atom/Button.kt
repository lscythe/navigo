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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.status.Circle
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays an elevated button for a prominent action requiring visual separation.
 *
 * @param onClick called when this button is clicked
 * @param modifier the [Modifier] to apply to this button
 * @param enabled whether this button responds to user input
 * @param shape the shape of this button
 * @param colors the colors used in different states
 * @param content the content displayed in this button
 */
@Composable
fun NavigoElevatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.elevatedButtonColors(),
    content: @Composable RowScope.() -> Unit,
) {
    ElevatedButton(onClick, modifier, enabled, shape, colors = colors, content = content)
}

/**
 * Displays a filled button for a high-emphasis action.
 *
 * @param onClick called when this button is clicked
 * @param modifier the [Modifier] to apply to this button
 * @param enabled whether this button responds to user input
 * @param shape the shape of this button
 * @param colors the colors used in different states
 * @param content the content displayed in this button
 */
@Composable
fun NavigoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable RowScope.() -> Unit,
) {
    Button(onClick, modifier, enabled, shape, colors = colors, content = content)
}

/**
 * Displays a filled tonal button for a medium-emphasis action.
 *
 * @param onClick called when this button is clicked
 * @param modifier the [Modifier] to apply to this button
 * @param enabled whether this button responds to user input
 * @param shape the shape of this button
 * @param colors the colors used in different states
 * @param content the content displayed in this button
 */
@Composable
fun NavigoFilledTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    content: @Composable RowScope.() -> Unit,
) {
    FilledTonalButton(onClick, modifier, enabled, shape, colors = colors, content = content)
}

/**
 * Displays an outlined button for a medium-emphasis action.
 *
 * @param onClick called when this button is clicked
 * @param modifier the [Modifier] to apply to this button
 * @param enabled whether this button responds to user input
 * @param shape the shape of this button
 * @param colors the colors used in different states
 * @param border the border drawn around this button
 * @param content the content displayed in this button
 */
@Composable
fun NavigoOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(enabled),
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick,
        modifier,
        enabled,
        shape,
        colors = colors,
        border = border,
        content = content,
    )
}

/**
 * Displays a text button for a low-emphasis action.
 *
 * @param onClick called when this button is clicked
 * @param modifier the [Modifier] to apply to this button
 * @param enabled whether this button responds to user input
 * @param shape the shape of this button
 * @param colors the colors used in different states
 * @param content the content displayed in this button
 */
@Composable
fun NavigoTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(onClick, modifier, enabled, shape, colors = colors, content = content)
}

/** Displays an icon button. */
@Composable
fun NavigoIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    content: @Composable () -> Unit,
) {
    IconButton(onClick, modifier, enabled, colors = colors, content = content)
}

/** Displays a filled icon button for a high-emphasis action. */
@Composable
fun NavigoFilledIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(),
    content: @Composable () -> Unit,
) {
    FilledIconButton(onClick, modifier, enabled, shape, colors, content = content)
}

/** Displays a filled tonal icon button for a medium-emphasis action. */
@Composable
fun NavigoFilledTonalIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: IconButtonColors = IconButtonDefaults.filledTonalIconButtonColors(),
    content: @Composable () -> Unit,
) {
    FilledTonalIconButton(onClick, modifier, enabled, shape, colors, content = content)
}

/** Displays an outlined icon button for a medium-emphasis action. */
@Composable
fun NavigoOutlinedIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: IconButtonColors = IconButtonDefaults.outlinedIconButtonColors(),
    border: BorderStroke? = IconButtonDefaults.outlinedIconButtonBorder(enabled),
    content: @Composable () -> Unit,
) {
    OutlinedIconButton(onClick, modifier, enabled, shape, colors, border, content = content)
}

@NavigoThemePreview
@Composable
private fun NavigoButtonPreview() {
    NavigoPreview { ButtonPreviewContent() }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoButtonMaterialKolorPreview() {
    NavigoMaterialKolorPreview { ButtonPreviewContent() }
}

@Composable
private fun ButtonPreviewContent() {
    Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.chipGap)) {
        Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.chipGap)) {
            NavigoElevatedButton(onClick = {}) { Text("Elevated") }
            NavigoButton(onClick = {}) { Text("Filled") }
            NavigoFilledTonalButton(onClick = {}) { Text("Tonal") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.chipGap)) {
            NavigoOutlinedButton(onClick = {}) { Text("Outlined") }
            NavigoTextButton(onClick = {}) { Text("Text") }
            NavigoButton(onClick = {}, enabled = false) { Text("Disabled") }
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoIconButtonPreview() {
    NavigoPreview { IconButtonPreviewContent() }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoIconButtonMaterialKolorPreview() {
    NavigoMaterialKolorPreview { IconButtonPreviewContent() }
}

@Composable
private fun IconButtonPreviewContent() {
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.chipGap)) {
        NavigoIconButton(onClick = {}) { PreviewIcon() }
        NavigoFilledIconButton(onClick = {}) { PreviewIcon() }
        NavigoFilledTonalIconButton(onClick = {}) { PreviewIcon() }
        NavigoOutlinedIconButton(onClick = {}) { PreviewIcon() }
        NavigoFilledIconButton(onClick = {}, enabled = false) { PreviewIcon() }
    }
}

@Composable
private fun PreviewIcon() {
    Icon(imageVector = NavigoIcons.Circle, contentDescription = null)
}
