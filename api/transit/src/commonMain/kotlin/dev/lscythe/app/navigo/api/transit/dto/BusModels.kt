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
data class StopEtaResponse(
    val stopId: String,
    val parentStopId: String,
    val name: String,
    val parentStopName: String,
    val headsign: String,
    val location: LocationResponse,
    val sequence: Int,
    val etaSeconds: Int,
    val timeFromPreviousSeconds: Int,
    val waitSeconds: Int,
)

@Serializable
data class BusResponse(
    val bodyNumber: String,
    val routeCode: String,
    val routeName: String,
    val routeColor: String,
    val routeTextColor: String,
    val tripId: String,
    val tripHeadsign: String,
    val tripShortName: String,
    val serviceType: String,
    val vehicleType: String,
    val location: LocationResponse,
    val snappedLocation: LocationResponse,
    val bearing: Double,
    val speed: Double,
    val direction: String,
    val distanceMeters: Double,
    val currentStops: String,
    val nextStops: String,
    val previousStops: String,
    val estimatedDistanceNextStopMeters: Int,
    val estimatedTimeNextStopSeconds: Int,
    val nextGate: String,
    val nextGateEtaSeconds: Int,
    val nextParentGate: String,
    val nextParentGateEtaSeconds: Int,
    val passengerStatus: String,
    val livery: String,
    val tile: String,
    val stops: List<StopEtaResponse>,
    val freshness: FreshnessResponse,
    val source: String,
)

@Serializable
data class BusesResponse(
    val items: List<BusResponse>,
    val freshness: FreshnessResponse,
)
