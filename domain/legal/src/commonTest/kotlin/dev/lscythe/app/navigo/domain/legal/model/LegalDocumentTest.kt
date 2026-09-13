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
package dev.lscythe.app.navigo.domain.legal.model

import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import kotlinx.datetime.LocalDate

class LegalDocumentTest :
    FunSpec({
        test("published document requires effective date") {
            shouldThrow<IllegalArgumentException> {
                document(LegalDocumentType.Terms, effectiveAt = null)
            }
        }
        test("document requires positive reading time") {
            shouldThrow<IllegalArgumentException> {
                document(LegalDocumentType.Terms, readingTimeMinutes = 0)
            }
        }
        test("document rejects system language") {
            shouldThrow<IllegalArgumentException> {
                document(LegalDocumentType.Terms, language = AppLanguage.System)
            }
        }
        test("set requires matching document types") {
            val terms = document(LegalDocumentType.Terms)
            shouldThrow<IllegalArgumentException> { LegalDocumentSet(terms, terms) }
        }
    })

private fun document(
    type: LegalDocumentType,
    language: AppLanguage = AppLanguage.English,
    effectiveAt: LocalDate? = LocalDate(2026, 1, 1),
    readingTimeMinutes: Int = 1,
) =
    LegalDocument(
        type,
        language,
        LegalDocumentStatus.Published,
        "1",
        effectiveAt,
        "Title",
        readingTimeMinutes,
        "<p>S</p>",
        "<p>B</p>",
        "https://example.com",
        LegalDocumentSource.Network,
    )
