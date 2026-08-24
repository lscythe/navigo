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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.lscythe.app.navigo.core.designsystem.brand.NavigoBrand
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoOutlinedButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoPagerIndicator
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoTextButton
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.locale.Language
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.ui.R
import dev.lscythe.app.navigo.feature.onboarding.impl.R as OBR

private const val PageCount = 3
private const val PageDurationMillis = 5_000

@Composable
internal fun OnboardingScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { PageCount })

    val buttonLabel by remember {
        derivedStateOf {
            val isLastPage = pagerState.currentPage == pagerState.pageCount - 1
            if (isLastPage) {
                R.string.core_ui_onboarding_setup_button_label
            } else {
                R.string.core_ui_onboarding_next_button_label
            }
        }
    }

    var pageProgress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(pagerState.settledPage) {
        val progress = Animatable(0f)
        pageProgress = 0f
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = PageDurationMillis, easing = LinearEasing),
        ) {
            pageProgress = value
        }
        if (pagerState.settledPage < PageCount - 1) {
            pagerState.animateScrollToPage(pagerState.settledPage + 1)
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(vertical = NavigoSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
    ) {
        OnboardingHeader(
            languageId = "ID",
            onChangeLanguage = {},
            onSkip = onContinue,
            modifier =
                Modifier.padding(horizontal = NavigoSpacing.screen)
                    .padding(bottom = NavigoSpacing.screen),
        )
        Text(
            stringResource(
                OBR.string.feature_onboarding_impl_pager_indicator_format,
                pagerState.currentPage + 1,
                PageCount,
            ),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = NavigoSpacing.screen),
        )
        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxSize().weight(1f),
            verticalAlignment = Alignment.Top,
        ) { page ->
            when (page) {
                0 -> OnboardingFeedPage()
                1 -> OnboardingGpsPage()
                2 -> OnboardingAlarmPage()
            }
        }
        Spacer(Modifier.height(NavigoSpacing.micro))
        Column(
            modifier = Modifier.padding(horizontal = NavigoSpacing.screen),
            verticalArrangement = Arrangement.spacedBy(NavigoSpacing.screen),
        ) {
            NavigoPagerIndicator(
                pageCount = PageCount,
                currentPage = pagerState.currentPage,
                pageProgress = pageProgress,
                inactiveWidth = 12.dp,
                indicatorSize = 6.dp,
                activeColor = MaterialTheme.colorScheme.primary,
                inactiveColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
            )
            NavigoButton(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
            ) {
                Text(stringResource(buttonLabel))
            }
        }
    }
}

@Composable
private fun OnboardingHeader(
    languageId: String,
    onChangeLanguage: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NavigoBrand(
            modifier = Modifier.height(32.dp),
            pinColor = MaterialTheme.colorScheme.primary,
            accentColor = MaterialTheme.colorScheme.onPrimary,
            showTagline = false,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.micro),
        ) {
            NavigoOutlinedButton(
                onClick = onChangeLanguage,
                modifier = Modifier.height(32.dp),
                shape = RoundedCornerShape(50),
                border =
                    BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                    ),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
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
            NavigoTextButton(
                onClick = onSkip,
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
            ) {
                Text(
                    text = stringResource(R.string.core_ui_onboarding_skip_button_label),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 13.sp,
                )
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun OnboardingScreenPreview() {
    NavigoPreview(contentPadding = PaddingValues(0.dp)) {
        OnboardingScreen(onContinue = {})
    }
}
