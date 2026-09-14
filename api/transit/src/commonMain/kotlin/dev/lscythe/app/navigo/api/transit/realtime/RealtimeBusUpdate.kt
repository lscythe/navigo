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
package dev.lscythe.app.navigo.api.transit.realtime

import dev.lscythe.app.navigo.api.transit.dto.FreshnessResponse
import dev.lscythe.app.navigo.api.transit.dto.LocationResponse
import dev.lscythe.app.navigo.api.transit.dto.StopEtaResponse
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Source that produced a realtime bus update. */
@Serializable
enum class RealtimeBusSource {
    @SerialName("rest") Rest,
    @SerialName("mqtt") Mqtt,
    @SerialName("cache") Cache,
}

/** A partial bus upsert from the viewport stream. Omitted properties are unchanged. */
@Serializable
data class RealtimeBusUpdate(
    val bodyNumber: String,
    val routeCode: String,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val routeName: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val routeColor: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val routeTextColor: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val tripId: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val tripHeadsign: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val tripShortName: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val serviceType: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val vehicleType: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val location: LocationResponse? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val snappedLocation: LocationResponse? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val bearing: Double? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val speed: Double? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val direction: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val distanceMeters: Double? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val currentStops: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val nextStops: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val previousStops: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER)
    val estimatedDistanceNextStopMeters: Int? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val estimatedTimeNextStopSeconds: Int? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val nextGate: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val nextGateEtaSeconds: Int? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val nextParentGate: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val nextParentGateEtaSeconds: Int? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val passengerStatus: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val livery: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val tile: String? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val stops: List<StopEtaResponse>? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val freshness: FreshnessResponse? = null,
    @property:EncodeDefault(EncodeDefault.Mode.NEVER) val source: RealtimeBusSource? = null,
) {
    init {
        require(bodyNumber.isNotBlank()) { "bodyNumber must not be blank" }
        require(routeCode.isNotBlank()) { "routeCode must not be blank" }
        validateLocation(location, "location")
        validateLocation(snappedLocation, "snappedLocation")
        require(bearing == null || bearing.isFinite()) { "bearing must be finite" }
        require(speed == null || speed.isFinite() && speed >= 0.0) { "speed must be non-negative" }
        require(distanceMeters == null || distanceMeters.isFinite() && distanceMeters >= 0.0) {
            "distanceMeters must be non-negative"
        }
        require(estimatedDistanceNextStopMeters == null || estimatedDistanceNextStopMeters >= 0) {
            "estimatedDistanceNextStopMeters must be non-negative"
        }
        require(estimatedTimeNextStopSeconds == null || estimatedTimeNextStopSeconds >= 0) {
            "estimatedTimeNextStopSeconds must be non-negative"
        }
        require(nextGateEtaSeconds == null || nextGateEtaSeconds >= 0) {
            "nextGateEtaSeconds must be non-negative"
        }
        require(nextParentGateEtaSeconds == null || nextParentGateEtaSeconds >= 0) {
            "nextParentGateEtaSeconds must be non-negative"
        }
    }
}

private fun validateLocation(location: LocationResponse?, name: String) {
    if (location == null) return
    require(location.latitude.isFinite() && location.latitude in -90.0..90.0) {
        "$name latitude is invalid"
    }
    require(location.longitude.isFinite() && location.longitude in -180.0..180.0) {
        "$name longitude is invalid"
    }
}
