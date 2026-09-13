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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.action.Paint
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.resources.generated.resources.Res
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_profile_custom_avatar_colour
import org.jetbrains.compose.resources.stringResource

internal val AvatarColors =
    listOf(
        Color(0xFF17473C),
        Color(0xFF4DAA57),
        Color(0xFFA7E548),
        Color(0xFFBC4B20),
        Color(0xFF293B8F),
    )

@Composable
internal fun OnboardingProfileAvatarColorRow(
    selectedColorArgb: UInt,
    avatarShape: Shape,
    onSelectColorArgb: (UInt) -> Unit,
    onOpenCustomPicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
    ) {
        AvatarColors.forEach { color ->
            val colorArgb = color.toArgb().toUInt()
            val isSelected = colorArgb == selectedColorArgb
            Box(
                modifier =
                    Modifier.size(48.dp)
                        .clip(avatarShape)
                        .then(
                            if (isSelected) {
                                Modifier.border(
                                    3.dp,
                                    MaterialTheme.colorScheme.onBackground,
                                    avatarShape,
                                )
                            } else Modifier
                        )
                        .clickable { onSelectColorArgb(colorArgb) }
                        .padding(if (isSelected) 5.dp else 0.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(Modifier.fillMaxSize().clip(MaterialTheme.shapes.medium).background(color))
            }
        }
        val isCustomSelected = AvatarColors.none { it.toArgb().toUInt() == selectedColorArgb }
        Box(
            modifier =
                Modifier.size(48.dp)
                    .clip(avatarShape)
                    .then(
                        if (isCustomSelected) {
                            Modifier.border(
                                3.dp,
                                MaterialTheme.colorScheme.onBackground,
                                avatarShape,
                            )
                        } else Modifier
                    )
                    .clickable(onClick = onOpenCustomPicker)
                    .padding(if (isCustomSelected) 5.dp else 0.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(Modifier.fillMaxSize().clip(MaterialTheme.shapes.medium)) {
                drawRect(
                    Brush.sweepGradient(
                        listOf(
                            Color.Red,
                            Color.Yellow,
                            Color.Green,
                            Color.Cyan,
                            Color.Blue,
                            Color.Magenta,
                            Color.Red,
                        )
                    )
                )
            }
            NavigoIcon(
                imageVector = NavigoIcons.Paint,
                contentDescription =
                    stringResource(Res.string.onboarding_profile_custom_avatar_colour),
                tint = Color.White,
            )
        }
    }
}
