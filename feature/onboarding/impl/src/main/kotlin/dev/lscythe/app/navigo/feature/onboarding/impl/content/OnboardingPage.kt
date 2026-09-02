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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.lscythe.app.navigo.core.designsystem.brand.NavigoLogo
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoAvatar
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoCard
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoAlertCard
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoDisclosureNote
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.action.Check
import dev.lscythe.app.navigo.core.designsystem.icon.status.ExclamationCircle
import dev.lscythe.app.navigo.core.designsystem.icon.status.Signal
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.ui.generated.resources.Res
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_app_name
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_alarm_notification_sample_category
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_alarm_notification_sample_description
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_alarm_notification_sample_time
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_alarm_notification_sample_title
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_alarm_page_description
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_alarm_page_first_item
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_alarm_page_second_item
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_alarm_page_title
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_feed_page_description
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_feed_page_first_item
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_feed_page_second_item
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_feed_page_third_item
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_feed_page_title
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_gps_disclosure_note
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_gps_feed_description
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_onboarding_gps_feed_title
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

private val feedCheckList =
    persistentListOf(
        Res.string.core_ui_onboarding_feed_page_first_item,
        Res.string.core_ui_onboarding_feed_page_second_item,
        Res.string.core_ui_onboarding_feed_page_third_item,
    )

private val gpsRoutes =
    persistentListOf(
        GpsRoute(
            number = "14",
            eta = "4 min",
            status = "seats free",
            source = "Operator GPS + 6 riders",
            updatedAt = "40s ago",
            quality = 3,
            enabled = true,
        ),
        GpsRoute(
            number = "27B",
            eta = "7 min",
            status = "packed, 2 passed full",
            source = "No tracker + riders only",
            updatedAt = "3 min ago",
            quality = 1,
            enabled = false,
        ),
    )

private val alarmCheckList =
    persistentListOf(
        Res.string.core_ui_onboarding_alarm_page_first_item,
        Res.string.core_ui_onboarding_alarm_page_second_item,
    )

@Composable
internal fun OnboardingPageIntro(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )
    }
}

@Composable
internal fun OnboardingFeedPage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = NavigoSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
    ) {
        OnboardingPageIntro(
            title = stringResource(Res.string.core_ui_onboarding_feed_page_title),
            description = stringResource(Res.string.core_ui_onboarding_feed_page_description),
        )
        Column(
            modifier = Modifier.padding(top = NavigoSpacing.element),
            verticalArrangement = Arrangement.spacedBy(NavigoSpacing.micro),
        ) {
            feedCheckList.forEach { feed ->
                OnboardingPageCheckList(stringResource(feed))
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun OnboardingFeedPagePreview() {
    NavigoPreview {
        OnboardingFeedPage()
    }
}

@Composable
internal fun OnboardingGpsPage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = NavigoSpacing.screen).fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.item)) {
            OnboardingPageIntro(
                title = stringResource(Res.string.core_ui_onboarding_gps_feed_title),
                description = stringResource(Res.string.core_ui_onboarding_gps_feed_description),
            )
            Column(
                modifier = Modifier.padding(top = NavigoSpacing.element),
                verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
            ) {
                gpsRoutes.forEach { route ->
                    OnboardingGpsPageRouteItem(
                        route = route,
                        enabled = route.enabled,
                    )
                }
            }
        }

        NavigoDisclosureNote(
            text = stringResource(Res.string.core_ui_onboarding_gps_disclosure_note),
            icon = NavigoIcons.ExclamationCircle,
            iconTint = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.secondary.copy(0.2f),
            contentColor = MaterialTheme.colorScheme.onBackground,
            alignment = Alignment.Top,
            textStyle = MaterialTheme.typography.bodyMedium,
        )
    }
}

@NavigoThemePreview
@Composable
private fun OnboardingGpsPagePreview() {
    NavigoPreview {
        OnboardingGpsPage()
    }
}

@Composable
internal fun OnboardingAlarmPage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = NavigoSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
    ) {
        OnboardingPageIntro(
            title = stringResource(Res.string.core_ui_onboarding_alarm_page_title),
            description = stringResource(Res.string.core_ui_onboarding_alarm_page_description),
        )
        NavigoAlertCard(
            source = stringResource(Res.string.core_ui_app_name).uppercase(),
            category =
                stringResource(Res.string.core_ui_onboarding_alarm_notification_sample_category)
                    .uppercase(),
            title = stringResource(Res.string.core_ui_onboarding_alarm_notification_sample_title),
            description =
                stringResource(Res.string.core_ui_onboarding_alarm_notification_sample_description),
            timestamp =
                stringResource(Res.string.core_ui_onboarding_alarm_notification_sample_time)
                    .lowercase(),
            icon = NavigoLogo,
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = NavigoSpacing.element),
        )
        Column(
            modifier = Modifier.padding(top = NavigoSpacing.element),
            verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        ) {
            alarmCheckList.forEach { feed ->
                OnboardingPageCheckList(stringResource(feed))
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun OnboardingAlarmPagePreview() {
    NavigoPreview {
        OnboardingAlarmPage()
    }
}

@Composable
private fun OnboardingPageCheckList(
    item: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NavigoIcon(
            NavigoIcons.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            size = 18.dp,
        )
        Text(
            item,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@NavigoThemePreview
@Composable
private fun OnboardingPageCheckListPreview() {
    NavigoPreview {
        OnboardingPageCheckList("First Item")
    }
}

private data class GpsRoute(
    val number: String,
    val eta: String,
    val status: String,
    val source: String,
    val updatedAt: String,
    val quality: Int,
    val enabled: Boolean,
)

@Composable
private fun OnboardingGpsPageRouteItem(
    route: GpsRoute,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
) {
    val color =
        if (enabled) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceContainerHigh

    val avatarTextColor =
        if (enabled) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    val textColor =
        if (enabled) MaterialTheme.colorScheme.onBackground
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)

    val cardContainerColor = color.copy(alpha = 0.34f)

    NavigoCard(
        modifier = modifier,
        border = BorderStroke(1.2.dp, color),
        containerColor = cardContainerColor,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NavigoAvatar(
                text = route.number,
                containerColor = color,
                contentColor = avatarTextColor,
                size = 40.dp,
                shape = MaterialTheme.shapes.medium,
                fontSize = 14.sp,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(NavigoSpacing.micro),
            ) {
                Text(
                    "${route.eta} · ${route.status}",
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor,
                    fontSize = 14.sp,
                )
                Text(
                    "${route.source} · ${route.updatedAt}",
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor,
                    fontSize = 10.sp,
                )
            }
            NavigoIcon(
                imageVector = route.quality.signalIcon,
                contentDescription = null,
                size = 32.dp,
                tint = color,
            )
        }
    }
}

private val Int.signalIcon
    get() =
        when {
            this <= 0 -> NavigoIcons.Signal.None
            this == 1 -> NavigoIcons.Signal.Weak
            this == 2 -> NavigoIcons.Signal.Medium
            else -> NavigoIcons.Signal.Strong
        }

@NavigoThemePreview
@Composable
private fun OnboardingGpsPageRouteItemPreview() {
    NavigoPreview {
        OnboardingGpsPageRouteItem(
            route =
                GpsRoute(
                    number = "14",
                    eta = "4 min",
                    status = "seats free",
                    source = "Official GPS + 6 riders",
                    updatedAt = "3 min ago",
                    quality = 2,
                    enabled = true,
                )
        )
    }
}
