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
package dev.lscythe.app.navigo.core.persistence.datasource

import dev.lscythe.app.navigo.core.persistence.LegalDocumentCachePreference
import eu.anifantakis.lib.ksafe.KSafe
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class LegalDocumentCacheDataSourceTest :
    FunSpec({
        test("cache records are independent by type and language") {
            val ksafe = KSafe(fileName = "navigo_test_legal_keys")
            try {
                val source = LegalDocumentCacheDataSource(ksafe)
                val terms = record("terms", "en", "terms-etag")
                val privacy = record("privacy", "id", "privacy-etag")
                source.put(terms)
                source.put(privacy)
                source.get("terms", "en") shouldBe terms
                source.get("privacy", "id") shouldBe privacy
                source.get("terms", "id") shouldBe null
            } finally {
                ksafe.clearAll()
                ksafe.close()
            }
        }

        test("put atomically replaces document and metadata") {
            val ksafe = KSafe(fileName = "navigo_test_legal_replace")
            try {
                val source = LegalDocumentCacheDataSource(ksafe)
                source.put(record("terms", "en", "old"))
                val replacement =
                    record("terms", "en", "new")
                        .copy(bodyHtml = "new body", cacheControl = "max-age=60")
                source.put(replacement)
                source.get("terms", "en") shouldBe replacement
            } finally {
                ksafe.clearAll()
                ksafe.close()
            }
        }
    })

private fun record(type: String, language: String, etag: String) =
    LegalDocumentCachePreference(
        type = type,
        language = language,
        status = "published",
        version = "1",
        effectiveAt = "2026-01-01",
        title = "Title",
        readingTimeMinutes = 1,
        summaryHtml = "summary",
        bodyHtml = "body",
        canonicalUrl = "https://example.com",
        etag = etag,
        contentLanguage = language,
        cacheControl = "max-age=0",
    )
