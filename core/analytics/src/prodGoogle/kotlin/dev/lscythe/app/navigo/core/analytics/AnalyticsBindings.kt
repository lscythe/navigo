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
package dev.lscythe.app.navigo.core.analytics.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dev.lscythe.app.navigo.core.analytics.AnalyticsHelper
import dev.lscythe.app.navigo.core.analytics.FirebaseAnalyticsHelper
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
@BindingContainer
internal object AnalyticsBindings {

    @Provides
    fun provideAnalyticsHelper(firebaseAnalyticsHelper: FirebaseAnalyticsHelper): AnalyticsHelper =
        firebaseAnalyticsHelper

    @Provides
    @SingleIn(AppScope::class)
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics
}
