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
package dev.lscythe.app.navigo.data.user.repository

import app.cash.turbine.test
import dev.lscythe.app.navigo.core.persistence.ThemeMode
import dev.lscythe.app.navigo.core.persistence.ThemePreference
import dev.lscythe.app.navigo.core.persistence.datasource.NavigoPreferenceDataSource
import dev.lscythe.app.navigo.domain.user.model.UserProfile
import dev.lscythe.app.navigo.domain.user.repository.UserFailure
import dev.lscythe.app.navigo.domain.user.repository.UserResult
import eu.anifantakis.lib.ksafe.KSafe
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DefaultUserRepositoryTest :
    FunSpec({
        test("blank persisted profile maps to null") {
            withRepository("blank") { repository, _ ->
                repository.profile.test {
                    awaitItem() shouldBe null
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        test("persisted profile maps to domain profile") {
            withRepository("persisted") { repository, dataSource ->
                dataSource.setProfile("Nara", 0xff123456u)
                repository.profile.test {
                    awaitItem() shouldBe UserProfile("Nara", 0xff123456u)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        test("update trims display name and preserves unrelated preferences") {
            withRepository("update") { repository, dataSource ->
                val theme = ThemePreference(mode = ThemeMode.Dark)
                dataSource.setTheme(theme)

                repository.updateProfile(UserProfile("  Nara  ", 0xff123456u)) shouldBe
                    UserResult.Success(Unit)

                dataSource.data.test {
                    awaitItem().let { preference ->
                        preference.displayName shouldBe "Nara"
                        preference.avatarColorArgb shouldBe 0xff123456u
                        preference.theme shouldBe theme
                    }
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        test("blank update fails before persistence") {
            withRepository("invalid") { repository, dataSource ->
                repository.updateProfile(UserProfile("  ", 1u)) shouldBe
                    UserResult.Failure(UserFailure.InvalidDisplayName)
                dataSource.data.test {
                    awaitItem().displayName shouldBe ""
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    })

private suspend fun withRepository(
    suffix: String,
    block: suspend (DefaultUserRepository, NavigoPreferenceDataSource) -> Unit,
) {
    val ksafe = KSafe(fileName = "navigo_test_user_repository_$suffix")
    try {
        val dataSource = NavigoPreferenceDataSource(ksafe)
        block(DefaultUserRepository(dataSource), dataSource)
    } finally {
        ksafe.clearAll()
        ksafe.close()
    }
}
