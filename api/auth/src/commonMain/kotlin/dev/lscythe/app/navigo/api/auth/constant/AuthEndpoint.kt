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
package dev.lscythe.app.navigo.api.auth.constant

object AuthEndpoint {
    const val INTEGRITY_CHALLENGES = "v1/integrity-challenges"
    const val SESSIONS = "v1/sessions"
    const val SESSION_REFRESHES = "v1/session-refreshes"
    const val CURRENT_SESSION = "v1/sessions/current"
}
