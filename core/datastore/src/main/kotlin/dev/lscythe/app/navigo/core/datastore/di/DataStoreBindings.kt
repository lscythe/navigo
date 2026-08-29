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
package dev.lscythe.app.navigo.core.datastore.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import dev.lscythe.app.navigo.core.common.coroutines.ApplicationScope
import dev.lscythe.app.navigo.core.common.dispatchers.Dispatcher
import dev.lscythe.app.navigo.core.common.dispatchers.NavigoDispatchers
import dev.lscythe.app.navigo.core.datastore.SessionPreference
import dev.lscythe.app.navigo.core.datastore.UserPreference
import dev.lscythe.app.navigo.core.datastore.serializer.EncryptedSerializer
import dev.lscythe.app.navigo.core.datastore.serializer.SessionPreferenceSerializer
import dev.lscythe.app.navigo.core.datastore.serializer.UserPreferenceSerializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@ContributesTo(AppScope::class)
@BindingContainer
object DataStoreBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideUserPreferenceDataStore(
        application: Application,
        @ApplicationScope scope: CoroutineScope,
        @Dispatcher(NavigoDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): DataStore<UserPreference> =
        DataStoreFactory.create(
            serializer = UserPreferenceSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            application.dataStoreFile("user_preference.pb")
        }

    @Provides
    @EncryptedPreference
    @SingleIn(AppScope::class)
    fun provideSessionPreferenceDataStore(
        application: Application,
        aead: Aead,
        @ApplicationScope scope: CoroutineScope,
        @Dispatcher(NavigoDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): DataStore<SessionPreference> =
        DataStoreFactory.create(
            serializer = EncryptedSerializer(SessionPreferenceSerializer, aead),
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            application.dataStoreFile("session_preference_secure.pb")
        }
}
