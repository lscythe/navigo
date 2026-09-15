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

import dev.lscythe.app.navigo.core.network.ProblemDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** State reported for a viewport subscription. */
@Serializable
enum class ViewportStreamState {
    @SerialName("ready") Ready,
    @SerialName("degraded") Degraded,
    @SerialName("shutdown") Shutdown,
}

/** A decoded application event from the viewport stream. */
sealed interface ViewportStreamEvent {
    data class Subscribed(val requestId: String, val subscriptionId: String) : ViewportStreamEvent {
        init {
            require(requestId.isNotBlank()) { "requestId must not be blank" }
            require(subscriptionId.isNotBlank()) { "subscriptionId must not be blank" }
        }
    }

    data class Status(
        val subscriptionId: String,
        val state: ViewportStreamState,
        val reason: String,
        val topicCount: Int,
    ) : ViewportStreamEvent {
        init {
            require(subscriptionId.isNotBlank()) { "subscriptionId must not be blank" }
            require(topicCount >= 0) { "topicCount must be non-negative" }
        }
    }

    data class Bus(val subscriptionId: String, val bus: RealtimeBusUpdate) : ViewportStreamEvent {
        init {
            require(subscriptionId.isNotBlank()) { "subscriptionId must not be blank" }
        }
    }

    data class BusRemoved(val subscriptionId: String, val bodyNumber: String) :
        ViewportStreamEvent {
        init {
            require(subscriptionId.isNotBlank()) { "subscriptionId must not be blank" }
            require(bodyNumber.isNotBlank()) { "bodyNumber must not be blank" }
        }
    }

    data class Error(val requestId: String, val problem: ProblemDetail) : ViewportStreamEvent {
        init {
            require(requestId.isNotBlank()) { "requestId must not be blank" }
        }
    }
}
