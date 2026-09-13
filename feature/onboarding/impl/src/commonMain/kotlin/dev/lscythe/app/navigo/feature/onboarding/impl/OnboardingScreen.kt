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
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.ui.locale.LanguageSelectionBottomSheet
import dev.lscythe.app.navigo.feature.onboarding.impl.content.OnboardingIntroduction
import dev.lscythe.app.navigo.feature.onboarding.impl.content.OnboardingPageCount
import dev.lscythe.app.navigo.feature.onboarding.impl.content.OnboardingPermissions
import dev.lscythe.app.navigo.feature.onboarding.impl.content.OnboardingProfile
import dev.lscythe.app.navigo.feature.onboarding.impl.legal.LegalDocumentsBottomSheet
import kotlinx.coroutines.launch

private const val PageCount = OnboardingPageCount
private const val PageDurationMillis = 5_000
private const val PageTransitionDurationMillis = 650

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OnboardingScreen(
    state: OnboardingUiState,
    onIntent: (OnboardingIntent) -> Unit,
    modifier: Modifier = Modifier,
    systemLanguage: OnboardingLanguage,
) {
    val pagerState = rememberPagerState(pageCount = { PageCount })
    var showLanguageSelector by remember { mutableStateOf(false) }
    var showLegalDocuments by remember { mutableStateOf(false) }
    var pendingLanguage by remember { mutableStateOf(state.language.toSupportedLanguage()) }
    val sheetState =
        rememberBottomSheetState(
            initialValue = SheetValue.Hidden,
            enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
        )
    val scope = rememberCoroutineScope()
    val dismissLanguageSelector = {
        scope.launch {
            sheetState.hide()
            if (!sheetState.isVisible) showLanguageSelector = false
        }
        Unit
    }

    var pageProgress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(pagerState.settledPage, showLanguageSelector, state.stage) {
        if (showLanguageSelector || state.stage != OnboardingStage.Introduction)
            return@LaunchedEffect
        val progress = Animatable(0f)
        pageProgress = 0f
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = PageDurationMillis, easing = LinearEasing),
        ) {
            pageProgress = value
        }
        if (pagerState.settledPage < PageCount - 1) {
            pageProgress = 0f
            pagerState.animateScrollToPage(
                page = pagerState.settledPage + 1,
                animationSpec =
                    tween(
                        durationMillis = PageTransitionDurationMillis,
                        easing = FastOutSlowInEasing,
                    ),
            )
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(vertical = NavigoSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
    ) {
        OnboardingHeader(
            showBack = state.stage != OnboardingStage.Introduction,
            showLanguage = state.stage != OnboardingStage.Profile,
            showSkip = state.stage != OnboardingStage.Profile,
            languageId = state.effectiveLanguage.toSupportedLanguage()!!.displayCode,
            onBack = { onIntent(OnboardingIntent.BackClicked) },
            onChangeLanguage = {
                pendingLanguage = state.language.toSupportedLanguage()
                showLanguageSelector = true
            },
            onSkip = { onIntent(OnboardingIntent.SkipClicked) },
            modifier =
                Modifier.padding(horizontal = NavigoSpacing.screen)
                    .padding(bottom = NavigoSpacing.container),
        )
        AnimatedContent(
            targetState = state.stage,
            transitionSpec = {
                val forward = targetState.ordinal > initialState.ordinal
                val direction = if (forward) 1 else -1
                (slideInHorizontally(tween(300)) { direction * 24 } + fadeIn(tween(220)))
                    .togetherWith(
                        slideOutHorizontally(tween(300)) { -direction * 24 } + fadeOut(tween(180))
                    )
            },
            label = "onboardingStage",
            modifier = Modifier.weight(1f),
        ) { currentStage ->
            when (currentStage) {
                OnboardingStage.Introduction ->
                    OnboardingIntroduction(
                        pagerState = pagerState,
                        pageProgress = pageProgress,
                        pagerEnabled = !showLanguageSelector,
                        onNextPage = {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = pagerState.currentPage + 1,
                                    animationSpec =
                                        tween(
                                            durationMillis = PageTransitionDurationMillis,
                                            easing = FastOutSlowInEasing,
                                        ),
                                )
                            }
                        },
                        onContinue = { onIntent(OnboardingIntent.IntroductionContinued) },
                    )

                OnboardingStage.Permissions ->
                    OnboardingPermissions(
                        onContinue = {
                            onIntent(OnboardingIntent.PermissionChoiceSelected)
                        }
                    )

                OnboardingStage.Profile ->
                    OnboardingProfile(
                        state = state,
                        onOpenLegalDocuments = { showLegalDocuments = true },
                        onIntent = onIntent,
                    )
            }
        }
    }
    if (showLanguageSelector) {
        LanguageSelectionBottomSheet(
            selectedLanguage = pendingLanguage,
            sheetState = sheetState,
            onLanguageSelected = { language ->
                pendingLanguage = language
                onIntent(
                    OnboardingIntent.LanguageSelected(
                        language = language.toOnboardingLanguage(),
                        effectiveLanguage = language?.toOnboardingLanguage() ?: systemLanguage,
                    )
                )
            },
            onApply = dismissLanguageSelector,
            onDismissRequest = dismissLanguageSelector,
        )
    }
    if (showLegalDocuments) {
        LaunchedEffect(showLegalDocuments, state.language) {
            onIntent(OnboardingIntent.LegalDocumentsRequested)
        }
        state.legalDocuments?.let { documents ->
            LegalDocumentsBottomSheet(
                documents = documents,
                onAccept = {
                    onIntent(OnboardingIntent.LegalDocumentsAccepted)
                    showLegalDocuments = false
                },
                onDismissRequest = { showLegalDocuments = false },
            )
        }
    }
}
