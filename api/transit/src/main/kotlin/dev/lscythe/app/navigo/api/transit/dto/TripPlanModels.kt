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
data class TripPlanRequest(
    val origin: LocationResponse,
    val destination: LocationResponse,
    val alternatives: Boolean? = null,
)

@Serializable data class DistanceResponse(val text: String, val meters: Int)

@Serializable data class DurationResponse(val text: String, val seconds: Int)

@Serializable data class PolylineResponse(val points: String)

@Serializable data class NamedLocationResponse(val name: String, val location: LocationResponse)

@Serializable data class AgencyResponse(val name: String, val url: String, val phone: String)

@Serializable
data class TransitLineResponse(
    val name: String,
    val shortName: String,
    val color: String,
    val textColor: String,
    val agencies: List<AgencyResponse>,
    val routeDescription: String,
    val tripIds: List<String>,
    val url: String,
    val iconUrl: String,
)

@Serializable
data class TransitDetailsResponse(
    val departureStop: NamedLocationResponse,
    val arrivalStop: NamedLocationResponse,
    val departureTime: String,
    val arrivalTime: String,
    val headsign: String,
    val headway: DurationResponse,
    val stopCount: Int,
    val tripShortName: String,
    val line: TransitLineResponse,
    val stops: List<StopResponse>,
    val facilities: List<FacilityResponse>,
)

@Serializable
data class TripStepResponse(
    val htmlInstructions: String,
    val travelMode: String,
    val distance: DistanceResponse,
    val duration: DurationResponse,
    val startLocation: LocationResponse,
    val endLocation: LocationResponse,
    val polyline: PolylineResponse,
    val transitDetails: TransitDetailsResponse? = null,
    val steps: List<TripStepResponse>,
)

@Serializable
data class TripLegResponse(
    val steps: List<TripStepResponse>,
    val distance: DistanceResponse,
    val duration: DurationResponse,
    val startLocation: LocationResponse,
    val endLocation: LocationResponse,
)

@Serializable
data class BoundsResponse(val southwest: LocationResponse, val northeast: LocationResponse)

@Serializable data class FareResponse(val currency: String, val value: Int, val text: String)

@Serializable
data class TripPlanResponse(
    val summary: String,
    val legs: List<TripLegResponse>,
    val overviewPolyline: PolylineResponse,
    val bounds: BoundsResponse,
    val attribution: String,
    val warnings: List<String>,
    val waypointOrder: List<Int>,
    val fare: FareResponse,
    val facilities: List<FacilityResponse>,
)

@Serializable
data class TripPlanResultResponse(
    val origin: LocationResponse,
    val destination: LocationResponse,
    val plans: List<TripPlanResponse>,
    val freshness: FreshnessResponse,
)
