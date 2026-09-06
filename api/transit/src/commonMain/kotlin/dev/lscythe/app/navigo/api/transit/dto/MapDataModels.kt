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
package dev.lscythe.app.navigo.api.transit.dto

import kotlinx.serialization.Serializable

@Serializable
data class MapVariantResponse(
    val id: String,
    val headsign: String,
    val polyline: String,
)

@Serializable
data class MapRouteResponse(
    val id: String,
    val name: String,
    val shortName: String,
    val color: String,
    val textColor: String,
    val variants: List<MapVariantResponse>,
)

@Serializable
data class MapStopResponse(
    val id: String,
    val name: String,
    val location: LocationResponse,
)

@Serializable
data class MapDataResponse(
    val routes: List<MapRouteResponse>,
    val stops: List<MapStopResponse>,
    val freshness: FreshnessResponse,
)
