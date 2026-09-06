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

import dev.lscythe.app.navigo.core.persistence.UserPreference
import dev.lscythe.app.navigo.core.persistence.datasource.NavigoPreferenceDataSource
import dev.lscythe.app.navigo.domain.user.model.UserProfile
import dev.lscythe.app.navigo.domain.user.repository.UserFailure
import dev.lscythe.app.navigo.domain.user.repository.UserRepository
import dev.lscythe.app.navigo.domain.user.repository.UserResult
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserPreferenceStore {
    val data: Flow<UserPreference>

    suspend fun setProfile(displayName: String, avatarColorArgb: UInt)
}

@Inject
@ContributesBinding(AppScope::class)
class PersistentUserPreferenceStore(private val dataSource: NavigoPreferenceDataSource) :
    UserPreferenceStore {
    override val data = dataSource.data

    override suspend fun setProfile(displayName: String, avatarColorArgb: UInt) =
        dataSource.setProfile(displayName, avatarColorArgb)
}

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DefaultUserRepository(private val store: UserPreferenceStore) : UserRepository {
    override val profile: Flow<UserProfile?> =
        store.data.map { preference ->
            preference.displayName.takeIf(String::isNotBlank)?.let {
                UserProfile(it, preference.avatarColorArgb)
            }
        }

    override suspend fun updateProfile(profile: UserProfile): UserResult<Unit> {
        val displayName = profile.displayName.trim()
        if (displayName.isEmpty()) return UserResult.Failure(UserFailure.InvalidDisplayName)

        return try {
            store.setProfile(displayName, profile.avatarColorArgb)
            UserResult.Success(Unit)
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            UserResult.Failure(UserFailure.Persistence(error.message))
        }
    }
}
