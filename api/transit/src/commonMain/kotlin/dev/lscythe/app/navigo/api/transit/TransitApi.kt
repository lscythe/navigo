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
package dev.lscythe.app.navigo.api.transit

import dev.lscythe.app.navigo.api.transit.dto.BusesResponse
import dev.lscythe.app.navigo.api.transit.dto.LocationResponse
import dev.lscythe.app.navigo.api.transit.dto.MapDataResponse
import dev.lscythe.app.navigo.api.transit.dto.RouteDetailResponse
import dev.lscythe.app.navigo.api.transit.dto.RoutesResponse
import dev.lscythe.app.navigo.api.transit.dto.SearchCategory
import dev.lscythe.app.navigo.api.transit.dto.SearchResultsResponse
import dev.lscythe.app.navigo.api.transit.dto.StopDetailResponse
import dev.lscythe.app.navigo.api.transit.dto.StopsResponse
import dev.lscythe.app.navigo.api.transit.dto.TripPlanRequest
import dev.lscythe.app.navigo.api.transit.dto.TripPlanResultResponse
import dev.lscythe.app.navigo.core.network.ApiResponse

interface TransitApi {
    suspend fun getRoute(routeId: String): ApiResponse<RouteDetailResponse>

    suspend fun getRoutes(ids: List<String> = emptyList()): ApiResponse<RoutesResponse>

    suspend fun getStops(
        latitude: Double,
        longitude: Double,
        radiusMeters: Double,
        limit: Int? = null,
    ): ApiResponse<StopsResponse>

    suspend fun getStop(stopId: String): ApiResponse<StopDetailResponse>

    suspend fun getBuses(
        south: Double,
        west: Double,
        north: Double,
        east: Double,
        routeCodes: List<String> = emptyList(),
    ): ApiResponse<BusesResponse>

    suspend fun createTripPlan(request: TripPlanRequest): ApiResponse<TripPlanResultResponse>

    suspend fun getMapData(): ApiResponse<MapDataResponse>

    suspend fun getSearchResults(
        query: String,
        near: LocationResponse? = null,
        category: SearchCategory? = null,
        limit: Int? = null,
    ): ApiResponse<SearchResultsResponse>
}
