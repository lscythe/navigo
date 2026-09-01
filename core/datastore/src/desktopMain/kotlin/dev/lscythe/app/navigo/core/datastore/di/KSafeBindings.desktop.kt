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

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import eu.anifantakis.lib.ksafe.KSafeKeyRotationPolicy
import kotlin.time.Duration.Companion.days

private const val APP_NAMESPACE = "dev.lscythe.app.navigo"

@ContributesTo(AppScope::class)
@BindingContainer
object KSafeBindings {
    @Provides
    @PreferencesKSafe
    @SingleIn(AppScope::class)
    fun providePreferencesKSafe(): KSafe =
        KSafe(
            fileName = "preferences",
            config = KSafeConfig(appNamespace = APP_NAMESPACE),
        )

    @Provides
    @SessionKSafe
    @SingleIn(AppScope::class)
    fun provideSessionKSafe(): KSafe =
        KSafe(
            fileName = "session",
            config =
                KSafeConfig(
                    appNamespace = APP_NAMESPACE,
                    keyRotationPolicy = KSafeKeyRotationPolicy.MaxAge(90.days),
                ),
        )
}
