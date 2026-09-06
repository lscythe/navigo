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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoPagerIndicator
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.resources.generated.resources.Res
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_next_button_label
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_setup_button_label
import dev.lscythe.app.navigo.core.resources.generated.resources.feature_onboarding_impl_pager_indicator_format
import org.jetbrains.compose.resources.stringResource

internal const val OnboardingPageCount = 3

@Composable
internal fun OnboardingIntroduction(
    pagerState: PagerState,
    pageProgress: Float,
    pagerEnabled: Boolean,
    onNextPage: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLastPage = pagerState.currentPage == pagerState.pageCount - 1
    val routePagerProgress by
        remember(pagerState) {
            derivedStateOf {
                (pagerState.currentPage + pagerState.currentPageOffsetFraction).coerceIn(0f, 2f)
            }
        }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
    ) {
        Text(
            stringResource(
                Res.string.feature_onboarding_impl_pager_indicator_format,
                pagerState.currentPage + 1,
                OnboardingPageCount,
            ),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = NavigoSpacing.screen),
        )
        Box(modifier = Modifier.fillMaxSize().weight(1f)) {
            OnboardingRouteBackground(
                pagerProgress = routePagerProgress,
                modifier =
                    Modifier.align(Alignment.BottomCenter).fillMaxWidth().fillMaxHeight(0.5f),
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                userScrollEnabled = pagerEnabled,
            ) { page ->
                when (page) {
                    0 -> OnboardingFeedPage()
                    1 -> OnboardingGpsPage()
                    2 -> OnboardingAlarmPage()
                }
            }
        }
        Spacer(Modifier.height(NavigoSpacing.micro))
        Column(
            modifier = Modifier.padding(horizontal = NavigoSpacing.screen),
            verticalArrangement = Arrangement.spacedBy(NavigoSpacing.screen),
        ) {
            NavigoPagerIndicator(
                pageCount = OnboardingPageCount,
                currentPage = pagerState.settledPage,
                pageProgress = pageProgress,
                inactiveWidth = 12.dp,
                indicatorSize = 6.dp,
                activeColor = MaterialTheme.colorScheme.primary,
                inactiveColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
            )
            NavigoButton(
                onClick = if (isLastPage) onContinue else onNextPage,
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
            ) {
                Text(
                    stringResource(
                        if (isLastPage) {
                            Res.string.core_ui_onboarding_setup_button_label
                        } else {
                            Res.string.core_ui_onboarding_next_button_label
                        }
                    )
                )
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun OnboardingIntroductionPreview() {
    NavigoPreview(contentPadding = PaddingValues(0.dp)) {
        OnboardingIntroduction(
            pagerState = rememberPagerState(pageCount = { OnboardingPageCount }),
            pageProgress = 0.35f,
            pagerEnabled = true,
            onNextPage = {},
            onContinue = {},
        )
    }
}
