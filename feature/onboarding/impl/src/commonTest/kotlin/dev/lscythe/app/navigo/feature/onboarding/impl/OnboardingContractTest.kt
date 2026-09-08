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
package dev.lscythe.app.navigo.feature.onboarding.impl

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe

class OnboardingContractTest :
    FunSpec({
        test("default state is an immutable Indonesian onboarding snapshot") {
            val state = OnboardingUiState()

            state.language shouldBe OnboardingLanguage.English
            state.displayName shouldBe ""
            state.avatarColorArgb shouldBe DefaultAvatarColorArgb
            state.analyticsEnabled.shouldBeFalse()
            state.crashReportsEnabled.shouldBeFalse()
            state.legalDocuments shouldBe null
            state.legalAccepted.shouldBeFalse()
            state.legalLoading.shouldBeFalse()
            state.completionLoading.shouldBeFalse()
        }

        test("contract exposes distinct completion intents and navigation effect") {
            OnboardingIntent.OpenMapClicked shouldBe OnboardingIntent.OpenMapClicked
            OnboardingIntent.RideAsGuestClicked shouldBe OnboardingIntent.RideAsGuestClicked
            OnboardingEffect.NavigateHome shouldBe OnboardingEffect.NavigateHome
        }
    })
