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

import androidx.compose.runtime.Composable
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview

@NavigoThemePreview
@Composable
private fun OnboardingFeedPagePreview() {
    NavigoPreview {
        OnboardingFeedPage()
    }
}

@NavigoThemePreview
@Composable
private fun OnboardingGpsPagePreview() {
    NavigoPreview {
        OnboardingGpsPage()
    }
}

@NavigoThemePreview
@Composable
private fun OnboardingAlarmPagePreview() {
    NavigoPreview {
        OnboardingAlarmPage()
    }
}

@NavigoThemePreview
@Composable
private fun OnboardingPageCheckListPreview() {
    NavigoPreview {
        OnboardingPageCheckList("First Item")
    }
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
