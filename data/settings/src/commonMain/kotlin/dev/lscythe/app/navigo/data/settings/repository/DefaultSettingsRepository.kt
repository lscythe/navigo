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

import dev.lscythe.app.navigo.core.persistence.Language
import dev.lscythe.app.navigo.core.persistence.UserPreference
import dev.lscythe.app.navigo.core.persistence.datasource.NavigoPreferenceDataSource
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage
import dev.lscythe.app.navigo.domain.settings.model.AppSettings
import dev.lscythe.app.navigo.domain.settings.model.PrivacySettings
import dev.lscythe.app.navigo.domain.settings.repository.SettingsFailure
import dev.lscythe.app.navigo.domain.settings.repository.SettingsRepository
import dev.lscythe.app.navigo.domain.settings.repository.SettingsResult
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DefaultSettingsRepository(private val dataSource: NavigoPreferenceDataSource) :
    SettingsRepository {
    override val settings: Flow<AppSettings> = dataSource.data.map(UserPreference::toDomain)

    override suspend fun updateLanguage(language: AppLanguage): SettingsResult<Unit> = persist {
        dataSource.setLanguage(language.toPersistence())
    }

    override suspend fun updatePrivacy(privacy: PrivacySettings): SettingsResult<Unit> = persist {
        dataSource.setPrivacyChoices(privacy.analyticsEnabled, privacy.crashReportsEnabled)
    }

    private suspend inline fun persist(operation: () -> Unit): SettingsResult<Unit> =
        try {
            operation()
            SettingsResult.Success(Unit)
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            SettingsResult.Failure(SettingsFailure.Persistence(error.message))
        }
}

private fun UserPreference.toDomain() =
    AppSettings(
        language = language.toDomain(),
        privacy = PrivacySettings(analyticsEnabled, crashReportsEnabled),
        hasCompletedOnboarding = hasCompletedOnboarding,
    )

private fun Language.toDomain() =
    when (this) {
        Language.System -> AppLanguage.System
        Language.English -> AppLanguage.English
        Language.Indonesian -> AppLanguage.Indonesian
    }

private fun AppLanguage.toPersistence() =
    when (this) {
        AppLanguage.System -> Language.System
        AppLanguage.English -> Language.English
        AppLanguage.Indonesian -> Language.Indonesian
    }
