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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/**
 * Displays the shared Navigo modal sheet shell: a centered drag handle, a large title, optional
 * supporting description, and caller-owned content below.
 *
 * @param title the sheet title
 * @param onDismissRequest called when the user dismisses the sheet
 * @param modifier the [Modifier] to apply to this sheet
 * @param description optional supporting text displayed below the title
 * @param sheetState the state controlling this sheet
 * @param sheetGesturesEnabled whether drag gestures can move or dismiss this sheet
 * @param dragHandle the handle displayed at the top of the sheet, or null for none
 * @param shape the shape of this sheet
 * @param containerColor the background color of this sheet
 * @param contentColor the preferred color of this sheet's content
 * @param headerPadding the padding around the title and description
 * @param content the domain-specific content displayed below the header
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigoModalBottomSheet(
    title: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    sheetState: SheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden),
    sheetGesturesEnabled: Boolean = true,
    dragHandle: (@Composable () -> Unit)? = { SheetDragHandle() },
    shape: Shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    headerPadding: PaddingValues =
        PaddingValues(
            start = NavigoSpacing.screen,
            top = NavigoSpacing.container,
            end = NavigoSpacing.screen,
            bottom = NavigoSpacing.screen,
        ),
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(sheetState) {
        sheetState.show()
    }
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        sheetGesturesEnabled = sheetGesturesEnabled,
        dragHandle = dragHandle,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        Column(modifier = Modifier.padding(headerPadding)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
            )
            if (description != null) {
                Text(
                    text = description,
                    modifier = Modifier.padding(top = NavigoSpacing.item),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        content()
    }
}

@Composable
private fun SheetDragHandle() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier =
                Modifier.padding(top = NavigoSpacing.container)
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = RoundedCornerShape(2.dp),
                    )
        )
    }
}

@NavigoThemePreview
@Composable
private fun NavigoModalBottomSheetPreview() {
    NavigoPreview {
        ModalBottomSheetPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoModalBottomSheetMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        ModalBottomSheetPreviewContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalBottomSheetPreviewContent() {
    var showSheet by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Box(Modifier.padding(padding)) {
            NavigoButton(onClick = { showSheet = true }) {
                Text("Open bottom sheet")
            }

            if (showSheet) {
                NavigoModalBottomSheet(
                    title = "Choose a language",
                    description = "Select the language used throughout Navigo.",
                    onDismissRequest = { showSheet = false },
                ) {
                    Text(
                        text = "English",
                        modifier = Modifier.padding(NavigoSpacing.container),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoModalBottomSheetOpenPreview() {
    NavigoPreview {
        ModalBottomSheetOpenPreviewContent()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoModalBottomSheetOpenMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        ModalBottomSheetOpenPreviewContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalBottomSheetOpenPreviewContent() {
    NavigoModalBottomSheet(
        title = "Text size",
        description = "Affects every screen. Arrival numbers scale with it.",
        onDismissRequest = {},
    ) {
        Text(
            text = "Sample content",
            modifier =
                Modifier.padding(horizontal = NavigoSpacing.screen, vertical = NavigoSpacing.item),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
