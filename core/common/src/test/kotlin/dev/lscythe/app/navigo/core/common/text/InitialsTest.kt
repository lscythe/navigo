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
package dev.lscythe.app.navigo.core.common.text

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class InitialsTest :
    FunSpec({
        test("uses the first two non-empty words") {
            "  Ayu   Kusuma Putri  ".toInitials() shouldBe "AK"
        }

        test("uses one initial for one word") {
            "Ayu".toInitials() shouldBe "A"
        }

        test("uses the supplied fallback for blank text") {
            "  ".toInitials(fallback = "?") shouldBe "?"
        }
    })
