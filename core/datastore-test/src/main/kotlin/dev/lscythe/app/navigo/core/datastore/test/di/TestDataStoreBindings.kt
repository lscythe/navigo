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
package dev.lscythe.app.navigo.core.datastore.test.di

import androidx.datastore.core.DataStore
import dev.lscythe.app.navigo.core.datastore.SessionPreference
import dev.lscythe.app.navigo.core.datastore.UserPreference
import dev.lscythe.app.navigo.core.datastore.di.DataStoreBindings
import dev.lscythe.app.navigo.core.datastore.di.EncryptedPreference
import dev.lscythe.app.navigo.core.datastore.serializer.SessionPreferenceSerializer
import dev.lscythe.app.navigo.core.datastore.serializer.UserPreferenceSerializer
import dev.lscythe.app.navigo.core.datastore.test.InMemoryDataStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class, replaces = [DataStoreBindings::class])
@BindingContainer
object TestDataStoreBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideUserPreferenceDataStore(): DataStore<UserPreference> =
        InMemoryDataStore(UserPreferenceSerializer.defaultValue)

    @Provides
    @EncryptedPreference
    @SingleIn(AppScope::class)
    fun provideSessionPreferenceDataStore(): DataStore<SessionPreference> =
        InMemoryDataStore(SessionPreferenceSerializer.defaultValue)
}
