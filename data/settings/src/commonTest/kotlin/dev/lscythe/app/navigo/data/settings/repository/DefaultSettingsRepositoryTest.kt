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
package dev.lscythe.app.navigo.data.settings.repository

import app.cash.turbine.test
import dev.lscythe.app.navigo.core.persistence.Language
import dev.lscythe.app.navigo.core.persistence.ThemeMode
import dev.lscythe.app.navigo.core.persistence.ThemePreference
import dev.lscythe.app.navigo.core.persistence.datasource.NavigoPreferenceDataSource
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.domain.settings.model.AppSettings
import dev.lscythe.app.navigo.domain.settings.model.PrivacySettings
import dev.lscythe.app.navigo.domain.settings.repository.SettingsResult
import eu.anifantakis.lib.ksafe.KSafe
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DefaultSettingsRepositoryTest :
    FunSpec({
        test("preferences map to settings") {
            withRepository("mapping") { repository, source ->
                source.setLanguage(Language.Indonesian)
                source.setPrivacyChoices(analyticsEnabled = true, crashReportsEnabled = false)
                source.completeOnboarding()
                repository.settings.test {
                    awaitItem() shouldBe
                        AppSettings(
                            AppLanguage.Indonesian,
                            PrivacySettings(true, false),
                            true,
                        )
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        test("all language values map in both directions") {
            val cases =
                listOf(
                    Language.System to AppLanguage.System,
                    Language.English to AppLanguage.English,
                    Language.Indonesian to AppLanguage.Indonesian,
                )
            cases.forEach { (persisted, domain) ->
                withRepository("language_${persisted.name}") { repository, source ->
                    source.setLanguage(persisted)
                    repository.settings.test {
                        awaitItem().language shouldBe domain
                        cancelAndIgnoreRemainingEvents()
                    }
                    repository.updateLanguage(domain) shouldBe SettingsResult.Success(Unit)
                    source.data.test {
                        awaitItem().language shouldBe persisted
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }
        }

        test("language update preserves unrelated fields") {
            withRepository("language_preserves") { repository, source ->
                val theme = ThemePreference(mode = ThemeMode.Dark)
                source.setProfile("Nara", 0u)
                source.setTheme(theme)
                source.setPrivacyChoices(analyticsEnabled = true, crashReportsEnabled = false)

                repository.updateLanguage(AppLanguage.English) shouldBe SettingsResult.Success(Unit)

                source.data.test {
                    awaitItem().let { preference ->
                        preference.displayName shouldBe "Nara"
                        preference.language shouldBe Language.English
                        preference.theme shouldBe theme
                        preference.analyticsEnabled shouldBe true
                    }
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        test("privacy update preserves unrelated fields") {
            withRepository("privacy_preserves") { repository, source ->
                source.setProfile("Nara", 0u)
                source.setLanguage(Language.Indonesian)

                repository.updatePrivacy(PrivacySettings(true, true)) shouldBe
                    SettingsResult.Success(Unit)

                source.data.test {
                    awaitItem().let { preference ->
                        preference.displayName shouldBe "Nara"
                        preference.language shouldBe Language.Indonesian
                        preference.analyticsEnabled shouldBe true
                        preference.crashReportsEnabled shouldBe true
                    }
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    })

private suspend fun withRepository(
    suffix: String,
    block: suspend (DefaultSettingsRepository, NavigoPreferenceDataSource) -> Unit,
) {
    val fileSuffix = suffix.lowercase()
    val ksafe = KSafe(fileName = "navigo_test_settings_repository_$fileSuffix")
    try {
        val source = NavigoPreferenceDataSource(ksafe)
        block(DefaultSettingsRepository(source), source)
    } finally {
        ksafe.clearAll()
        ksafe.close()
    }
}
