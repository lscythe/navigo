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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.lscythe.app.navigo.core.designsystem.brand.NavigoLogo
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoAlertCard
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.resources.generated.resources.Res
import dev.lscythe.app.navigo.core.resources.generated.resources.app_name
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_alarm_notification_sample_category
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_alarm_notification_sample_description
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_alarm_notification_sample_time
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_alarm_notification_sample_title
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_alarm_page_description
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_alarm_page_first_item
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_alarm_page_second_item
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_alarm_page_title
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

private val alarmCheckList =
    persistentListOf(
        Res.string.onboarding_alarm_page_first_item,
        Res.string.onboarding_alarm_page_second_item,
    )

@Composable
internal fun OnboardingAlarmPage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = NavigoSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(NavigoSpacing.item),
    ) {
        OnboardingPageIntro(
            title = stringResource(Res.string.onboarding_alarm_page_title),
            description = stringResource(Res.string.onboarding_alarm_page_description),
        )
        NavigoAlertCard(
            source = stringResource(Res.string.app_name).uppercase(),
            category =
                stringResource(Res.string.onboarding_alarm_notification_sample_category)
                    .uppercase(),
            title = stringResource(Res.string.onboarding_alarm_notification_sample_title),
            description =
                stringResource(Res.string.onboarding_alarm_notification_sample_description),
            timestamp =
                stringResource(Res.string.onboarding_alarm_notification_sample_time).lowercase(),
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
