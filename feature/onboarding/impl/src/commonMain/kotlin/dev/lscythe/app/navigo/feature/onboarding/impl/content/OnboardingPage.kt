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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.action.Check
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.resources.generated.resources.Res
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_feed_page_description
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_feed_page_first_item
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_feed_page_second_item
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_feed_page_third_item
import dev.lscythe.app.navigo.core.resources.generated.resources.onboarding_feed_page_title
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

private val feedCheckList =
    persistentListOf(
        Res.string.onboarding_feed_page_first_item,
        Res.string.onboarding_feed_page_second_item,
        Res.string.onboarding_feed_page_third_item,
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
            title = stringResource(Res.string.onboarding_feed_page_title),
            description = stringResource(Res.string.onboarding_feed_page_description),
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

@Composable
internal fun OnboardingPageCheckList(
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
