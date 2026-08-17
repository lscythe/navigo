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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview

/**
 * Displays a single-line text field with a persistent label and bottom indicator.
 *
 * @param value the text displayed in this field
 * @param onValueChange called when the text changes
 * @param label the label displayed above this field
 * @param modifier the [Modifier] to apply to this field
 * @param helperText supporting text displayed below this field
 * @param maxLength the character limit shown by the counter, or `null` for no counter
 * @param minHeight the minimum height of the input area
 * @param enabled whether this field responds to user input
 * @param readOnly whether the text can be changed
 * @param isError whether this field should indicate an error
 * @param textStyle the style of the input text
 * @param labelStyle the style of the label
 * @param keyboardOptions software keyboard options for this field
 * @param keyboardActions actions triggered by the software keyboard
 * @param visualTransformation transforms the text displayed in this field
 * @param focusedIndicatorColor the indicator color when focused
 * @param unfocusedIndicatorColor the indicator color when unfocused
 * @param errorColor the color used when this field is in error
 * @param disabledColor the color used when this field is disabled
 */
@Composable
fun NavigoUnderlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    helperText: String? = null,
    maxLength: Int? = null,
    minHeight: Dp = 56.dp,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    labelStyle: TextStyle = MaterialTheme.typography.labelSmall,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    focusedIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor: Color = MaterialTheme.colorScheme.outline,
    errorColor: Color = MaterialTheme.colorScheme.error,
    disabledColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
) {
    require(maxLength == null || maxLength >= 0) { "maxLength must be non-negative" }
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val fieldIsError = isError || (maxLength != null && value.length > maxLength)
    val indicatorColor =
        when {
            !enabled -> disabledColor
            fieldIsError -> errorColor
            focused -> focusedIndicatorColor
            else -> unfocusedIndicatorColor
        }
    val contentColor = if (enabled) MaterialTheme.colorScheme.onSurface else disabledColor
    val supportingColor =
        if (fieldIsError) errorColor else MaterialTheme.colorScheme.onSurfaceVariant

    Column(modifier = modifier) {
        Text(
            text = label,
            color = if (fieldIsError) errorColor else contentColor,
            style = labelStyle,
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.copy(color = contentColor),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(if (fieldIsError) errorColor else focusedIndicatorColor),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth().heightIn(min = minHeight),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    innerTextField()
                }
            },
        )
        Canvas(modifier = Modifier.fillMaxWidth().height(if (focused) 2.dp else 1.dp)) {
            drawRect(indicatorColor)
        }
        if (helperText != null || maxLength != null) {
            Row(modifier = Modifier.fillMaxWidth()) {
                if (helperText != null) {
                    Text(
                        text = helperText,
                        modifier = Modifier.weight(1f),
                        color = supportingColor,
                        style = MaterialTheme.typography.bodySmall,
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                if (maxLength != null) {
                    Text(
                        text = "${value.length}/$maxLength",
                        color = supportingColor,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoUnderlinedTextFieldPreview() {
    NavigoPreview {
        UnderlinedTextFieldPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoUnderlinedTextFieldMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        UnderlinedTextFieldPreviewContent()
    }
}

@Composable
private fun UnderlinedTextFieldPreviewContent() {
    NavigoUnderlinedTextField(
        value = "Ayu K.",
        onValueChange = {},
        label = "DISPLAY NAME",
        modifier = Modifier.fillMaxWidth(),
        maxLength = 40,
    )
}
