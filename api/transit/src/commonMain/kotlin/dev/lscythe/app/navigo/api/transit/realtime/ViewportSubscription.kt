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

import kotlinx.serialization.Serializable

/** Geographic bounds for a viewport stream subscription. */
@Serializable
data class ViewportBounds(
    val south: Double,
    val west: Double,
    val north: Double,
    val east: Double,
) {
    init {
        require(south.isFinite() && south in -90.0..90.0) { "Invalid south latitude" }
        require(north.isFinite() && north in -90.0..90.0) { "Invalid north latitude" }
        require(west.isFinite() && west in -180.0..180.0) { "Invalid west longitude" }
        require(east.isFinite() && east in -180.0..180.0) { "Invalid east longitude" }
        require(south < north) { "South latitude must be below north latitude" }
        require(west < east) { "West longitude must be below east longitude" }
    }
}

/** A validated replacement subscription sent over the viewport stream. */
@Serializable
data class ViewportSubscription(
    val requestId: String,
    val viewport: ViewportBounds,
    val routeCodes: List<String> = emptyList(),
    val includeSnapshot: Boolean,
) {
    init {
        require(requestId.isNotBlank()) { "requestId must not be blank" }
        require(routeCodes.all(String::isNotBlank)) { "routeCodes must not contain blanks" }
        require(routeCodes.size == routeCodes.distinct().size) { "routeCodes must be unique" }
    }
}
