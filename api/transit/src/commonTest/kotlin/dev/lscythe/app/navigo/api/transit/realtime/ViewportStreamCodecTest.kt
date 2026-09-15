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

import dev.lscythe.app.navigo.api.transit.dto.LocationResponse
import dev.lscythe.app.navigo.core.network.ProblemDetail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class ViewportStreamCodecTest :
    FunSpec({
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
        val codec = ViewportStreamCodec(json)

        test("encodes a subscribe frame with the exact semantic shape") {
            val encoded =
                codec.encode(
                    ViewportSubscription(
                        requestId = "request-1",
                        viewport = ViewportBounds(-6.3, 106.7, -6.1, 106.9),
                        routeCodes = listOf("1", "6A"),
                        includeSnapshot = true,
                    )
                )

            json.parseToJsonElement(encoded) shouldBe
                json.parseToJsonElement(
                    """{"type":"subscribe","requestId":"request-1","viewport":{"south":-6.3,"west":106.7,"north":-6.1,"east":106.9},"routeCodes":["1","6A"],"includeSnapshot":true}"""
                )
        }

        test("decodes subscription lifecycle frames") {
            codec.decode(
                """{"type":"subscribed","requestId":"request-1","subscriptionId":"subscription-1"}"""
            ) shouldBe ViewportStreamEvent.Subscribed("request-1", "subscription-1")
            codec.decode(
                """{"type":"status","subscriptionId":"subscription-1","state":"ready","reason":"","topicCount":6}"""
            ) shouldBe
                ViewportStreamEvent.Status(
                    "subscription-1",
                    ViewportStreamState.Ready,
                    "",
                    6,
                )
            codec.decode(
                """{"type":"status","subscriptionId":"subscription-1","state":"degraded","reason":"upstream","topicCount":2}"""
            ) shouldBe
                ViewportStreamEvent.Status(
                    "subscription-1",
                    ViewportStreamState.Degraded,
                    "upstream",
                    2,
                )
            codec.decode(
                """{"type":"status","subscriptionId":"subscription-1","state":"shutdown","reason":"maintenance","topicCount":0}"""
            ) shouldBe
                ViewportStreamEvent.Status(
                    "subscription-1",
                    ViewportStreamState.Shutdown,
                    "maintenance",
                    0,
                )
        }

        test("decodes minimal and partial bus upserts without REST defaults") {
            codec.decode(
                """{"type":"bus","subscriptionId":"subscription-1","bus":{"bodyNumber":"B-01","routeCode":"1"}}"""
            ) shouldBe
                ViewportStreamEvent.Bus(
                    "subscription-1",
                    RealtimeBusUpdate(bodyNumber = "B-01", routeCode = "1"),
                )

            codec.decode(
                """{"type":"bus","subscriptionId":"subscription-1","bus":{"bodyNumber":"B-01","routeCode":"1","location":{"latitude":-6.2,"longitude":106.8},"bearing":90.0,"speed":12.5,"distanceMeters":100.0,"estimatedDistanceNextStopMeters":250,"estimatedTimeNextStopSeconds":30,"nextGateEtaSeconds":45,"nextParentGateEtaSeconds":60,"source":"mqtt","unknownCompatibleField":true}}"""
            ) shouldBe
                ViewportStreamEvent.Bus(
                    "subscription-1",
                    RealtimeBusUpdate(
                        bodyNumber = "B-01",
                        routeCode = "1",
                        location = LocationResponse(-6.2, 106.8),
                        bearing = 90.0,
                        speed = 12.5,
                        distanceMeters = 100.0,
                        estimatedDistanceNextStopMeters = 250,
                        estimatedTimeNextStopSeconds = 30,
                        nextGateEtaSeconds = 45,
                        nextParentGateEtaSeconds = 60,
                        source = RealtimeBusSource.Mqtt,
                    ),
                )
        }

        test("ignores compatible unknown null bus properties") {
            codec.decode(
                """{"type":"bus","subscriptionId":"subscription-1","bus":{"bodyNumber":"B-01","routeCode":"1","futureNullableField":null}}"""
            ) shouldBe
                ViewportStreamEvent.Bus(
                    "subscription-1",
                    RealtimeBusUpdate(bodyNumber = "B-01", routeCode = "1"),
                )
        }
        test("decodes bus removal and request scoped errors") {
            codec.decode(
                """{"type":"bus_removed","subscriptionId":"subscription-1","bodyNumber":"B-01"}"""
            ) shouldBe ViewportStreamEvent.BusRemoved("subscription-1", "B-01")
            codec.decode(
                """{"type":"error","requestId":"request-1","problem":{"type":"https://navigo.app/problems/invalid-viewport","title":"Invalid viewport","status":400,"detail":"Viewport is invalid","instance":"request-1"}}"""
            ) shouldBe
                ViewportStreamEvent.Error(
                    "request-1",
                    ProblemDetail(
                        type = "https://navigo.app/problems/invalid-viewport",
                        title = "Invalid viewport",
                        status = 400,
                        detail = "Viewport is invalid",
                        instance = "request-1",
                    ),
                )
        }

        test("rejects unknown malformed and invalid top level frames") {
            listOf(
                    "not-json",
                    "[]",
                    "{}",
                    """{"type":"future"}""",
                    """{"type":"subscribed","requestId":" ","subscriptionId":"subscription-1"}""",
                    """{"type":"status","subscriptionId":"subscription-1","state":"future","reason":"","topicCount":0}""",
                    """{"type":"bus_removed","subscriptionId":"subscription-1","bodyNumber":" "}""",
                )
                .forEach { frame ->
                    shouldThrow<ViewportStreamFailure.Protocol> { codec.decode(frame) }
                }
        }

        test("rejects null missing or invalid bus properties") {
            listOf(
                    """{"type":"bus","subscriptionId":"subscription-1","bus":{"routeCode":"1"}}""",
                    """{"type":"bus","subscriptionId":"subscription-1","bus":{"bodyNumber":"B-01"}}""",
                    """{"type":"bus","subscriptionId":"subscription-1","bus":{"bodyNumber":"B-01","routeCode":"1","speed":null}}""",
                    """{"type":"bus","subscriptionId":"subscription-1","bus":{"bodyNumber":"B-01","routeCode":"1","source":"future"}}""",
                    """{"type":"bus","subscriptionId":"subscription-1","bus":{"bodyNumber":"B-01","routeCode":"1","location":{"latitude":91.0,"longitude":106.8}}}""",
                    """{"type":"bus","subscriptionId":"subscription-1","bus":{"bodyNumber":"B-01","routeCode":"1","speed":-1.0}}""",
                    """{"type":"bus","subscriptionId":"subscription-1","bus":{"bodyNumber":"B-01","routeCode":"1","distanceMeters":-1.0}}""",
                    """{"type":"bus","subscriptionId":"subscription-1","bus":{"bodyNumber":"B-01","routeCode":"1","estimatedTimeNextStopSeconds":-1}}""",
                )
                .forEach { frame ->
                    shouldThrow<ViewportStreamFailure.Protocol> { codec.decode(frame) }
                }
        }

        test("rejects non finite bus numbers") {
            shouldThrow<IllegalArgumentException> {
                RealtimeBusUpdate("B-01", "1", bearing = Double.NaN)
            }
        }
    })
