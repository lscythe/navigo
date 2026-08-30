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
package dev.lscythe.app.navigo.feature.onboarding.impl.legal

import dev.lscythe.app.navigo.core.common.locale.SupportedLanguage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class LegalReadingStateTest :
    FunSpec({
        test("asset paths use the selected supported language") {
            legalAssetPath(LegalDocumentType.Terms, SupportedLanguage.Indonesian) shouldBe
                "terms/id.json"
            legalAssetPath(LegalDocumentType.Privacy, SupportedLanguage.English) shouldBe
                "privacy/en.json"
            legalAssetPath(LegalDocumentType.Terms, null) shouldBe "terms/en.json"
        }

        test("terms completion continues to privacy") {
            val state = LegalReadingState().markRead(LegalDocumentType.Terms)

            state.primaryAction shouldBe LegalPrimaryAction.ContinueToPrivacy
            state.onPrimaryAction().activeDocument shouldBe LegalDocumentType.Privacy
        }

        test("privacy completion alone returns to unread terms") {
            val state =
                LegalReadingState(activeDocument = LegalDocumentType.Privacy)
                    .markRead(LegalDocumentType.Privacy)

            state.primaryAction shouldBe LegalPrimaryAction.ContinueToTerms
            state.onPrimaryAction().activeDocument shouldBe LegalDocumentType.Terms
        }

        test("both completed documents enable acceptance") {
            val state =
                LegalReadingState()
                    .markRead(LegalDocumentType.Terms)
                    .open(LegalDocumentType.Privacy)
                    .markRead(LegalDocumentType.Privacy)

            state.primaryAction shouldBe LegalPrimaryAction.Accept
        }
    })
