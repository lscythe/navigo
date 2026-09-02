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

import androidx.compose.runtime.Immutable

@Immutable
internal data class NearbyStop(
    val route: String,
    val name: String,
    val distance: String,
    val status: String,
    val detail: String,
    val arrival: String,
    val accent: StopAccent,
)

internal enum class StopAccent {
    Dark,
    Green,
    Lime,
    Muted,
}

@Immutable
internal data class UsualRun(
    val label: String,
    val route: String,
    val detail: String,
    val duration: String,
    val emphasized: Boolean,
)

internal val nearbyStops =
    listOf(
        NearbyStop("14", "Stop A", "120 m", "Packed", "8 riders said so", "2 min", StopAccent.Dark),
        NearbyStop(
            "27B",
            "Stop B",
            "240 m",
            "Seats",
            "running 3 min late",
            "6 min",
            StopAccent.Green,
        ),
        NearbyStop(
            "9",
            "Pasar Baru",
            "310 m",
            "No tracker",
            "riders only · 3 min ago",
            "7 min",
            StopAccent.Green,
        ),
        NearbyStop(
            "14",
            "Jalan Melati",
            "480 m",
            "Seats",
            "4 riders aboard",
            "11 min",
            StopAccent.Dark,
        ),
        NearbyStop(
            "31",
            "Terminal Cikini",
            "620 m",
            "Quiet line",
            "last seen 12 min ago",
            "—",
            StopAccent.Green,
        ),
        NearbyStop(
            "27B",
            "Bundaran Sari",
            "850 m",
            "Packed",
            "6 riders said so",
            "14 min",
            StopAccent.Green,
        ),
        NearbyStop(
            "9",
            "Kebon Jeruk",
            "1.1 km",
            "Seats",
            "2 riders aboard",
            "18 min",
            StopAccent.Green,
        ),
        NearbyStop(
            "14",
            "Cikini Raya",
            "1.3 km",
            "Packed",
            "5 riders said so",
            "21 min",
            StopAccent.Dark,
        ),
        NearbyStop(
            "31",
            "Stasiun Gondangdia",
            "1.6 km",
            "No tracker",
            "last seen 9 min ago",
            "24 min",
            StopAccent.Green,
        ),
    )

internal val usualRuns =
    listOf(
        UsualRun("Home", "14 → 27B", "", "32 min door to door", true),
        UsualRun("Work", "6 direct", "", "19 min door to door", false),
    )
