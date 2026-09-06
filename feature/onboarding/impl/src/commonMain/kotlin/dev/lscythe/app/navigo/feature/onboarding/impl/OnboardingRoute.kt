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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
internal fun OnboardingRoute(
    navigateHome: () -> Unit,
    viewModel: OnboardingViewModel = metroViewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    OnboardingScreen(
        legalDocuments = state.legalDocuments,
        onLoadLegalDocuments = { language -> viewModel.loadLegalDocuments(language.toDomain()) },
        onClearLegalDocuments = viewModel::clearLegalDocuments,
        onContinue = navigateHome,
        modifier = modifier,
    )
}

private fun dev.lscythe.app.navigo.core.common.locale.SupportedLanguage.toDomain() =
    when (this) {
        dev.lscythe.app.navigo.core.common.locale.SupportedLanguage.English -> AppLanguage.English
        dev.lscythe.app.navigo.core.common.locale.SupportedLanguage.Indonesian ->
            AppLanguage.Indonesian
    }
