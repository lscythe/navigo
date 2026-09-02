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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.action.Close
import dev.lscythe.app.navigo.core.designsystem.icon.action.Search
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview

/**
 * Displays a single-line search field with search and clear actions.
 *
 * @param query the search text displayed in this field
 * @param onQueryChange called when the search text changes
 * @param onSearch called with the current query when the keyboard search action is triggered
 * @param onClear called when the clear button is clicked
 * @param modifier the [Modifier] to apply to this search bar
 * @param placeholder text displayed when the query is empty
 * @param enabled whether this search bar responds to user input
 * @param readOnly whether the query can be changed
 * @param shape the shape of this search bar
 * @param minHeight the minimum height of this search bar
 * @param containerColor the background color of this search bar
 * @param contentColor the color of the query and search icon
 * @param placeholderColor the color of the placeholder
 * @param clearContainerColor the background color of the clear button
 * @param clearContentColor the color of the clear icon
 * @param textStyle the style of the query and placeholder
 */
@Composable
fun NavigoSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    shape: Shape = MaterialTheme.shapes.large,
    minHeight: Dp = 56.dp,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    clearContainerColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    clearContentColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    val resolvedContentColor = if (enabled) contentColor else contentColor.copy(alpha = 0.38f)
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .clip(shape)
                .background(containerColor),
        enabled = enabled,
        readOnly = readOnly,
        singleLine = true,
        textStyle = textStyle.copy(color = resolvedContentColor),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
        interactionSource = interactionSource,
        cursorBrush = SolidColor(contentColor),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = NavigoIcons.Search,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = resolvedContentColor,
                )
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (query.isEmpty()) {
                        Text(
                            text = placeholder,
                            color =
                                if (enabled) placeholderColor
                                else placeholderColor.copy(alpha = 0.38f),
                            style = textStyle,
                            maxLines = 1,
                        )
                    }
                    innerTextField()
                }
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = onClear,
                        enabled = enabled && !readOnly,
                        colors =
                            IconButtonDefaults.iconButtonColors(
                                contentColor = clearContentColor,
                                disabledContentColor = clearContentColor.copy(alpha = 0.38f),
                            ),
                    ) {
                        Box(
                            modifier =
                                Modifier.size(28.dp).background(clearContainerColor, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = NavigoIcons.Close,
                                contentDescription = "Clear search",
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                }
            }
        },
    )
}

@NavigoThemePreview
@Composable
private fun NavigoSearchBarPreview() {
    NavigoPreview {
        SearchBarPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoSearchBarMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        SearchBarPreviewContent()
    }
}

@Composable
private fun SearchBarPreviewContent() {
    var query by remember { mutableStateOf("") }
    NavigoSearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = {},
        onClear = { query = "" },
        placeholder = "Search a stop or street",
    )
}
