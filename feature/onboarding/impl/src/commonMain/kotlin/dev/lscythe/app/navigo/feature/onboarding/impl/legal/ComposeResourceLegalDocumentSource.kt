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

import dev.lscythe.app.navigo.data.legal.repository.BundledLegalDocumentSource
import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentType
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.feature.onboarding.impl.generated.resources.Res
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class)
class ComposeResourceLegalDocumentSource : BundledLegalDocumentSource {
    override suspend fun read(type: LegalDocumentType, language: AppLanguage): String? =
        runCatching {
            Res.readBytes("files/${type.directory}/${language.tag}.json").decodeToString()
        }
        .getOrNull()
}

private val LegalDocumentType.directory: String
    get() =
        when (this) {
            LegalDocumentType.Terms -> "terms"
            LegalDocumentType.Privacy -> "privacy"
        }

private val AppLanguage.tag: String
    get() =
        when (this) {
            AppLanguage.English -> "en"
            AppLanguage.Indonesian -> "id"
            AppLanguage.System -> error("Bundled legal documents require an explicit language")
        }
