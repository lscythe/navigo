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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoChoiceChip
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
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
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = NavigoSpacing.screen),
        ) {
            SheetDragHandle()
            SheetTitle()
            Spacer(Modifier.height(NavigoSpacing.item))
            AnimatedContent(
                targetState = value,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 160, delayMillis = 120))
                        .togetherWith(fadeOut(animationSpec = tween(durationMillis = 120)))
                },
                label = "nearbyStopsSheetContent",
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false),
            ) { current ->
                when (current) {
                    NearbyStopsSheetValue.Collapsed ->
                        CollapsedNearbyStops(
                            onExpand = { onValueChange(NearbyStopsSheetValue.HalfExpanded) }
                        )
                    NearbyStopsSheetValue.HalfExpanded -> HalfExpandedNearbyStops()
                    NearbyStopsSheetValue.Expanded -> ExpandedNearbyStops()
                }
            }
        }
    }
}

@Composable
private fun CollapsedNearbyStops(onExpand: () -> Unit) {
    Column(
        modifier =
            Modifier.fillMaxWidth()
                .padding(top = NavigoSpacing.item)
                .clickable(onClick = onExpand),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
    ) {
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
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = NavigoSpacing.item),
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        nearbyStops.take(2).forEach { stop ->
            NearbyStopRow(stop, modifier = Modifier.padding(vertical = NavigoSpacing.item))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
        Text(
            "YOUR USUAL RUNS",
            modifier = Modifier.padding(top = NavigoSpacing.item, bottom = NavigoSpacing.element),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item)) {
            usualRuns.forEach { run -> UsualRunCard(run, Modifier.weight(1f)) }
        }
    }
}

@Composable
private fun ExpandedNearbyStops() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = NavigoSpacing.item),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        ) {
            NavigoChoiceChip(
                selected = true,
                onClick = {},
                label = "All lines",
                horizontalPadding = NavigoSpacing.item,
                verticalPadding = NavigoSpacing.element,
                labelStyle = MaterialTheme.typography.labelMedium,
            )
            NavigoChoiceChip(
                selected = false,
                onClick = {},
                label = "Live only",
                horizontalPadding = NavigoSpacing.item,
                verticalPadding = NavigoSpacing.element,
                labelStyle = MaterialTheme.typography.labelMedium,
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = NavigoSpacing.item),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        LazyColumn(Modifier.weight(1f)) {
            items(nearbyStops) { stop ->
                NearbyStopRow(
                    stop,
                    modifier =
                        Modifier.padding(
                            vertical = NavigoSpacing.item,
                        ),
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}
