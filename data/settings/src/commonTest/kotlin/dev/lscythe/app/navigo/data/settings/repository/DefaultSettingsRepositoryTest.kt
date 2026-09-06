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
import dev.lscythe.app.navigo.core.persistence.UserPreference
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.domain.settings.model.AppSettings
import dev.lscythe.app.navigo.domain.settings.model.PrivacySettings
import dev.lscythe.app.navigo.domain.settings.repository.SettingsFailure
import dev.lscythe.app.navigo.domain.settings.repository.SettingsResult
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultSettingsRepositoryTest :
    FunSpec({
        test("preferences map to settings") {
            val preference =
                UserPreference(
                    language = Language.Indonesian,
                    hasCompletedOnboarding = true,
                    analyticsEnabled = true,
                    crashReportsEnabled = false,
                )
            DefaultSettingsRepository(FakeSettingsStore(preference)).settings.test {
                awaitItem() shouldBe
                    AppSettings(
                        AppLanguage.Indonesian,
                        PrivacySettings(true, false),
                        true,
                    )
                cancelAndIgnoreRemainingEvents()
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
                val store = FakeSettingsStore(UserPreference(language = persisted))
                DefaultSettingsRepository(store).settings.test {
                    awaitItem().language shouldBe domain
                    cancelAndIgnoreRemainingEvents()
                }
                DefaultSettingsRepository(store).updateLanguage(domain) shouldBe
                    SettingsResult.Success(Unit)
                store.value.language shouldBe persisted
            }
        }

        test("language update preserves unrelated fields") {
            val theme = ThemePreference(mode = ThemeMode.Dark)
            val store =
                FakeSettingsStore(
                    UserPreference(displayName = "Nara", theme = theme, analyticsEnabled = true)
                )
            DefaultSettingsRepository(store).updateLanguage(AppLanguage.English) shouldBe
                SettingsResult.Success(Unit)
            store.value shouldBe
                UserPreference(
                    displayName = "Nara",
                    language = Language.English,
                    theme = theme,
                    analyticsEnabled = true,
                )
        }

        test("privacy update preserves unrelated fields") {
            val store =
                FakeSettingsStore(
                    UserPreference(displayName = "Nara", language = Language.Indonesian)
                )
            DefaultSettingsRepository(store).updatePrivacy(PrivacySettings(true, true)) shouldBe
                SettingsResult.Success(Unit)
            store.value shouldBe
                UserPreference(
                    displayName = "Nara",
                    language = Language.Indonesian,
                    analyticsEnabled = true,
                    crashReportsEnabled = true,
                )
        }

        test("persistence exception becomes typed failure") {
            val repository =
                DefaultSettingsRepository(
                    FakeSettingsStore(failure = IllegalStateException("disk"))
                )
            repository.updateLanguage(AppLanguage.English) shouldBe
                SettingsResult.Failure(SettingsFailure.Persistence("disk"))
        }

        test("cancellation propagates") {
            val repository =
                DefaultSettingsRepository(FakeSettingsStore(failure = CancellationException()))
            shouldThrow<CancellationException> {
                repository.updatePrivacy(PrivacySettings(true, true))
            }
        }
    })

private class FakeSettingsStore(
    initial: UserPreference = UserPreference(),
    private val failure: Throwable? = null,
) : SettingsPreferenceStore {
    private val state = MutableStateFlow(initial)
    val value
        get() = state.value

    override val data = state

    override suspend fun setLanguage(language: Language) {
        failure?.let { throw it }
        state.value = state.value.copy(language = language)
    }

    override suspend fun setPrivacyChoices(
        analyticsEnabled: Boolean,
        crashReportsEnabled: Boolean,
    ) {
        failure?.let { throw it }
        state.value =
            state.value.copy(
                analyticsEnabled = analyticsEnabled,
                crashReportsEnabled = crashReportsEnabled,
            )
    }
}
