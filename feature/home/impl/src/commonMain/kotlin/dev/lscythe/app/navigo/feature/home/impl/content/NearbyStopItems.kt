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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoAvatar
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

@Composable
internal fun SheetDragHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier.size(width = 40.dp, height = 4.dp)
                .background(MaterialTheme.colorScheme.onSurfaceVariant, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
internal fun SheetTitle(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Stops near you", style = MaterialTheme.typography.titleLarge)
        Text(
            "updated 40s ago",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
internal fun NearbyStopRow(
    stop: NearbyStop,
    modifier: Modifier = Modifier,
    compact: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
    ) {
        NavigoAvatar(
            text = stop.route,
            size = if (compact) 44.dp else 48.dp,
            containerColor =
                if (stop.accent == StopAccent.Dark) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary,
            contentColor =
                if (stop.accent == StopAccent.Dark) MaterialTheme.colorScheme.primaryFixed
                else MaterialTheme.colorScheme.onSecondary,
            textStyle = if (compact) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleLarge,
        )
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "${stop.name} · ${stop.distance}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color =
                        if (stop.status == "Seats") MaterialTheme.colorScheme.primaryFixed
                        else MaterialTheme.colorScheme.primary,
                ) {
                    Text(
                        stop.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        color =
                            if (stop.status == "Seats") MaterialTheme.colorScheme.onPrimaryFixed
                            else MaterialTheme.colorScheme.primaryFixed,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Text(
                    stop.detail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Text(
            stop.arrival,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
internal fun UsualRunCard(
    run: UsualRun,
    modifier: Modifier = Modifier,
) {
    val background =
        if (run.emphasized) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceContainer
    val foreground =
        if (run.emphasized) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurface
    Surface(modifier = modifier, shape = MaterialTheme.shapes.large, color = background) {
        Column(
            Modifier.padding(horizontal = NavigoSpacing.item, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(run.label, style = MaterialTheme.typography.labelMedium, color = foreground)
            Text(
                run.route,
                style = MaterialTheme.typography.titleLarge,
                color =
                    if (run.emphasized) MaterialTheme.colorScheme.primaryFixed
                    else MaterialTheme.colorScheme.secondary,
            )
            Text(run.duration, style = MaterialTheme.typography.bodySmall, color = foreground)
        }
    }
}
