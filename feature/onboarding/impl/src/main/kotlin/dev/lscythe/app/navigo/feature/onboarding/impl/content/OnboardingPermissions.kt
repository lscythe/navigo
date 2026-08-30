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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoTextButton
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.map.MapPin
import dev.lscythe.app.navigo.core.designsystem.icon.status.Notification
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.ui.R

@Composable
internal fun OnboardingPermissions(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = NavigoSpacing.screen),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.section)) {
            OnboardingPageIntro(
                title = stringResource(R.string.core_ui_onboarding_permissions_title),
                description = stringResource(R.string.core_ui_onboarding_permissions_description),
            )
            Column {
                PermissionRow(
                    icon = NavigoIcons.MapPin,
                    title = stringResource(R.string.core_ui_onboarding_permissions_location_title),
                    description =
                        stringResource(
                            R.string.core_ui_onboarding_permissions_location_description
                        ),
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = NavigoSpacing.container))
                PermissionRow(
                    icon = NavigoIcons.Notification,
                    title =
                        stringResource(R.string.core_ui_onboarding_permissions_notifications_title),
                    description =
                        stringResource(
                            R.string.core_ui_onboarding_permissions_notifications_description
                        ),
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
            NavigoButton(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
            ) {
                Text(stringResource(R.string.core_ui_onboarding_permissions_allow_now))
            }
            NavigoTextButton(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.core_ui_onboarding_permissions_ask_later))
            }
        }
    }
}

@Composable
private fun PermissionRow(
    icon: ImageVector,
    title: String,
    description: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.container)) {
        NavigoIcon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = NavigoSpacing.micro),
        )
        Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.micro)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )
        }
    }
}

@NavigoThemePreview
@Composable
private fun OnboardingPermissionsPreview() {
    NavigoPreview(contentPadding = PaddingValues(0.dp)) {
        OnboardingPermissions(onContinue = {})
    }
}
