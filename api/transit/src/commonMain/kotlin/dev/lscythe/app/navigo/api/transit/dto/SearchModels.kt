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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SearchCategory {
    @SerialName("all") ALL,
    @SerialName("stops") STOPS,
    @SerialName("routes") ROUTES,
    @SerialName("destinations") DESTINATIONS,
}

@Serializable
data class DestinationResponse(
    val providerId: String,
    val name: String,
    val address: String,
    val location: LocationResponse,
    val distanceMeters: Double,
    val category: String,
    val osmType: String,
    val osmId: Int,
    val attribution: String,
)

@Serializable
data class SearchSourceResponse(
    val category: String,
    val state: String,
)

@Serializable
data class SearchResultsResponse(
    val stops: List<StopResponse>,
    val routes: List<RouteResponse>,
    val destinations: List<DestinationResponse>,
    val sources: List<SearchSourceResponse>,
)
