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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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

/**
 * Displays page position and progress through a pager.
 *
 * @param pageCount the number of pages
 * @param currentPage the current page index
 * @param pageProgress progress through the current page, from `0f` to `1f`
 * @param modifier the [Modifier] to apply to this indicator
 * @param activeWidth the width of the active indicator
 * @param indicatorSize the size of each inactive indicator
 * @param activeColor the color of the active indicator
 * @param inactiveColor the color of inactive indicators
 */
@Composable
fun NavigoPagerIndicator(
    pageCount: Int,
    currentPage: Int,
    pageProgress: Float,
    modifier: Modifier = Modifier,
    activeWidth: Dp = 64.dp,
    indicatorSize: Dp = 24.dp,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    require(pageCount > 0) { "pageCount must be greater than zero" }
    require(currentPage in 0 until pageCount) { "currentPage must be within pageCount" }

    val progress by
        animateFloatAsState(
            targetValue = pageProgress.coerceIn(0f, 1f),
            animationSpec = tween(durationMillis = 150, easing = LinearEasing),
            label = "pager indicator progress",
        )

    Row(
        modifier =
            modifier.semantics {
                progressBarRangeInfo =
                    ProgressBarRangeInfo(
                        current = currentPage + progress,
                        range = 0f..pageCount.toFloat(),
                        steps = pageCount - 1,
                    )
            },
        horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.chipGap),
    ) {
        repeat(pageCount) { page ->
            val isActive = page == currentPage
            Canvas(
                modifier =
                    Modifier.size(
                            width = if (isActive) activeWidth else indicatorSize,
                            height = indicatorSize,
                        )
                        .clip(MaterialTheme.shapes.extraLarge)
            ) {
                val radius = size.height / 2f
                drawRoundRect(
                    color = inactiveColor,
                    size = size,
                    cornerRadius = CornerRadius(radius),
                )
                if (isActive && progress > 0f) {
                    clipRect(right = size.width * progress) {
                        drawRoundRect(
                            color = activeColor,
                            topLeft = Offset.Zero,
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(radius),
                        )
                    }
                }
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoPagerIndicatorPreview() {
    val transition = rememberInfiniteTransition(label = "pager preview")
    val progress by
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 3_000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "page progress",
        )

    NavigoPreview {
        NavigoPagerIndicator(
            pageCount = 4,
            currentPage = 0,
            pageProgress = progress,
        )
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoPagerIndicatorMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        NavigoPagerIndicator(
            pageCount = 4,
            currentPage = 1,
            pageProgress = 0.65f,
        )
    }
}
