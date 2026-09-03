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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

@Composable
fun NavigoElevatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.elevatedButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    ElevatedButton(
        onClick,
        modifier,
        enabled,
        shape,
        colors = colors,
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun NavigoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick,
        modifier,
        enabled,
        shape,
        colors = colors,
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun NavigoFilledTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    FilledTonalButton(
        onClick,
        modifier,
        enabled,
        shape,
        colors = colors,
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun NavigoOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(enabled),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick,
        modifier,
        enabled,
        shape,
        colors = colors,
        border = border,
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun NavigoTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick,
        modifier,
        enabled,
        shape,
        colors = colors,
        contentPadding = contentPadding,
        content = content,
    )
}

@NavigoThemePreview
@Composable
private fun NavigoButtonPreview() = NavigoPreview { ButtonPreviewContent() }

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoButtonMaterialKolorPreview() = NavigoMaterialKolorPreview {
    ButtonPreviewContent()
}

@Composable
private fun ButtonPreviewContent() {
    Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
        Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
            NavigoElevatedButton(onClick = {}) { Text("Elevated") }
            NavigoButton(onClick = {}) { Text("Filled") }
            NavigoFilledTonalButton(onClick = {}) { Text("Tonal") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
            NavigoOutlinedButton(onClick = {}) { Text("Outlined") }
            NavigoTextButton(onClick = {}) { Text("Text") }
            NavigoButton(onClick = {}, enabled = false) { Text("Disabled") }
        }
    }
}
