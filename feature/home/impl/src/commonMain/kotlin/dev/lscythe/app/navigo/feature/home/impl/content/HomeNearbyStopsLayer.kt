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

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
internal fun HomeNearbyStopsLayer(modifier: Modifier = Modifier) {
    val state = remember { AnchoredDraggableState(NearbyStopsSheetValue.Collapsed) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val cornerRadiusFraction by
        remember(state) {
            derivedStateOf {
                val halfOffset = state.anchors.positionOf(NearbyStopsSheetValue.HalfExpanded)
                val offset = state.offset
                if (!offset.isFinite() || halfOffset.isNaN() || halfOffset <= 0f) {
                    1f
                } else {
                    (offset / (halfOffset * 0.1f)).coerceIn(0f, 1f)
                }
            }
        }

    val nestedScrollConnection =
        remember(state) {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (source != NestedScrollSource.UserInput || available.y >= 0f)
                        return Offset.Zero
                    return Offset(0f, state.dispatchRawDelta(available.y))
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset {
                    if (source != NestedScrollSource.UserInput || available.y <= 0f)
                        return Offset.Zero
                    return Offset(0f, state.dispatchRawDelta(available.y))
                }
            }
        }
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val screenHeight = constraints.maxHeight.toFloat()
        val collapsedHeight = with(density) { 250.dp.toPx() }
        LaunchedEffect(screenHeight, collapsedHeight) {
            val positions = nearbyStopsSheetAnchors(screenHeight, collapsedHeight)
            state.updateAnchors(
                DraggableAnchors {
                    NearbyStopsSheetValue.Collapsed at
                        positions.getValue(NearbyStopsSheetValue.Collapsed)
                    NearbyStopsSheetValue.HalfExpanded at
                        positions.getValue(NearbyStopsSheetValue.HalfExpanded)
                    NearbyStopsSheetValue.Expanded at
                        positions.getValue(NearbyStopsSheetValue.Expanded)
                }
            )
        }

        HomeNearbyStopsSheet(
            value = state.currentValue,
            cornerRadiusFraction = cornerRadiusFraction,
            onValueChange = { target -> scope.launch { state.animateTo(target) } },
            modifier =
                Modifier.fillMaxSize()
                    .graphicsLayer {
                        translationY = state.offset.takeIf(Float::isFinite) ?: screenHeight
                    }
                    .nestedScroll(nestedScrollConnection)
                    .anchoredDraggable(state, Orientation.Vertical),
        )
    }
}
