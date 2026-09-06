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

import dev.lscythe.app.navigo.core.persistence.Language
import dev.lscythe.app.navigo.core.persistence.SessionPreference
import dev.lscythe.app.navigo.core.persistence.ThemeMode
import dev.lscythe.app.navigo.core.persistence.ThemePreference
import eu.anifantakis.lib.ksafe.KSafe
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first

class KSafeDataSourceTest :
    FunSpec({
        test("preference mutations preserve concurrently updated fields") {
            val ksafe = isolatedKSafe("preferences")
            try {
                val dataSource = NavigoPreferenceDataSource(ksafe)
                listOf(
                        async { dataSource.setLanguage(Language.Indonesian) },
                        async { dataSource.setTheme(ThemePreference(mode = ThemeMode.Dark)) },
                        async { dataSource.completeOnboarding() },
                    )
                    .awaitAll()

                dataSource.data.first().let { preference ->
                    preference.language shouldBe Language.Indonesian
                    preference.theme shouldBe ThemePreference(mode = ThemeMode.Dark)
                    preference.hasCompletedOnboarding shouldBe true
                }
                ksafe.getKeyInfo("user_preference")?.protection shouldBe null
            } finally {
                ksafe.clearAll()
                ksafe.close()
            }
        }

        test("session set and clear emit encrypted values") {
            val ksafe = isolatedKSafe("session")
            try {
                val dataSource = SessionPreferenceDataSource(ksafe)
                val session =
                    SessionPreference(
                        id = "session-id",
                        accessToken = "access",
                        refreshToken = "refresh",
                    )

                dataSource.setSession(session)
                dataSource.data.first() shouldBe session
                ksafe.getKeyInfo("session_preference")?.protection shouldNotBe null

                dataSource.clear()
                dataSource.data.first() shouldBe SessionPreference()
            } finally {
                ksafe.clearAll()
                ksafe.close()
            }
        }
    })

private fun isolatedKSafe(suffix: String): KSafe = KSafe(fileName = "navigo_test_$suffix")
