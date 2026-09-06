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
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.lscythe.app.navigo.core.common.locale.SupportedLanguage
import dev.lscythe.app.navigo.core.designsystem.brand.NavigoBrand
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIconButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoOutlinedButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoTextButton
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.locale.Language
import dev.lscythe.app.navigo.core.designsystem.icon.navigation.ArrowLeft
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.resources.generated.resources.Res
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_permissions_back
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_skip_button_label
import dev.lscythe.app.navigo.core.ui.locale.LanguageSelectionBottomSheet
import dev.lscythe.app.navigo.feature.onboarding.impl.content.OnboardingIntroduction
import dev.lscythe.app.navigo.feature.onboarding.impl.content.OnboardingPageCount
import dev.lscythe.app.navigo.feature.onboarding.impl.content.OnboardingPermissions
import dev.lscythe.app.navigo.feature.onboarding.impl.content.OnboardingProfile
import dev.lscythe.app.navigo.feature.onboarding.impl.legal.LegalDocumentsBottomSheet
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

private const val PageCount = OnboardingPageCount
private const val PageDurationMillis = 5_000
private const val PageTransitionDurationMillis = 650

private enum class OnboardingStage {
    Introduction,
    Permissions,
    Profile,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OnboardingScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { PageCount })
    var stage by remember { mutableStateOf(OnboardingStage.Introduction) }
    var showLanguageSelector by remember { mutableStateOf(false) }
    var showLegalDocuments by remember { mutableStateOf(false) }
    var legalDocumentsRead by remember { mutableStateOf(false) }
    var selectedLanguage by remember {
        mutableStateOf<SupportedLanguage?>(SupportedLanguage.Indonesian)
    }
    var pendingLanguage by remember { mutableStateOf(selectedLanguage) }
    val sheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val dismissLanguageSelector = {
        scope.launch {
            sheetState.hide()
            if (!sheetState.isVisible) showLanguageSelector = false
        }
        Unit
    }

    var pageProgress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(pagerState.settledPage, showLanguageSelector, stage) {
        if (showLanguageSelector || stage != OnboardingStage.Introduction) return@LaunchedEffect
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
            showBack = stage != OnboardingStage.Introduction,
            showLanguage = stage != OnboardingStage.Profile,
            showSkip = stage != OnboardingStage.Profile,
            languageId = selectedLanguage?.displayCode.orEmpty(),
            onBack = {
                stage =
                    when (stage) {
                        OnboardingStage.Introduction -> OnboardingStage.Introduction
                        OnboardingStage.Permissions -> OnboardingStage.Introduction
                        OnboardingStage.Profile -> OnboardingStage.Permissions
                    }
            },
            onChangeLanguage = {
                pendingLanguage = selectedLanguage
                showLanguageSelector = true
            },
            onSkip = { stage = OnboardingStage.Profile },
            modifier =
                Modifier.padding(horizontal = NavigoSpacing.screen)
                    .padding(bottom = NavigoSpacing.container),
        )
        AnimatedContent(
            targetState = stage,
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
                        onContinue = { stage = OnboardingStage.Permissions },
                    )

                OnboardingStage.Permissions ->
                    OnboardingPermissions(onContinue = { stage = OnboardingStage.Profile })

                OnboardingStage.Profile ->
                    OnboardingProfile(
                        legalDocumentsRead = legalDocumentsRead,
                        onOpenLegalDocuments = { showLegalDocuments = true },
                        onContinue = onContinue,
                    )
            }
        }
    }
    if (showLanguageSelector) {
        LanguageSelectionBottomSheet(
            selectedLanguage = pendingLanguage,
            sheetState = sheetState,
            onLanguageSelected = { pendingLanguage = it },
            onApply = {
                selectedLanguage = pendingLanguage
                dismissLanguageSelector()
            },
            onDismissRequest = dismissLanguageSelector,
        )
    }
    if (showLegalDocuments) {
        LegalDocumentsBottomSheet(
            language = selectedLanguage,
            onAccept = {
                legalDocumentsRead = true
                showLegalDocuments = false
            },
            onDismissRequest = { showLegalDocuments = false },
        )
    }
}

@Composable
private fun OnboardingHeader(
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
                                stringResource(Res.string.core_ui_onboarding_permissions_back),
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
                        text = stringResource(Res.string.core_ui_onboarding_skip_button_label),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 13.sp,
                    )
                }
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
