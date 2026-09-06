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
import io.kotest.matchers.shouldBe

class HomeMapConfigTest :
    FunSpec({
        test("light theme selects light map style") {
            homeMapStyleUri(isDark = false) shouldBe
                "https://transit-basemap.pages.dev/style-light.json"
        }

        test("dark theme selects dark map style") {
            homeMapStyleUri(isDark = true) shouldBe
                "https://transit-basemap.pages.dev/style-dark.json"
        }
    })
