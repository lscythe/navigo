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
package dev.lscythe.app.navigo.domain.settings.repository

import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.domain.settings.model.AppSettings
import dev.lscythe.app.navigo.domain.settings.model.PrivacySettings
import kotlinx.coroutines.flow.Flow

/** Stable failures exposed by settings operations. */
sealed interface SettingsFailure {
    data class Persistence(val message: String? = null) : SettingsFailure

    data class Unknown(val message: String? = null) : SettingsFailure
}

/** Result of a settings operation. */
sealed interface SettingsResult<out T> {
    data class Success<T>(val value: T) : SettingsResult<T>

    data class Failure(val failure: SettingsFailure) : SettingsResult<Nothing>
}

/** Reads and independently updates reusable application settings. */
interface SettingsRepository {
    val settings: Flow<AppSettings>

    suspend fun updateLanguage(language: AppLanguage): SettingsResult<Unit>

    suspend fun updatePrivacy(privacy: PrivacySettings): SettingsResult<Unit>
}
