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
package dev.lscythe.app.navigo.feature.home.impl.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.status.SocketConnected
import dev.lscythe.app.navigo.core.designsystem.icon.status.SocketDisconnected
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.resources.generated.resources.Res
import dev.lscythe.app.navigo.core.resources.generated.resources.home_bus_data_connected
import dev.lscythe.app.navigo.core.resources.generated.resources.home_bus_data_disconnected
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

/**
 * Socket status chip displayed below the search bar on the left.
 *
 * Expands to the right when disconnected to show the error message and stays expanded. When
 * connected, displays in green and shows "Bus Data Connected" briefly before smoothly collapsing
 * back into an icon-only chip.
 *
 * @param isConnected whether the bus telemetry socket is connected
 * @param modifier the [Modifier] to apply to this chip
 * @param iconSize the size of the status icon
 * @param horizontalPadding horizontal inner padding of the chip
 * @param verticalPadding vertical inner padding of the chip
 * @param textStyle typography style for the status message
 * @param collapseDelayMs delay before auto-collapsing when connected
 * @param onClick optional callback when user taps the chip
 */
@Composable
fun HomeSocketStatusChip(
    isConnected: Boolean,
    modifier: Modifier = Modifier,
    iconSize: Dp = 16.dp,
    horizontalPadding: Dp = NavigoSpacing.item,
    verticalPadding: Dp = NavigoSpacing.element,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
    collapseDelayMs: Long = 2500L,
    onClick: (() -> Unit)? = null,
) {
    var isConnectedExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            isConnectedExpanded = true
            delay(collapseDelayMs.milliseconds)
            isConnectedExpanded = false
        }
    }

    val isExpanded = !isConnected || isConnectedExpanded

    val targetContainerColor =
        if (isConnected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.errorContainer
        }

    val targetContentColor =
        if (isConnected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onErrorContainer
        }

    val containerColor by
        animateColorAsState(
            targetValue = targetContainerColor,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
            label = "chipContainerColor",
        )
    val contentColor by
        animateColorAsState(
            targetValue = targetContentColor,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
            label = "chipContentColor",
        )

    val statusText =
        if (isConnected) {
            stringResource(Res.string.home_bus_data_connected)
        } else {
            stringResource(Res.string.home_bus_data_disconnected)
        }

    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = NavigoSpacing.element,
    ) {
        Row(
            modifier =
                Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        role = Role.Button,
                        onClick = {
                            if (onClick != null) {
                                onClick()
                            } else if (isConnected) {
                                isConnectedExpanded = !isConnectedExpanded
                            }
                        },
                    )
                    .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        ) {
            NavigoIcon(
                imageVector =
                    if (isConnected) {
                        NavigoIcons.SocketConnected
                    } else {
                        NavigoIcons.SocketDisconnected
                    },
                contentDescription = statusText,
                size = iconSize,
                tint = contentColor,
            )
            AnimatedVisibility(
                visible = isExpanded,
                enter =
                    expandHorizontally(
                        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
                        expandFrom = Alignment.Start,
                    ) + fadeIn(animationSpec = tween(durationMillis = 150, delayMillis = 50)),
                exit =
                    shrinkHorizontally(
                        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
                        shrinkTowards = Alignment.Start,
                    ) + fadeOut(animationSpec = tween(durationMillis = 100)),
            ) {
                Text(
                    text = statusText,
                    style = textStyle,
                    color = contentColor,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
internal fun HomeSocketStatusChipPreviewContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        horizontalAlignment = Alignment.Start,
    ) {
        HomeSocketStatusChip(isConnected = true)
        HomeSocketStatusChip(isConnected = false)
    }
}
