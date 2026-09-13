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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIconButton
import dev.lscythe.app.navigo.core.designsystem.generated.resources.Res
import dev.lscythe.app.navigo.core.designsystem.generated.resources.navigation_back
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.action.Close
import dev.lscythe.app.navigo.core.designsystem.icon.action.ExternalLink
import dev.lscythe.app.navigo.core.designsystem.icon.navigation.ArrowLeft
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import org.jetbrains.compose.resources.stringResource

/**
 * Displays a title and optional subtitle between caller-provided navigation and action content.
 *
 * @param title the primary app bar text
 * @param navigationContent the content displayed before the title
 * @param modifier the [Modifier] to apply to this app bar
 * @param subtitle the optional secondary text below the title
 * @param actionContent the optional content displayed after the title
 * @param containerColor the background color of this app bar
 * @param contentColor the preferred color for the title and slot content
 * @param subtitleColor the color of the subtitle
 */
@Composable
fun NavigoTopAppBar(
    title: String,
    navigationContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionContent: @Composable RowScope.() -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerColor,
        contentColor = contentColor,
    ) {
        Row(
            modifier =
                Modifier.defaultMinSize(minHeight = TopAppBarMinHeight)
                    .padding(horizontal = NavigoSpacing.container),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                navigationContent()
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = NavigoSpacing.element)) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                )
                subtitle?.let {
                    Text(
                        text = it,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = subtitleColor,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                actionContent()
            }
        }
    }
}

/**
 * Displays a title and optional subtitle with a built-in uncontained back button.
 *
 * @param title the primary app bar text
 * @param onNavigateBack the callback invoked when the back button is clicked
 * @param modifier the [Modifier] to apply to this app bar
 * @param subtitle the optional secondary text below the title
 * @param actionContent the optional content displayed after the title
 * @param containerColor the background color of this app bar
 * @param contentColor the preferred color for the title and slot content
 * @param subtitleColor the color of the subtitle
 */
@Composable
fun NavigoTopAppBar(
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionContent: @Composable RowScope.() -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    NavigoTopAppBar(
        title = title,
        navigationContent = {
            NavigoIconButton(onClick = onNavigateBack) {
                NavigoIcon(
                    imageVector = NavigoIcons.ArrowLeft,
                    contentDescription = stringResource(Res.string.navigation_back),
                )
            }
        },
        modifier = modifier,
        subtitle = subtitle,
        actionContent = actionContent,
        containerColor = containerColor,
        contentColor = contentColor,
        subtitleColor = subtitleColor,
    )
}

@Composable
internal fun TopAppBarPreviewContent() {
    Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
        NavigoTopAppBar(title = "Theme", onNavigateBack = {})
        NavigoTopAppBar(
            title = "Your reports",
            subtitle = "23 sent · 2 waiting on riders",
            onNavigateBack = {},
            actionContent = { Text("Edit", style = MaterialTheme.typography.labelLarge) },
        )
        NavigoTopAppBar(
            title = "Jakarta Transport Open Data",
            subtitle = "data.jakarta.go.id",
            navigationContent = {
                NavigoIconButton(onClick = {}) {
                    NavigoIcon(NavigoIcons.Close, contentDescription = "Close")
                }
            },
            actionContent = {
                NavigoIconButton(onClick = {}) {
                    NavigoIcon(
                        NavigoIcons.ExternalLink,
                        contentDescription = "Open externally",
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    }
}

private val TopAppBarMinHeight = 72.dp
