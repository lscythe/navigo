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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Groups custom list rows in a shared rounded container with dividers.
 *
 * @param itemCount the number of rows in this container
 * @param modifier the [Modifier] to apply to this container
 * @param shape the shape of this container
 * @param containerColor the background color of this container
 * @param contentColor the preferred color of row content
 * @param dividerColor the color of dividers between rows
 * @param dividerThickness the thickness of dividers between rows
 * @param itemContent the content displayed for each row index
 */
@Composable
fun NavigoListContainer(
    itemCount: Int,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    dividerColor: Color = MaterialTheme.colorScheme.outlineVariant,
    dividerThickness: Dp = 1.dp,
    itemContent: @Composable (index: Int) -> Unit,
) {
    require(itemCount >= 0) { "itemCount must be non-negative" }

    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Column {
            repeat(itemCount) { index ->
                itemContent(index)
                if (index < itemCount - 1) {
                    HorizontalDivider(
                        thickness = dividerThickness,
                        color = dividerColor,
                    )
                }
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoListContainerPreview() {
    NavigoPreview {
        ListContainerPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoListContainerMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        ListContainerPreviewContent()
    }
}

@Composable
private fun ListContainerPreviewContent() {
    val labels =
        listOf(
            "Names or accuses a specific person",
            "Not true, I was there",
            "Nothing to do with this stop",
        )
    NavigoListContainer(
        itemCount = labels.size,
        modifier = Modifier.fillMaxWidth(),
    ) { index ->
        Row(modifier = Modifier.fillMaxWidth().padding(NavigoSpacing.container)) {
            Text(labels[index])
        }
    }
}
