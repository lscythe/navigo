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
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal class ViewportStreamCodec(private val json: Json) {
    internal fun encode(subscription: ViewportSubscription): String =
        json.encodeToString(SubscribeFrame(subscription = subscription))

    internal fun decode(text: String): ViewportStreamEvent =
        try {
            val objectValue = json.parseToJsonElement(text).jsonObject
            when (objectValue["type"]?.jsonPrimitive?.content) {
                "subscribed" -> json.decodeFromJsonElement<SubscribedFrame>(objectValue).toEvent()
                "status" -> json.decodeFromJsonElement<StatusFrame>(objectValue).toEvent()
                "bus" -> decodeBus(objectValue)
                "bus_removed" -> json.decodeFromJsonElement<BusRemovedFrame>(objectValue).toEvent()
                "error" -> json.decodeFromJsonElement<ErrorFrame>(objectValue).toEvent()
                else -> throw ViewportStreamFailure.Protocol("Unknown viewport stream frame type")
            }
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (failure: ViewportStreamFailure) {
            throw failure
        } catch (throwable: Throwable) {
            throw ViewportStreamFailure.Protocol("Invalid viewport stream payload", throwable)
        }

    private fun decodeBus(objectValue: JsonObject): ViewportStreamEvent {
        val busValue = objectValue["bus"] ?: throw SerializationException("Missing bus payload")
        rejectExplicitNulls(busValue)
        return json.decodeFromJsonElement<BusFrame>(objectValue).toEvent()
    }

    private fun rejectExplicitNulls(element: JsonElement) {
        val objectValue = element as? JsonObject ?: return
        objectValue.forEach { (name, value) ->
            if (name in BUS_PROPERTIES && value is JsonNull) {
                throw SerializationException("Explicit null is not supported")
            }
        }
    }

    private companion object {
        val BUS_PROPERTIES =
            setOf(
                "bodyNumber",
                "routeCode",
                "routeName",
                "routeColor",
                "routeTextColor",
                "tripId",
                "tripHeadsign",
                "tripShortName",
                "serviceType",
                "vehicleType",
                "location",
                "snappedLocation",
                "bearing",
                "speed",
                "direction",
                "distanceMeters",
                "currentStops",
                "nextStops",
                "previousStops",
                "estimatedDistanceNextStopMeters",
                "estimatedTimeNextStopSeconds",
                "nextGate",
                "nextGateEtaSeconds",
                "nextParentGate",
                "nextParentGateEtaSeconds",
                "passengerStatus",
                "livery",
                "tile",
                "stops",
                "freshness",
                "source",
            )
    }
}

@Serializable
private data class SubscribeFrame(
    @property:EncodeDefault(EncodeDefault.Mode.ALWAYS) val type: String = "subscribe",
    val requestId: String,
    val viewport: ViewportBounds,
    val routeCodes: List<String>,
    val includeSnapshot: Boolean,
) {
    constructor(
        subscription: ViewportSubscription
    ) : this(
        requestId = subscription.requestId,
        viewport = subscription.viewport,
        routeCodes = subscription.routeCodes,
        includeSnapshot = subscription.includeSnapshot,
    )
}

@Serializable
private data class SubscribedFrame(
    val requestId: String,
    val subscriptionId: String,
) {
    fun toEvent(): ViewportStreamEvent = ViewportStreamEvent.Subscribed(requestId, subscriptionId)
}

@Serializable
private data class StatusFrame(
    val subscriptionId: String,
    val state: ViewportStreamState,
    val reason: String,
    val topicCount: Int,
) {
    fun toEvent(): ViewportStreamEvent =
        ViewportStreamEvent.Status(subscriptionId, state, reason, topicCount)
}

@Serializable
private data class BusFrame(
    val subscriptionId: String,
    val bus: RealtimeBusUpdate,
) {
    fun toEvent(): ViewportStreamEvent = ViewportStreamEvent.Bus(subscriptionId, bus)
}

@Serializable
private data class BusRemovedFrame(
    val subscriptionId: String,
    val bodyNumber: String,
) {
    fun toEvent(): ViewportStreamEvent = ViewportStreamEvent.BusRemoved(subscriptionId, bodyNumber)
}

@Serializable
private data class ErrorFrame(
    val requestId: String,
    val problem: ProblemDetail,
) {
    fun toEvent(): ViewportStreamEvent = ViewportStreamEvent.Error(requestId, problem)
}
