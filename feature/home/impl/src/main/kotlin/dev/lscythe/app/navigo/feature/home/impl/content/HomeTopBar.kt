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
package dev.lscythe.app.navigo.feature.home.impl.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.brand.NavigoLogo
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoAvatar
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoBadge
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIconButton
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.status.Notification
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.feature.home.impl.R

@Composable
internal fun HomeTopBar(
    profileInitials: String,
    notificationCount: Int,
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val profileContentDescription = stringResource(R.string.feature_home_profile)
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.background,
        shadowElevation = NavigoSpacing.element,
    ) {
        Row(
            modifier = Modifier.padding(NavigoSpacing.element),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        ) {
            Surface(
                onClick = onSearchClick,
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = NavigoSpacing.element),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
                ) {
                    NavigoIcon(
                        imageVector = NavigoLogo,
                        contentDescription = null,
                        size = 24.dp,
                    )
                    Text(
                        text = stringResource(R.string.feature_home_search_destination),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            Box {
                NavigoIconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.size(48.dp),
                    colors =
                        IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                ) {
                    NavigoIcon(
                        imageVector = NavigoIcons.Notification,
                        contentDescription = stringResource(R.string.feature_home_notifications),
                        size = 24.dp,
                    )
                }
                if (notificationCount > 0) {
                    NavigoBadge(
                        text = notificationCount.toString(),
                        modifier = Modifier.align(Alignment.TopEnd),
                        horizontalPadding = 7.dp,
                        verticalPadding = 1.dp,
                        minHeight = 20.dp,
                    )
                }
            }
            Surface(
                onClick = onProfileClick,
                modifier =
                    Modifier.size(48.dp).semantics {
                        contentDescription = profileContentDescription
                    },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
            ) {
                NavigoAvatar(
                    text = profileInitials,
                    size = 48.dp,
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun HomeTopBarPreview() {
    NavigoPreview { HomeTopBarPreviewContent() }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun HomeTopBarMaterialKolorPreview() {
    NavigoMaterialKolorPreview { HomeTopBarPreviewContent() }
}

@Composable
private fun HomeTopBarPreviewContent() {
    HomeTopBar(
        profileInitials = "AK",
        notificationCount = 3,
        onSearchClick = {},
        onNotificationsClick = {},
        onProfileClick = {},
    )
}
