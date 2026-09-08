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
import androidx.compose.ui.tooling.preview.Preview
import dev.lscythe.app.navigo.core.designsystem.preview.DesktopPreview

@Preview
@Composable
private fun OnboardingFeedPagePreview() {
    DesktopPreview {
        OnboardingFeedPage()
    }
}

@Preview
@Composable
private fun OnboardingGpsPagePreview() {
    DesktopPreview {
        OnboardingGpsPage()
    }
}

@Preview
@Composable
private fun OnboardingAlarmPagePreview() {
    DesktopPreview {
        OnboardingAlarmPage()
    }
}

@Preview
@Composable
private fun OnboardingPageCheckListPreview() {
    DesktopPreview {
        OnboardingPageCheckList("First Item")
    }
}

@Preview
@Composable
private fun OnboardingGpsPageRouteItemPreview() {
    DesktopPreview {
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
