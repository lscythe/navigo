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

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.floats.shouldBeExactly

class NearbyStopsSheetStateTest :
    FunSpec({
        test("sheet exposes collapsed half and expanded anchors") {
            val anchors = nearbyStopsSheetAnchors(screenHeight = 1_000f, collapsedHeight = 240f)

            anchors.getValue(NearbyStopsSheetValue.Collapsed).shouldBeExactly(760f)
            anchors.getValue(NearbyStopsSheetValue.HalfExpanded).shouldBeExactly(500f)
            anchors.getValue(NearbyStopsSheetValue.Expanded).shouldBeExactly(0f)
        }

        test("collapsed anchor never rises above half expanded") {
            val anchors = nearbyStopsSheetAnchors(screenHeight = 400f, collapsedHeight = 260f)

            anchors.getValue(NearbyStopsSheetValue.Collapsed).shouldBeExactly(200f)
            anchors.getValue(NearbyStopsSheetValue.HalfExpanded).shouldBeExactly(200f)
        }
    })
