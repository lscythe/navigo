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

import dev.lscythe.app.navigo.core.persistence.SessionPreference
import dev.lscythe.app.navigo.core.persistence.di.SessionKSafe
import dev.zacsweers.metro.Inject
import eu.anifantakis.lib.ksafe.KSafe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val SESSION_PREFERENCE_KEY = "session_preference"

@Inject
class SessionPreferenceDataSource(@SessionKSafe private val ksafe: KSafe) {
    private val mutationMutex = Mutex()

    val data: Flow<SessionPreference> = ksafe.getFlow(SESSION_PREFERENCE_KEY, SessionPreference())

    suspend fun setSession(session: SessionPreference) {
        mutationMutex.withLock {
            ksafe.put(SESSION_PREFERENCE_KEY, session)
        }
    }

    suspend fun clear() {
        mutationMutex.withLock {
            ksafe.put(SESSION_PREFERENCE_KEY, SessionPreference())
        }
    }
}
