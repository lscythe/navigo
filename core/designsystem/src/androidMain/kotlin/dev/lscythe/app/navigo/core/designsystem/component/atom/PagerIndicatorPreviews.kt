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
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview

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
