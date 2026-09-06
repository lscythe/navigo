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

import dev.lscythe.app.navigo.api.transit.constant.TransitEndpoint
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
import dev.lscythe.app.navigo.core.network.AuthenticatedClient
import dev.lscythe.app.navigo.core.network.safeRequest
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.encodeURLPathPart
import io.ktor.http.encodedPath

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class TransitApiImpl(@AuthenticatedClient private val httpClient: HttpClient) :
    TransitApi {
    override suspend fun getRoute(routeId: String): ApiResponse<RouteDetailResponse> = safeRequest {
        httpClient.get {
            url { encodedPath = "/${TransitEndpoint.ROUTES}/${routeId.encodeURLPathPart()}" }
        }
    }

    override suspend fun getRoutes(ids: List<String>): ApiResponse<RoutesResponse> = safeRequest {
        httpClient.get(TransitEndpoint.ROUTES) {
            url { ids.forEach { parameters.append("id", it) } }
        }
    }

    override suspend fun getStops(
        latitude: Double,
        longitude: Double,
        radiusMeters: Double,
        limit: Int?,
    ): ApiResponse<StopsResponse> {
        require(latitude.isFinite() && latitude in -90.0..90.0) { "Invalid latitude" }
        require(longitude.isFinite() && longitude in -180.0..180.0) { "Invalid longitude" }
        require(radiusMeters.isFinite() && radiusMeters > 0.0) { "Invalid radiusMeters" }
        require(limit == null || limit in 1..100) { "Invalid limit" }

        return safeRequest {
            httpClient.get(TransitEndpoint.STOPS) {
                url {
                    parameters.append("near", "$latitude,$longitude")
                    parameters.append("radiusMeters", radiusMeters.toString())
                    limit?.let { parameters.append("limit", it.toString()) }
                }
            }
        }
    }

    override suspend fun getStop(stopId: String): ApiResponse<StopDetailResponse> = safeRequest {
        httpClient.get {
            url { encodedPath = "/${TransitEndpoint.STOPS}/${stopId.encodeURLPathPart()}" }
        }
    }

    override suspend fun getBuses(
        south: Double,
        west: Double,
        north: Double,
        east: Double,
        routeCodes: List<String>,
    ): ApiResponse<BusesResponse> {
        require(
            south.isFinite() &&
                south in -90.0..90.0 &&
                north.isFinite() &&
                north in -90.0..90.0 &&
                south <= north
        ) {
            "Invalid latitude bounds"
        }
        require(
            west.isFinite() &&
                west in -180.0..180.0 &&
                east.isFinite() &&
                east in -180.0..180.0 &&
                west <= east
        ) {
            "Invalid longitude bounds"
        }
        return safeRequest {
            httpClient.get(TransitEndpoint.BUSES) {
                url {
                    parameters.append("bounds", "$south,$west,$north,$east")
                    routeCodes.forEach { parameters.append("routeCode", it) }
                }
            }
        }
    }

    override suspend fun createTripPlan(
        request: TripPlanRequest
    ): ApiResponse<TripPlanResultResponse> {
        validateLocation(request.origin)
        validateLocation(request.destination)
        return safeRequest { httpClient.post(TransitEndpoint.TRIP_PLANS) { setBody(request) } }
    }

    override suspend fun getMapData(): ApiResponse<MapDataResponse> = safeRequest {
        httpClient.get(TransitEndpoint.MAP_DATA)
    }

    override suspend fun getSearchResults(
        query: String,
        near: LocationResponse?,
        category: SearchCategory?,
        limit: Int?,
    ): ApiResponse<SearchResultsResponse> {
        val trimmedQuery = query.trim()
        require(trimmedQuery.length >= 2) { "Invalid query" }
        near?.let(::validateLocation)
        require(limit == null || limit in 1..100) { "Invalid limit" }
        return safeRequest {
            httpClient.get(TransitEndpoint.SEARCH_RESULTS) {
                url {
                    parameters.append("q", trimmedQuery)
                    near?.let { parameters.append("near", "${it.latitude},${it.longitude}") }
                    category?.let { parameters.append("category", it.name.lowercase()) }
                    limit?.let { parameters.append("limit", it.toString()) }
                }
            }
        }
    }

    private fun validateLocation(location: LocationResponse) {
        require(location.latitude.isFinite() && location.latitude in -90.0..90.0) {
            "Invalid latitude"
        }
        require(location.longitude.isFinite() && location.longitude in -180.0..180.0) {
            "Invalid longitude"
        }
    }
}
