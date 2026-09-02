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

internal fun legalHtmlSections(html: String): List<String> {
    val sections = mutableListOf<String>()
    var start = html.indexOf("<section>")
    while (start >= 0) {
        val contentStart = start + "<section>".length
        val end = html.indexOf("</section>", contentStart)
        if (end < 0) break
        sections += html.substring(contentStart, end)
        start = html.indexOf("<section>", end + "</section>".length)
    }
    return sections.ifEmpty { listOf(html) }
}
