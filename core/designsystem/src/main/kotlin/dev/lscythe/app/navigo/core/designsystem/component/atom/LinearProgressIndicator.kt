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
package dev.lscythe.app.navigo.core.designsystem.component.atom

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * A colored segment in a [NavigoSegmentedLinearProgressIndicator].
 *
 * @param fraction the fraction of the indicator occupied by this segment
 * @param color the color of this segment
 */
@Immutable
data class NavigoProgressSegment(
    val fraction: Float,
    val color: Color,
)

/**
 * Displays linear progress as a sequence of colored segments.
 *
 * @param segments the segments to display, in start-to-end order
 * @param modifier the [Modifier] to apply to this indicator
 * @param trackColor the color of the unfilled track
 * @param height the height of this indicator
 */
@Composable
fun NavigoSegmentedLinearProgressIndicator(
    segments: ImmutableList<NavigoProgressSegment>,
    modifier: Modifier = Modifier,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    height: Dp = 12.dp,
) {
    require(segments.all { it.fraction.isFinite() && it.fraction >= 0f }) {
        "Segment fractions must be finite and non-negative"
    }
    require(segments.sumOf { it.fraction.toDouble() } <= 1.000_001) {
        "Segment fractions must not exceed 1"
    }

    val animatedFractions = segments.mapIndexed { index, segment ->
        val fraction by
            animateFloatAsState(
                targetValue = segment.fraction,
                animationSpec = tween(durationMillis = 400),
                label = "progress segment $index",
            )
        fraction
    }
    val usedFraction = animatedFractions.sum().coerceIn(0f, 1f)

    Canvas(
        modifier =
            modifier.fillMaxWidth().height(height).clip(CircleShape).semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(usedFraction, 0f..1f)
            }
    ) {
        val radius = size.height / 2f
        drawRoundRect(
            color = trackColor,
            size = size,
            cornerRadius = CornerRadius(radius),
        )
        clipRect {
            val lastVisibleSegment = animatedFractions.indexOfLast { it > 0f }
            var startFraction = 0f
            segments.forEachIndexed { index, segment ->
                val segmentFraction = animatedFractions[index].coerceAtLeast(0f)
                val endFraction = (startFraction + segmentFraction).coerceAtMost(1f)
                val left = size.width * startFraction
                val right = size.width * endFraction
                if (right > left) {
                    if (index == lastVisibleSegment) {
                        val capRadius = minOf(size.height / 2f, (right - left) / 2f)
                        clipRect(left = left, right = right) {
                            drawRect(
                                color = segment.color,
                                topLeft = Offset(left, 0f),
                                size =
                                    Size((right - left - capRadius).coerceAtLeast(0f), size.height),
                            )
                            drawCircle(
                                color = segment.color,
                                radius = capRadius,
                                center = Offset(right - capRadius, size.height / 2f),
                            )
                        }
                    } else {
                        drawRect(
                            color = segment.color,
                            topLeft = Offset(left, 0f),
                            size = Size(right - left, size.height),
                        )
                    }
                }
                startFraction = endFraction
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoLinearProgressIndicatorPreview() {
    NavigoPreview {
        Column(verticalArrangement = spacedBy(NavigoSpacing.cardPadding)) {
            NavigoSegmentedLinearProgressIndicator(
                segments =
                    persistentListOf(
                        NavigoProgressSegment(0.55f, MaterialTheme.colorScheme.primary),
                        NavigoProgressSegment(0.08f, MaterialTheme.colorScheme.onPrimary),
                        NavigoProgressSegment(0.12f, MaterialTheme.colorScheme.secondary),
                    )
            )

            val transition = rememberInfiniteTransition(label = "storage preview")
            val offlineFraction by
                transition.animateFloat(
                    initialValue = 0.02f,
                    targetValue = 0.2f,
                    animationSpec =
                        infiniteRepeatable(
                            animation = tween(2_000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse,
                        ),
                    label = "offline storage",
                )
            NavigoSegmentedLinearProgressIndicator(
                segments =
                    persistentListOf(
                        NavigoProgressSegment(0.55f, MaterialTheme.colorScheme.primary),
                        NavigoProgressSegment(0.08f, MaterialTheme.colorScheme.onPrimary),
                        NavigoProgressSegment(offlineFraction, MaterialTheme.colorScheme.secondary),
                    )
            )
        }
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoLinearProgressIndicatorMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        Column(verticalArrangement = spacedBy(NavigoSpacing.cardPadding)) {
            NavigoSegmentedLinearProgressIndicator(
                segments =
                    persistentListOf(
                        NavigoProgressSegment(0.55f, MaterialTheme.colorScheme.primary),
                        NavigoProgressSegment(0.08f, MaterialTheme.colorScheme.onPrimary),
                        NavigoProgressSegment(0.12f, MaterialTheme.colorScheme.secondary),
                    )
            )
            NavigoLinearProgressIndicator(progress = 0.65f)
        }
    }
}

/**
 * Displays determinate linear progress.
 *
 * @param progress the progress to display, from `0f` to `1f`
 * @param modifier the [Modifier] to apply to this indicator
 * @param color the color of the progress indicator
 * @param trackColor the color of the unfilled track
 * @param height the height of this indicator
 */
@Composable
fun NavigoLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    height: Dp = 12.dp,
) {
    require(progress.isFinite()) { "progress must be finite" }
    NavigoSegmentedLinearProgressIndicator(
        segments = persistentListOf(NavigoProgressSegment(progress.coerceIn(0f, 1f), color)),
        modifier = modifier,
        trackColor = trackColor,
        height = height,
    )
}

/**
 * Displays an indeterminate linear loading indicator.
 *
 * @param modifier the [Modifier] to apply to this indicator
 * @param color the color of the loading indicator
 * @param trackColor the color of the track
 * @param height the height of this indicator
 */
@Composable
fun NavigoLinearLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    height: Dp = 12.dp,
) {
    val transition = rememberInfiniteTransition(label = "linear loading")
    val position by
        transition.animateFloat(
            initialValue = -0.35f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 1_200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "loading position",
        )

    Canvas(
        modifier =
            modifier.fillMaxWidth().height(height).clip(CircleShape).semantics {
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            }
    ) {
        val radius = size.height / 2f
        drawRoundRect(
            color = trackColor,
            size = size,
            cornerRadius = CornerRadius(radius),
        )
        val indicatorWidth = size.width * 0.35f
        val left = size.width * position
        clipRect {
            drawRoundRect(
                color = color,
                topLeft = Offset(left, 0f),
                size = Size(indicatorWidth, size.height),
                cornerRadius = CornerRadius(radius),
            )
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoLinearLoadingIndicatorPreview() {
    NavigoPreview {
        NavigoLinearLoadingIndicator()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoLinearLoadingIndicatorMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        NavigoLinearLoadingIndicator()
    }
}
