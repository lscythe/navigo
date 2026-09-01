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
package dev.lscythe.app.navigo.core.datastore.datasource

import dev.lscythe.app.navigo.core.datastore.Language
import dev.lscythe.app.navigo.core.datastore.ThemePreference
import dev.lscythe.app.navigo.core.datastore.UserPreference
import dev.lscythe.app.navigo.core.datastore.di.PreferencesKSafe
import dev.zacsweers.metro.Inject
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeWriteMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val USER_PREFERENCE_KEY = "user_preference"

@Inject
class NavigoPreferenceDataSource(@PreferencesKSafe private val ksafe: KSafe) {
    private val mutationMutex = Mutex()

    val data: Flow<UserPreference> = ksafe.getFlow(USER_PREFERENCE_KEY, UserPreference())

    suspend fun setLanguage(language: Language) = update { copy(language = language) }

    suspend fun setTheme(theme: ThemePreference) = update { copy(theme = theme) }

    suspend fun completeOnboarding() = update { copy(hasCompletedOnboarding = true) }

    private suspend fun update(transform: UserPreference.() -> UserPreference) {
        mutationMutex.withLock {
            val current = ksafe.get(USER_PREFERENCE_KEY, UserPreference())
            ksafe.put(
                key = USER_PREFERENCE_KEY,
                value = current.transform(),
                mode = KSafeWriteMode.Plain,
            )
        }
    }
}
