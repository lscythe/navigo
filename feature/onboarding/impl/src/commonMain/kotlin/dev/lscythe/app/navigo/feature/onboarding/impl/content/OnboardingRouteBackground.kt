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
package dev.lscythe.app.navigo.feature.onboarding.impl.content

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.transit.BusFilled

@Composable
internal fun OnboardingRouteBackground(
    pagerProgress: Float,
    modifier: Modifier = Modifier,
) {
    val primary = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface
    val infiniteTransition = rememberInfiniteTransition(label = "busPulse")
    val animatedPulse by
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1_600), RepeatMode.Restart),
            label = "busPulseRadius",
        )
    val pulse = if (LocalWindowInfo.current.isWindowFocused) animatedPulse else 0f
    val routeFraction = routeFractionForPagerProgress(pagerProgress)

    BoxWithConstraints(modifier = modifier) {
        Canvas(Modifier.fillMaxSize()) {
            val route = onboardingRoutePath(size.width, size.height)
            val routeMeasure = PathMeasure().apply { setPath(route, false) }
            val travelled = Path()
            routeMeasure.getSegment(0f, routeMeasure.length * routeFraction, travelled, true)
            val position = routeMeasure.getPosition(routeMeasure.length * routeFraction)

            val secondary =
                Path().apply {
                    moveTo(-size.width * 0.08f, size.height * 0.92f)
                    cubicTo(
                        size.width * 0.3f,
                        size.height * 0.82f,
                        size.width * 0.58f,
                        size.height * 0.52f,
                        size.width * 1.08f,
                        size.height * 0.42f,
                    )
                }
            drawPath(
                secondary,
                primary.copy(alpha = 0.08f),
                style = Stroke(6.dp.toPx(), cap = StrokeCap.Round),
            )
            drawPath(
                route,
                primary.copy(alpha = 0.22f),
                style = Stroke(7.dp.toPx(), cap = StrokeCap.Round),
            )
            drawPath(
                travelled,
                primary.copy(alpha = 0.72f),
                style = Stroke(7.dp.toPx(), cap = StrokeCap.Round),
            )

            listOf(0.18f, 0.5f, 0.82f).forEach { fraction ->
                val stop = routeMeasure.getPosition(routeMeasure.length * fraction)
                drawCircle(primary.copy(alpha = 0.75f), 7.dp.toPx(), stop)
                drawCircle(surface, 3.dp.toPx(), stop)
            }
            repeat(2) { index ->
                val phase = (pulse + index * 0.5f) % 1f
                drawCircle(
                    color = primary.copy(alpha = (1f - phase) * 0.18f),
                    radius = (24 + phase * 22).dp.toPx(),
                    center = position,
                    style = Stroke(2.dp.toPx()),
                )
            }
        }

        val marker =
            routePosition(
                routeFraction,
                constraints.maxWidth.toFloat(),
                constraints.maxHeight.toFloat(),
            )
        Box(
            modifier =
                Modifier.align(Alignment.TopStart)
                    .graphicsLayer {
                        translationX = marker.x - 24.dp.toPx()
                        translationY = marker.y - 24.dp.toPx()
                    }
                    .size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(Modifier.fillMaxSize()) { drawCircle(primary, radius = size.minDimension / 2) }
            NavigoIcon(
                imageVector = NavigoIcons.BusFilled,
                contentDescription = null,
                size = 22.dp,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

private fun onboardingRoutePath(width: Float, height: Float) =
    Path().apply {
        moveTo(-width * 0.12f, height * 0.8f)
        cubicTo(
            width * 0.08f,
            height * 0.7f,
            width * 0.18f,
            height * 0.54f,
            width * 0.38f,
            height * 0.58f,
        )
        cubicTo(
            width * 0.66f,
            height * 0.64f,
            width * 0.7f,
            height * 0.18f,
            width * 1.08f,
            height * 0.1f,
        )
    }

private fun routePosition(fraction: Float, width: Float, height: Float): Offset {
    val path = onboardingRoutePath(width, height)
    val measure = PathMeasure().apply { setPath(path, false) }
    return measure.getPosition(measure.length * fraction)
}
