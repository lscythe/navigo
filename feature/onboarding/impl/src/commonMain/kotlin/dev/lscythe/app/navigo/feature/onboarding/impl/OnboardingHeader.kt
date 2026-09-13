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
package dev.lscythe.app.navigo.feature.onboarding.impl

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.lscythe.app.navigo.core.designsystem.brand.NavigoBrand
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIconButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoOutlinedButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoTextButton
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.locale.Language
import dev.lscythe.app.navigo.core.designsystem.icon.navigation.ArrowLeft
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.resources.generated.resources.Res
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_permissions_back
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_skip_button_label
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OnboardingHeader(
    showBack: Boolean,
    showLanguage: Boolean,
    showSkip: Boolean,
    languageId: String,
    onBack: () -> Unit,
    onChangeLanguage: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.height(32.dp), contentAlignment = Alignment.CenterStart) {
            AnimatedContent(
                targetState = showBack,
                transitionSpec = {
                    (fadeIn() + scaleIn(initialScale = 0.8f)).togetherWith(
                        fadeOut() + scaleOut(targetScale = 0.8f)
                    )
                },
                label = "onboardingBack",
            ) { backVisible ->
                if (backVisible) {
                    NavigoIconButton(
                        onClick = onBack,
                        modifier = Modifier.size(32.dp),
                    ) {
                        NavigoIcon(
                            imageVector = NavigoIcons.ArrowLeft,
                            contentDescription =
                                stringResource(Res.string.onboarding_permissions_back),
                        )
                    }
                } else {
                    NavigoBrand(
                        modifier = Modifier.height(32.dp),
                        pinColor = MaterialTheme.colorScheme.primary,
                        accentColor = MaterialTheme.colorScheme.primary,
                        showTagline = false,
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.micro),
        ) {
            if (showLanguage) {
                NavigoOutlinedButton(
                    onClick = onChangeLanguage,
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(50),
                    border =
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                        ),
                    colors =
                        ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.micro),
                    ) {
                        NavigoIcon(
                            imageVector = NavigoIcons.Language,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            size = 14.dp,
                        )
                        Text(
                            text = languageId,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 12.sp,
                        )
                    }
                }
            }
            if (showSkip) {
                NavigoTextButton(
                    onClick = onSkip,
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.onboarding_skip_button_label),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 13.sp,
                    )
                }
            }
        }
    }
}
