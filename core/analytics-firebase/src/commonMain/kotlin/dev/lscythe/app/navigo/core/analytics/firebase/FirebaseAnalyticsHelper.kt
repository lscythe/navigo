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
package dev.lscythe.app.navigo.core.analytics.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import dev.lscythe.app.navigo.core.analytics.AnalyticsEvent
import dev.lscythe.app.navigo.core.analytics.AnalyticsHelper
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class FirebaseAnalyticsHelper : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        Firebase.analytics.logEvent(
            name = event.type,
            parameters = event.extras.associate { it.key to it.value },
        )
    }
}
