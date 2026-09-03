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
package dev.lscythe.app.navigo.feature.home.impl.content

internal const val HomeMapLongitude = 106.8999865
internal const val HomeMapLatitude = -6.2054059
internal const val HomeMapZoom = 12.0
internal const val HomeMaptoolkitUrl = "https://www.maptoolkit.com"
internal const val HomeMapAttributionHtml =
    """<a href="https://maplibre.org/">MapLibre</a> | <a href="https://www.maptoolkit.com/copyright/">© Maptoolkit</a> <a href="https://www.openstreetmap.org/copyright">© OpenStreetMap</a>"""

internal fun homeMapStyleUri(isDark: Boolean): String =
    "https://transit-basemap.pages.dev/style-${if (isDark) "dark" else "light"}.json"
