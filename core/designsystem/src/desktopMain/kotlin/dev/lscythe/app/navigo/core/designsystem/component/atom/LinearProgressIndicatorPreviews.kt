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
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import dev.lscythe.app.navigo.core.designsystem.preview.DesktopMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.DesktopPreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
private fun NavigoLinearProgressIndicatorPreview() {
    DesktopPreview {
        Column(verticalArrangement = spacedBy(NavigoSpacing.container)) {
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

@Preview
@Composable
private fun NavigoLinearProgressIndicatorMaterialKolorPreview() {
    DesktopMaterialKolorPreview {
        Column(verticalArrangement = spacedBy(NavigoSpacing.container)) {
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

@Preview
@Composable
private fun NavigoLinearLoadingIndicatorPreview() {
    DesktopPreview {
        NavigoLinearLoadingIndicator()
    }
}

@Preview
@Composable
private fun NavigoLinearLoadingIndicatorMaterialKolorPreview() {
    DesktopMaterialKolorPreview {
        NavigoLinearLoadingIndicator()
    }
}
