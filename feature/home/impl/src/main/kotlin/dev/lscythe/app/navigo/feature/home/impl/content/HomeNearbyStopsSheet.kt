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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoAvatar
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoOutlinedButton
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoChoiceChip
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.action.Plus
import dev.lscythe.app.navigo.core.designsystem.icon.map.MapPin
import dev.lscythe.app.navigo.core.designsystem.icon.navigation.ArrowLeft
import dev.lscythe.app.navigo.core.designsystem.icon.navigation.ChevronUp
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

@Composable
internal fun HomeNearbyStopsSheet(
    value: NearbyStopsSheetValue,
    onValueChange: (NearbyStopsSheetValue) -> Unit,
    cornerRadiusFraction: Float = if (value == NearbyStopsSheetValue.Expanded) 0f else 1f,
    modifier: Modifier = Modifier,
) {
    val cornerRadius = 28.dp * cornerRadiusFraction.coerceIn(0f, 1f)
    Surface(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius),
        color = MaterialTheme.colorScheme.surface,
    ) {
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                fadeIn(tween(durationMillis = 160, delayMillis = 120))
                    .togetherWith(fadeOut(tween(durationMillis = 120)))
            },
            label = "nearbyStopsSheetContent",
        ) { current ->
            when (current) {
                NearbyStopsSheetValue.Collapsed ->
                    CollapsedNearbyStops(
                        onExpand = { onValueChange(NearbyStopsSheetValue.HalfExpanded) }
                    )
                NearbyStopsSheetValue.HalfExpanded -> HalfExpandedNearbyStops()
                NearbyStopsSheetValue.Expanded ->
                    ExpandedNearbyStops(
                        onBack = { onValueChange(NearbyStopsSheetValue.HalfExpanded) }
                    )
            }
        }
    }
}

@Composable
private fun CollapsedNearbyStops(onExpand: () -> Unit) {
    Column(
        modifier =
            Modifier.fillMaxWidth()
                .clickable(onClick = onExpand)
                .padding(horizontal = NavigoSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.screen),
    ) {
        SheetDragHandle()
        SheetTitle(updatedOnly = true)
        NearbyStopRow(nearbyStops.first(), compact = false)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                "3 more stops",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            NavigoIcon(NavigoIcons.ChevronUp, contentDescription = null, size = 20.dp)
        }
    }
}

@Composable
private fun HalfExpandedNearbyStops() {
    Column(Modifier.fillMaxSize().padding(horizontal = NavigoSpacing.screen)) {
        SheetDragHandle()
        SheetTitle(updatedOnly = true)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        nearbyStops.take(2).forEach { stop ->
            NearbyStopRow(stop, modifier = Modifier.padding(vertical = NavigoSpacing.item))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
        Text(
            "YOUR USUAL RUNS",
            modifier = Modifier.padding(top = NavigoSpacing.screen, bottom = NavigoSpacing.item),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item)) {
            usualRuns.forEach { run -> UsualRunCard(run, Modifier.weight(1f)) }
        }
    }
}

@Composable
private fun ExpandedNearbyStops(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(NavigoSpacing.screen),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
        ) {
            NavigoOutlinedButton(
                onClick = onBack,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
            ) {
                NavigoIcon(NavigoIcons.ArrowLeft, contentDescription = "Back", size = 24.dp)
            }
            Column(Modifier.weight(1f)) {
                Text("Stops near you", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "14 stops · updated 40s ago",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            NavigoOutlinedButton(onClick = {}) { Text("Nearest") }
        }
        Row(
            Modifier.padding(horizontal = NavigoSpacing.screen),
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
        ) {
            NavigoChoiceChip(selected = true, onClick = {}, label = "All lines")
            NavigoChoiceChip(selected = false, onClick = {}, label = "Live only")
            NavigoChoiceChip(selected = false, onClick = {}, label = "Seats free")
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = NavigoSpacing.screen),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        LazyColumn(Modifier.weight(1f)) {
            items(nearbyStops) { stop ->
                NearbyStopRow(
                    stop,
                    modifier =
                        Modifier.padding(
                            horizontal = NavigoSpacing.screen,
                            vertical = NavigoSpacing.item,
                        ),
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(NavigoSpacing.screen),
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
        ) {
            NavigoButton(onClick = {}, modifier = Modifier.weight(1f)) {
                NavigoIcon(NavigoIcons.Plus, contentDescription = null, size = 22.dp)
                Text("Report a bus", modifier = Modifier.padding(start = 8.dp))
            }
            NavigoOutlinedButton(onClick = {}) {
                NavigoIcon(NavigoIcons.MapPin, contentDescription = "Open map", size = 24.dp)
            }
        }
    }
}

@Composable
private fun SheetDragHandle() {
    Box(Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
        Box(
            Modifier.size(width = 40.dp, height = 4.dp)
                .background(MaterialTheme.colorScheme.onSurfaceVariant, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
private fun SheetTitle(updatedOnly: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Stops near you", style = MaterialTheme.typography.headlineMedium)
        Text(
            "updated 40s ago",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun NearbyStopRow(
    stop: NearbyStop,
    modifier: Modifier = Modifier,
    compact: Boolean = true,
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
    ) {
        NavigoAvatar(
            text = stop.route,
            size = if (compact) 52.dp else 60.dp,
            containerColor =
                if (stop.accent == StopAccent.Dark) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary,
            contentColor =
                if (stop.accent == StopAccent.Dark) MaterialTheme.colorScheme.primaryFixed
                else MaterialTheme.colorScheme.onSecondary,
        )
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "${stop.name} · ${stop.distance}",
                style = MaterialTheme.typography.titleLarge,
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
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color =
                            if (stop.status == "Seats") MaterialTheme.colorScheme.onPrimaryFixed
                            else MaterialTheme.colorScheme.primaryFixed,
                        style = MaterialTheme.typography.labelLarge,
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
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun UsualRunCard(run: UsualRun, modifier: Modifier = Modifier) {
    val background =
        if (run.emphasized) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceContainer
    val foreground =
        if (run.emphasized) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurface
    Surface(modifier = modifier, shape = MaterialTheme.shapes.extraLarge, color = background) {
        Column(
            Modifier.padding(NavigoSpacing.screen),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(run.label, style = MaterialTheme.typography.titleMedium, color = foreground)
            Text(
                run.route,
                style = MaterialTheme.typography.headlineLarge,
                color =
                    if (run.emphasized) MaterialTheme.colorScheme.primaryFixed
                    else MaterialTheme.colorScheme.secondary,
            )
            Text(run.duration, style = MaterialTheme.typography.titleMedium, color = foreground)
        }
    }
}
