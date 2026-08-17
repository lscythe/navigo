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
@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.lscythe.app.navigo.core.designsystem.component.atom

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
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
 * Displays determinate progress along a six-sided cookie outline.
 *
 * @param progress the progress to display, from `0f` to `1f`
 * @param modifier the [Modifier] to apply to this indicator
 * @param color the color of the progress outline
 * @param trackColor the color of the unfilled outline
 * @param backgroundColor the color of the circular background
 * @param size the width and height of this indicator
 * @param strokeWidth the width of the cookie outline
 */
@Composable
fun NavigoCircularProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.outlineVariant,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    size: Dp = 48.dp,
    strokeWidth: Dp = 4.dp,
) {
    require(progress.isFinite()) { "progress must be finite" }
    DeterminateCircularIndicator(
        progress = progress.coerceIn(0f, 1f),
        modifier = modifier,
        color = color,
        trackColor = trackColor,
        backgroundColor = backgroundColor,
        size = size,
        strokeWidth = strokeWidth,
    )
}

/**
 * Displays an indeterminate solid six-sided cookie indicator.
 *
 * @param modifier the [Modifier] to apply to this indicator
 * @param color the color of the cookie
 * @param backgroundColor the color of the circular background
 * @param size the width and height of this indicator
 */
@Composable
fun NavigoCircularLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    size: Dp = 48.dp,
) {
    val transition = rememberInfiniteTransition(label = "circular loading")
    val rotation by
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(1_200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "circular loading rotation",
        )
    val cookiePath = MaterialShapes.Cookie6Sided.toPath(startAngle = -90)
    Canvas(
        modifier =
            modifier.size(size).semantics {
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            }
    ) {
        drawCircle(backgroundColor)
        val inset = this.size.minDimension * 0.2f
        val bounds = cookiePath.getBounds()
        val scale = (this.size.minDimension - inset * 2f) / maxOf(bounds.width, bounds.height)
        val left = (this.size.width - bounds.width * scale) / 2f
        val top = (this.size.height - bounds.height * scale) / 2f
        val transformed = Path().apply { addPath(cookiePath) }
        transformed.translate(Offset(-bounds.left, -bounds.top))

        withTransform({
            rotate(rotation)
            translate(left, top)
            scale(scale, scale, pivot = Offset.Zero)
        }) {
            drawPath(transformed, color)
        }
    }
}

@Composable
private fun DeterminateCircularIndicator(
    progress: Float,
    modifier: Modifier,
    color: Color,
    trackColor: Color,
    backgroundColor: Color,
    size: Dp,
    strokeWidth: Dp,
) {
    val cookiePath = MaterialShapes.Cookie6Sided.toPath(startAngle = -90)
    Canvas(
        modifier =
            modifier.size(size).semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f)
            }
    ) {
        drawCircle(backgroundColor)

        val stroke = strokeWidth.toPx()
        val inset = stroke * 1.5f
        val bounds = cookiePath.getBounds()
        val scale = (this.size.minDimension - inset * 2f) / maxOf(bounds.width, bounds.height)
        val left = (this.size.width - bounds.width * scale) / 2f
        val top = (this.size.height - bounds.height * scale) / 2f
        val transformed = Path().apply { addPath(cookiePath) }
        transformed.translate(Offset(-bounds.left, -bounds.top))

        withTransform({
            translate(left, top)
            scale(scale, scale, pivot = Offset.Zero)
        }) {
            val pathStroke = Stroke(width = stroke / scale, cap = StrokeCap.Round)
            drawPath(transformed, trackColor, style = pathStroke)

            val measure = PathMeasure().apply { setPath(transformed, false) }
            val segment = Path()
            measure.getSegment(0f, measure.length * progress, segment)
            drawPath(segment, color, style = pathStroke)
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoCircularProgressIndicatorPreview() {
    NavigoPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.cardPadding)) {
            NavigoCircularProgressIndicator(progress = 0.25f)
            NavigoCircularProgressIndicator(progress = 0.65f)
            NavigoCircularProgressIndicator(progress = 1f)
        }
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoCircularProgressIndicatorMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        NavigoCircularProgressIndicator(progress = 0.65f)
    }
}

@NavigoThemePreview
@Composable
private fun NavigoCircularLoadingIndicatorPreview() {
    NavigoPreview {
        NavigoCircularLoadingIndicator()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoCircularLoadingIndicatorMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        NavigoCircularLoadingIndicator()
    }
}
