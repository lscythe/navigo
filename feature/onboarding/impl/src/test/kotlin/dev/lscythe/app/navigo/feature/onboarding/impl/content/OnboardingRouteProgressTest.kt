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

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.floats.shouldBeWithinPercentageOf

class OnboardingRouteProgressTest :
    FunSpec({
        test("settled pages align with route stops") {
            routeFractionForPagerProgress(0f).shouldBeWithinPercentageOf(0.18f, 0.001)
            routeFractionForPagerProgress(1f).shouldBeWithinPercentageOf(0.5f, 0.001)
            routeFractionForPagerProgress(2f).shouldBeWithinPercentageOf(0.82f, 0.001)
        }

        test("pager motion interpolates in either direction") {
            routeFractionForPagerProgress(0.5f).shouldBeWithinPercentageOf(0.34f, 0.001)
            routeFractionForPagerProgress(1.5f).shouldBeWithinPercentageOf(0.66f, 0.001)
            routeFractionForPagerProgress(1.25f).shouldBeWithinPercentageOf(0.58f, 0.001)
        }

        test("pager progress stays within the route") {
            routeFractionForPagerProgress(-1f).shouldBeWithinPercentageOf(0.18f, 0.001)
            routeFractionForPagerProgress(3f).shouldBeWithinPercentageOf(0.82f, 0.001)
        }
    })
