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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.common.locale.SupportedLanguage
import dev.lscythe.app.navigo.core.common.text.toInitials
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoAvatar
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoCheckbox
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoSwitch
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoTextButton
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoChoiceChip
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoUnderlinedTextField
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.action.Paint
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.resources.generated.resources.Res
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_language_english
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_language_indonesian
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_analytics_description
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_analytics_title
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_avatar_colour_label
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_color_picker_description
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_color_picker_title
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_crash_reports_description
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_crash_reports_title
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_custom_avatar_colour
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_description
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_language_label
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_name_label
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_name_placeholder
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_open_map
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_privacy_label
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_privacy_link
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_required
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_ride_guest
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_terms_joiner
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_terms_link
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_terms_prefix
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_terms_title
import dev.lscythe.app.navigo.core.resources.generated.resources.core_ui_onboarding_profile_title
import dev.lscythe.app.navigo.core.ui.color.ColorSelectionBottomSheet
import org.jetbrains.compose.resources.stringResource

private val AvatarColors =
    listOf(
        Color(0xFF17473C),
        Color(0xFF4DAA57),
        Color(0xFFA7E548),
        Color(0xFFBC4B20),
        Color(0xFF293B8F),
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OnboardingProfile(
    legalDocumentsRead: Boolean,
    onOpenLegalDocuments: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val avatarShape = MaterialTheme.shapes.large
    var name by remember { mutableStateOf("") }
    var avatarColor by remember { mutableStateOf(AvatarColors.first()) }
    var customColor by remember { mutableStateOf(Color(0xFF5C8A3E)) }
    var customColorSelected by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf(SupportedLanguage.Indonesian) }
    var analyticsEnabled by remember { mutableStateOf(false) }
    var crashReportsEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = NavigoSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.section),
    ) {
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(NavigoSpacing.section),
        ) {
            OnboardingPageIntro(
                title = stringResource(Res.string.core_ui_onboarding_profile_title),
                description = stringResource(Res.string.core_ui_onboarding_profile_description),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.screen),
            ) {
                NavigoAvatar(
                    text = name.toInitials(),
                    size = 72.dp,
                    containerColor = avatarColor,
                    shape = avatarShape,
                    contentColor =
                        if (avatarColor == AvatarColors[2]) Color(0xFF17473C) else Color.White,
                )
                NavigoUnderlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.weight(1f),
                    label =
                        stringResource(Res.string.core_ui_onboarding_profile_name_label)
                            .uppercase(),
                    hint = stringResource(Res.string.core_ui_onboarding_profile_name_placeholder),
                )
            }
            ProfileSection(
                stringResource(Res.string.core_ui_onboarding_profile_avatar_colour_label)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item)) {
                    AvatarColors.forEach { color ->
                        Box(
                            modifier =
                                Modifier.size(48.dp)
                                    .clip(avatarShape)
                                    .then(
                                        if (color == avatarColor) {
                                            Modifier.border(
                                                3.dp,
                                                MaterialTheme.colorScheme.onBackground,
                                                avatarShape,
                                            )
                                        } else Modifier
                                    )
                                    .clickable {
                                        customColorSelected = false
                                        avatarColor = color
                                    }
                                    .padding(if (color == avatarColor) 5.dp else 0.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                Modifier.fillMaxSize()
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(color)
                            )
                        }
                    }
                    Box(
                        modifier =
                            Modifier.size(48.dp)
                                .clip(avatarShape)
                                .then(
                                    if (customColorSelected) {
                                        Modifier.border(
                                            3.dp,
                                            MaterialTheme.colorScheme.onBackground,
                                            avatarShape,
                                        )
                                    } else Modifier
                                )
                                .clickable {
                                    showColorPicker = true
                                }
                                .padding(if (customColorSelected) 5.dp else 0.dp),
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
                                stringResource(
                                    Res.string.core_ui_onboarding_profile_custom_avatar_colour
                                ),
                            tint = Color.White,
                        )
                    }
                }
            }
            ProfileSection(stringResource(Res.string.core_ui_onboarding_profile_language_label)) {
                Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item)) {
                    SupportedLanguage.entries.forEach { option ->
                        NavigoChoiceChip(
                            selected = language == option,
                            onClick = { language = option },
                            label =
                                when (option) {
                                    SupportedLanguage.English ->
                                        stringResource(Res.string.core_ui_language_english)
                                    SupportedLanguage.Indonesian ->
                                        stringResource(Res.string.core_ui_language_indonesian)
                                },
                        )
                    }
                }
            }
            ProfileSection(stringResource(Res.string.core_ui_onboarding_profile_privacy_label)) {
                ConsentRow(
                    title = stringResource(Res.string.core_ui_onboarding_profile_analytics_title),
                    description =
                        stringResource(Res.string.core_ui_onboarding_profile_analytics_description),
                    checked = analyticsEnabled,
                    onCheckedChange = { analyticsEnabled = it },
                )
                ConsentRow(
                    title =
                        stringResource(Res.string.core_ui_onboarding_profile_crash_reports_title),
                    description =
                        stringResource(
                            Res.string.core_ui_onboarding_profile_crash_reports_description
                        ),
                    checked = crashReportsEnabled,
                    onCheckedChange = { crashReportsEnabled = it },
                )
            }
            ProfileSection(
                stringResource(Res.string.core_ui_onboarding_profile_terms_title),
                required = true,
            ) {
                Row(
                    modifier = Modifier.clickable(onClick = onOpenLegalDocuments),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NavigoCheckbox(
                        checked = legalDocumentsRead,
                        onCheckedChange = null,
                        enabled = legalDocumentsRead,
                    )
                    Text(
                        text =
                            buildAnnotatedString {
                                append(
                                    "${stringResource(Res.string.core_ui_onboarding_profile_terms_prefix).trim()} "
                                )
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        textDecoration = TextDecoration.Underline,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                ) {
                                    append(
                                        stringResource(
                                            Res.string.core_ui_onboarding_profile_terms_link
                                        )
                                    )
                                }
                                append(
                                    " ${stringResource(Res.string.core_ui_onboarding_profile_terms_joiner)} "
                                )
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        textDecoration = TextDecoration.Underline,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                ) {
                                    append(
                                        stringResource(
                                            Res.string.core_ui_onboarding_profile_privacy_link
                                        )
                                    )
                                }
                            },
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
            NavigoButton(
                onClick = onContinue,
                enabled = legalDocumentsRead,
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
            ) {
                Text(stringResource(Res.string.core_ui_onboarding_profile_open_map))
            }
            NavigoTextButton(
                onClick = onContinue,
                enabled = legalDocumentsRead,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(Res.string.core_ui_onboarding_profile_ride_guest))
            }
        }
    }
    if (showColorPicker) {
        ColorSelectionBottomSheet(
            title = stringResource(Res.string.core_ui_onboarding_profile_color_picker_title),
            description =
                stringResource(Res.string.core_ui_onboarding_profile_color_picker_description),
            selectedColor = customColor,
            onApply = { selectedColor ->
                customColor = selectedColor
                avatarColor = selectedColor
                customColorSelected = true
                showColorPicker = false
            },
            onDismissRequest = { showColorPicker = false },
        )
    }
}

@Composable
private fun ProfileSection(
    title: String,
    required: Boolean = false,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.item)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
        ) {
            Text(title.uppercase(), style = MaterialTheme.typography.labelSmall)
            if (required) {
                Text(
                    stringResource(Res.string.core_ui_onboarding_profile_required).uppercase(),
                    modifier =
                        Modifier.background(
                                MaterialTheme.colorScheme.surfaceContainerHighest,
                                MaterialTheme.shapes.medium,
                            )
                            .padding(
                                horizontal = NavigoSpacing.item,
                                vertical = NavigoSpacing.element,
                            ),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
        content()
    }
}

@Composable
private fun ConsentRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.container),
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )
        }
        NavigoSwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@NavigoThemePreview
@Composable
private fun OnboardingProfilePreview() {
    NavigoPreview(contentPadding = PaddingValues(0.dp)) {
        OnboardingProfile(
            legalDocumentsRead = true,
            onOpenLegalDocuments = {},
            onContinue = {},
        )
    }
}
