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
package dev.lscythe.app.navigo.core.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSBundle
import platform.Foundation.NSUserDefaults

actual object LocalAppLocale {
    private const val LanguagesKey = "AppleLanguages"
    private val systemLocale =
        NSBundle.mainBundle.preferredLocalizations.firstOrNull() as? String ?: "en"
    private val localLocale = staticCompositionLocalOf { systemLocale }

    actual val current: String
        @Composable get() = localLocale.current

    @Composable
    actual infix fun provides(languageTag: String?): ProvidedValue<*> {
        val locale = languageTag ?: systemLocale
        if (languageTag == null) {
            NSUserDefaults.standardUserDefaults.removeObjectForKey(LanguagesKey)
        } else {
            NSUserDefaults.standardUserDefaults.setObject(listOf(locale), LanguagesKey)
        }
        return localLocale.provides(locale)
    }
}
