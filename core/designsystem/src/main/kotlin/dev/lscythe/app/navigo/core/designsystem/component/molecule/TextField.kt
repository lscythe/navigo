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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays an outlined text field with optional helper text and character counter.
 *
 * @param value the text displayed in this field
 * @param onValueChange called when the text changes
 * @param modifier the [Modifier] to apply to this field
 * @param helperText supporting text displayed below this field
 * @param maxLength the character limit shown by the counter, or `null` for no counter
 * @param minHeight the minimum height of this field
 * @param minLines the minimum number of visible text lines
 * @param maxLines the maximum number of visible text lines
 * @param enabled whether this field responds to user input
 * @param readOnly whether the text can be changed
 * @param isError whether this field should indicate an error
 * @param textStyle the style of the input text
 * @param placeholder the optional placeholder content
 * @param leadingIcon the optional leading icon
 * @param trailingIcon the optional trailing icon
 * @param keyboardOptions software keyboard options for this field
 * @param keyboardActions actions triggered by the software keyboard
 * @param visualTransformation transforms the text displayed in this field
 * @param shape the shape of this field
 * @param colors the colors used in different states
 */
@Composable
fun NavigoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    helperText: String? = null,
    maxLength: Int? = null,
    minHeight: Dp = OutlinedTextFieldDefaults.MinHeight,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    require(maxLength == null || maxLength >= 0) { "maxLength must be non-negative" }
    require(minLines > 0) { "minLines must be greater than zero" }
    require(maxLines >= minLines) { "maxLines must be at least minLines" }

    val hasSupportingContent = helperText != null || maxLength != null
    val fieldIsError = isError || (maxLength != null && value.length > maxLength)
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = fieldIsError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = maxLines == 1,
            minLines = minLines,
            maxLines = maxLines,
            shape = shape,
            colors = colors,
        )
        if (hasSupportingContent) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                if (helperText != null) {
                    Text(
                        text = helperText,
                        modifier = Modifier.weight(1f),
                        color =
                            if (fieldIsError) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                if (maxLength != null) {
                    Text(
                        text = "${value.length}/$maxLength",
                        color =
                            if (fieldIsError) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoTextFieldPreview() {
    NavigoPreview {
        TextFieldPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoTextFieldMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        TextFieldPreviewContent()
    }
}

@Composable
private fun TextFieldPreviewContent() {
    var address by remember { mutableStateOf("Jalan Melati") }
    var notes by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.cardPadding)) {
        NavigoTextField(
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
        )
        NavigoTextField(
            value = notes,
            onValueChange = { notes = it },
            helperText = "Add delivery instructions",
            maxLength = 120,
            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
