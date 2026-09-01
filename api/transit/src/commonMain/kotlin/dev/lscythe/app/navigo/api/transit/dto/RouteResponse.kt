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
data class RouteResponse(
    val id: String,
    val agencyId: String,
    val shortName: String,
    val longName: String,
    val description: String,
    val type: Int,
    val url: String,
    val color: String,
    val textColor: String,
    val sortOrder: Int,
    val continuousPickup: String,
    val continuousDropOff: String,
    val networkId: String,
    val priceIdr: Long,
    val startTime: String,
    val endTime: String,
    val headwaySeconds: Int,
    val operationalDays: List<String>,
    val tripIds: List<String>,
    val facilities: List<FacilityResponse>,
)
