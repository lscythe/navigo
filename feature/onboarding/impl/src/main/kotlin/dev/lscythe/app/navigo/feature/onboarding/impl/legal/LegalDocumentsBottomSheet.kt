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
package dev.lscythe.app.navigo.feature.onboarding.impl.legal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.common.locale.SupportedLanguage
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoLinearProgressIndicator
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoOutlinedButton
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoChoiceChip
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoModalBottomSheet
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.navigation.ArrowDown
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LegalDocumentsBottomSheet(
    language: SupportedLanguage?,
    onAccept: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val documents =
        remember(language) {
            LegalDocumentType.entries.associateWith {
                loadBundledLegalDocument(context, it, language)
            }
        }
    val sheetState =
        rememberBottomSheetState(
            initialValue = SheetValue.Hidden,
            enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
        )
    var readingState by remember { mutableStateOf(LegalReadingState()) }
    val document = checkNotNull(documents[readingState.activeDocument])
    val termsScrollState = rememberScrollState()
    val privacyScrollState = rememberScrollState()
    val scrollState =
        when (readingState.activeDocument) {
            LegalDocumentType.Terms -> termsScrollState
            LegalDocumentType.Privacy -> privacyScrollState
        }
    val reachedEnd by
        remember(scrollState) {
            derivedStateOf {
                scrollState.maxValue > 0 && scrollState.value >= scrollState.maxValue
            }
        }

    LaunchedEffect(readingState.activeDocument, reachedEnd) {
        if (reachedEnd) readingState = readingState.markRead(readingState.activeDocument)
    }

    NavigoModalBottomSheet(
        title = document.title,
        description =
            stringResource(
                R.string.core_ui_legal_sheet_metadata,
                document.version,
                document.readingTimeMinutes,
            ),
        onDismissRequest = onDismissRequest,
        modifier = modifier.fillMaxSize(),
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        dragHandle = null,
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = NavigoSpacing.screen),
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
        ) {
            LegalDocumentType.entries.forEach { type ->
                NavigoChoiceChip(
                    selected = type == readingState.activeDocument,
                    onClick = { readingState = readingState.open(type) },
                    label = stringResource(type.labelResource),
                )
            }
        }
        Column(
            modifier =
                Modifier.fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(NavigoSpacing.screen),
            verticalArrangement = Arrangement.spacedBy(NavigoSpacing.screen),
        ) {
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(16.dp),
                        )
                        .padding(NavigoSpacing.container)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.item)) {
                    Text(
                        text = stringResource(R.string.core_ui_legal_sheet_summary),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = AnnotatedString.fromHtml(document.summaryHtml),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            legalHtmlSections(document.bodyHtml).forEach { sectionHtml ->
                Text(
                    text = AnnotatedString.fromHtml(sectionHtml),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        val progress by
            remember(scrollState) {
                derivedStateOf {
                    if (scrollState.maxValue == 0) 0f
                    else {
                        scrollState.value.toFloat() / scrollState.maxValue
                    }
                }
            }
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .padding(horizontal = NavigoSpacing.screen, vertical = NavigoSpacing.item),
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NavigoLinearProgressIndicator(
                progress = progress,
                modifier = Modifier.weight(1f),
            )
            Text(
                "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(0.14f),
                textAlign = TextAlign.End,
            )
        }
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .padding(
                        start = NavigoSpacing.screen,
                        top = NavigoSpacing.container,
                        end = NavigoSpacing.screen,
                        bottom = NavigoSpacing.section,
                    ),
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
        ) {
            NavigoOutlinedButton(
                onClick = onDismissRequest,
                modifier = Modifier.weight(1f).heightIn(min = 54.dp),
            ) {
                Text(stringResource(R.string.core_ui_legal_sheet_decline))
            }
            NavigoButton(
                onClick = {
                    if (readingState.primaryAction == LegalPrimaryAction.Accept) {
                        onAccept()
                    } else {
                        readingState = readingState.onPrimaryAction()
                    }
                },
                enabled = readingState.primaryAction != LegalPrimaryAction.ReadToContinue,
                modifier = Modifier.weight(1.4f).heightIn(min = 54.dp),
            ) {
                if (readingState.primaryAction == LegalPrimaryAction.ReadToContinue) {
                    NavigoIcon(imageVector = NavigoIcons.ArrowDown, contentDescription = null)
                }
                Text(stringResource(readingState.primaryAction.labelResource))
            }
        }
    }
}

private val LegalDocumentType.labelResource: Int
    get() =
        when (this) {
            LegalDocumentType.Terms -> R.string.core_ui_legal_sheet_terms
            LegalDocumentType.Privacy -> R.string.core_ui_legal_sheet_privacy
        }

private val LegalPrimaryAction.labelResource: Int
    get() =
        when (this) {
            LegalPrimaryAction.ReadToContinue -> R.string.core_ui_legal_sheet_read_to_continue
            LegalPrimaryAction.ContinueToTerms -> R.string.core_ui_legal_sheet_continue_to_terms
            LegalPrimaryAction.ContinueToPrivacy -> R.string.core_ui_legal_sheet_continue_to_privacy
            LegalPrimaryAction.Accept -> R.string.core_ui_legal_sheet_accept
        }
