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
package dev.lscythe.app.navigo.core.ui.locale

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.common.locale.SupportedLanguage
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoRadioButton
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoModalBottomSheet
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionBottomSheet(
    selectedLanguage: SupportedLanguage?,
    onLanguageSelected: (SupportedLanguage?) -> Unit,
    onApply: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigoModalBottomSheet(
        title = stringResource(R.string.core_ui_language_sheet_title),
        description = stringResource(R.string.core_ui_language_sheet_description),
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        LanguageRow(
            title = stringResource(R.string.core_ui_language_system_default),
            description = stringResource(R.string.core_ui_language_system_default_description),
            selected = selectedLanguage == null,
            onClick = { onLanguageSelected(null) },
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        SupportedLanguage.entries.forEach { language ->
            LanguageRow(
                title = language.displayName(),
                selected = language == selectedLanguage,
                onClick = { onLanguageSelected(language) },
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
        NavigoButton(
            onClick = onApply,
            modifier =
                Modifier.fillMaxWidth()
                    .padding(
                        start = NavigoSpacing.screen,
                        top = NavigoSpacing.screen,
                        end = NavigoSpacing.screen,
                        bottom = NavigoSpacing.section,
                    )
                    .heightIn(min = 64.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
        ) {
            Text(
                text = stringResource(R.string.core_ui_language_apply),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

@Composable
private fun LanguageRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    description: String? = null,
) {
    Row(
        modifier =
            Modifier.fillMaxWidth()
                .clickable(role = Role.RadioButton, onClick = onClick)
                .padding(horizontal = NavigoSpacing.screen, vertical = NavigoSpacing.screen),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.container),
    ) {
        NavigoRadioButton(selected = selected, onClick = null)
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            if (description != null) {
                Text(
                    description,
                    modifier = Modifier.padding(top = NavigoSpacing.micro),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SupportedLanguage.displayName(): String =
    when (this) {
        SupportedLanguage.English -> stringResource(R.string.core_ui_language_english)
        SupportedLanguage.Indonesian -> stringResource(R.string.core_ui_language_indonesian)
    }

@NavigoThemePreview
@Composable
private fun LanguageSelectionBottomSheetPreview() {
    var showSheet by remember { mutableStateOf(false) }

    NavigoPreview {
        Scaffold { padding ->
            NavigoButton(onClick = { showSheet = true }, modifier = Modifier.padding(padding)) {
                Text("Open language selector")
            }
            if (showSheet) {
                LanguageSelectionBottomSheet(
                    selectedLanguage = SupportedLanguage.Indonesian,
                    onLanguageSelected = {},
                    onApply = { showSheet = false },
                    onDismissRequest = { showSheet = false },
                )
            }
        }
    }
}
