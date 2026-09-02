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
package dev.lscythe.app.navigo.feature.onboarding.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.lscythe.app.navigo.core.navigation.Navigator
import dev.lscythe.app.navigo.feature.home.api.navigateToHome
import dev.lscythe.app.navigo.feature.onboarding.api.OnboardingNavKey
import dev.lscythe.app.navigo.feature.onboarding.impl.OnboardingRoute

fun EntryProviderScope<NavKey>.onboardingEntry(navigator: Navigator) {
    entry<OnboardingNavKey> {
        OnboardingRoute(navigateHome = navigator::navigateToHome)
    }
}
