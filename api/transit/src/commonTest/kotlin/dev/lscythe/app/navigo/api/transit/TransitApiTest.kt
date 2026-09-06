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

import dev.lscythe.app.navigo.api.transit.dto.FacilityResponse
import dev.lscythe.app.navigo.api.transit.dto.FreshnessResponse
import dev.lscythe.app.navigo.api.transit.dto.LocationResponse
import dev.lscythe.app.navigo.api.transit.dto.MapDataResponse
import dev.lscythe.app.navigo.api.transit.dto.MapRouteResponse
import dev.lscythe.app.navigo.api.transit.dto.MapStopResponse
import dev.lscythe.app.navigo.api.transit.dto.MapVariantResponse
import dev.lscythe.app.navigo.api.transit.dto.RouteDetailResponse
import dev.lscythe.app.navigo.api.transit.dto.RouteResponse
import dev.lscythe.app.navigo.api.transit.dto.RoutesResponse
import dev.lscythe.app.navigo.api.transit.dto.SearchCategory
import dev.lscythe.app.navigo.api.transit.dto.StopResponse
import dev.lscythe.app.navigo.api.transit.dto.StopsResponse
import dev.lscythe.app.navigo.api.transit.dto.TripPlanRequest
import dev.lscythe.app.navigo.core.network.ApiResponse
import dev.lscythe.app.navigo.core.testing.readResource
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.Instant

class TransitApiTest :
    FunSpec({
        test("gets an authenticated route by encoded id") {
            val engine = MockEngine { request ->
                request.method shouldBe HttpMethod.Get
                request.url.encodedPath shouldBe "/v1/routes/route%2Fid"
                respond(
                    content = readResource("transit/route-response.json"),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
            val api: TransitApi = TransitApiImpl(client)

            val result = api.getRoute("route/id")

            result shouldBe
                ApiResponse.Success(
                    RouteDetailResponse(
                        route =
                            RouteResponse(
                                id = "route-id",
                                agencyId = "agency-id",
                                shortName = "1A",
                                longName = "Route One",
                                description = "Route description",
                                type = 3,
                                url = "https://example.com/routes/route-id",
                                color = "0B3D33",
                                textColor = "FFFFFF",
                                sortOrder = 1,
                                continuousPickup = "",
                                continuousDropOff = "",
                                networkId = "network-id",
                                priceIdr = 3500,
                                startTime = "05:00:00",
                                endTime = "24:30:00",
                                headwaySeconds = 600,
                                operationalDays = listOf("monday", "tuesday"),
                                tripIds = listOf("trip-1", "trip-2"),
                                facilities =
                                    listOf(
                                        FacilityResponse(
                                            name = "wheelchair",
                                            title = "Wheelchair accessible",
                                            iconUrl = "https://example.com/icons/wheelchair.svg",
                                            darkIconUrl =
                                                "https://example.com/icons/wheelchair-dark.svg",
                                        )
                                    ),
                            ),
                        freshness =
                            FreshnessResponse(
                                observedAt = Instant.parse("2019-08-24T14:15:22Z"),
                                receivedAt = Instant.parse("2019-08-24T14:15:23Z"),
                                stale = true,
                            ),
                    )
                )
        }

        test("gets routes filtered by repeated ids") {
            val engine = MockEngine { request ->
                request.method shouldBe HttpMethod.Get
                request.url.encodedPath shouldBe "/v1/routes"
                request.url.parameters.getAll("id") shouldBe listOf("1", "6A")
                respond(
                    content = readResource("transit/routes-response.json"),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
            val api: TransitApi = TransitApiImpl(client)

            val result = api.getRoutes(listOf("1", "6A"))

            result shouldBe
                ApiResponse.Success(
                    RoutesResponse(
                        items =
                            listOf(
                                RouteResponse(
                                    id = "1",
                                    agencyId = "TJ",
                                    shortName = "1",
                                    longName = "Blok-M",
                                    description = "",
                                    type = 3,
                                    url = "https://navigo.app/routes/1",
                                    color = "123456",
                                    textColor = "ffffff",
                                    sortOrder = 1,
                                    continuousPickup = "",
                                    continuousDropOff = "",
                                    networkId = "TJ",
                                    priceIdr = 3500,
                                    startTime = "05:00",
                                    endTime = "22:00",
                                    headwaySeconds = 600,
                                    operationalDays = listOf("monday"),
                                    tripIds = listOf("trip-1"),
                                    facilities = emptyList(),
                                )
                            ),
                        freshness =
                            FreshnessResponse(
                                observedAt = Instant.parse("2026-08-28T05:00:00Z"),
                                receivedAt = Instant.parse("2026-08-28T05:00:01Z"),
                                stale = false,
                            ),
                    )
                )
        }

        test("gets all routes without an id query") {
            val engine = MockEngine { request ->
                request.url.parameters.contains("id") shouldBe false
                respond(
                    content = readResource("transit/routes-response.json"),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client = HttpClient(engine) { install(ContentNegotiation) { json() } }

            TransitApiImpl(client).getRoutes()
        }

        test("gets nearby stops with an optional limit") {
            val engine = MockEngine { request ->
                request.method shouldBe HttpMethod.Get
                request.url.encodedPath shouldBe "/v1/stops"
                request.url.parameters["near"] shouldBe "-6.1754,106.8272"
                request.url.parameters["radiusMeters"] shouldBe "500.0"
                request.url.parameters["limit"] shouldBe "20"
                respond(
                    content = readResource("transit/stops-response.json"),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client = HttpClient(engine) { install(ContentNegotiation) { json() } }
            val api: TransitApi = TransitApiImpl(client)

            val result = api.getStops(-6.1754, 106.8272, radiusMeters = 500.0, limit = 20)

            result shouldBe
                ApiResponse.Success(
                    StopsResponse(
                        items =
                            listOf(
                                StopResponse(
                                    id = "stop-id",
                                    parentStation = "station-id",
                                    name = "Central Stop",
                                    location = LocationResponse(-6.1754, 106.8272),
                                    routeIds = listOf("1", "6A"),
                                    distanceMeters = 125.5,
                                    platformCode = "A",
                                    description = "Main platform",
                                    zoneId = "zone-1",
                                    url = "https://example.com/stops/stop-id",
                                    locationType = 0,
                                    timezone = "Asia/Jakarta",
                                    wheelchairBoarding = 1,
                                    facilityNames = listOf("wheelchair"),
                                    stopType = "bus_stop",
                                    facilities =
                                        listOf(
                                            FacilityResponse(
                                                name = "wheelchair",
                                                title = "Wheelchair accessible",
                                                iconUrl =
                                                    "https://example.com/icons/wheelchair.svg",
                                                darkIconUrl =
                                                    "https://example.com/icons/wheelchair-dark.svg",
                                            )
                                        ),
                                )
                            ),
                        freshness =
                            FreshnessResponse(
                                observedAt = Instant.parse("2019-08-24T14:15:22Z"),
                                receivedAt = Instant.parse("2019-08-24T14:15:22Z"),
                                stale = true,
                            ),
                    )
                )
        }

        test("omits the stops limit when absent") {
            val engine = MockEngine { request ->
                request.url.parameters.contains("limit") shouldBe false
                respond(
                    content = readResource("transit/stops-response.json"),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val client = HttpClient(engine) { install(ContentNegotiation) { json() } }

            TransitApiImpl(client).getStops(0.0, 0.0, radiusMeters = 1.0)
        }

        test("rejects invalid stops query values") {
            val api = TransitApiImpl(HttpClient(MockEngine { error("request must not execute") }))

            shouldThrow<IllegalArgumentException> {
                api.getStops(Double.NaN, 0.0, 1.0)
            }
            shouldThrow<IllegalArgumentException> {
                api.getStops(91.0, 0.0, 1.0)
            }
            shouldThrow<IllegalArgumentException> {
                api.getStops(0.0, 181.0, 1.0)
            }
            shouldThrow<IllegalArgumentException> {
                api.getStops(0.0, 0.0, 0.0)
            }
            shouldThrow<IllegalArgumentException> {
                api.getStops(0.0, 0.0, 1.0, limit = 0)
            }
            shouldThrow<IllegalArgumentException> {
                api.getStops(0.0, 0.0, 1.0, limit = 101)
            }
        }

        test("maps remaining OpenAPI endpoint requests") {
            val requests = mutableListOf<Pair<HttpMethod, String>>()
            val engine = MockEngine { request ->
                requests += request.method to request.url.toString()
                respond(
                    content =
                        if (request.url.encodedPath == "/v1/map-data") {
                            readResource("transit/map-data-response.json")
                        } else {
                            "{}"
                        },
                    status =
                        if (request.method == HttpMethod.Post) HttpStatusCode.Created
                        else HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
            val api =
                TransitApiImpl(
                    HttpClient(engine) {
                        defaultRequest { contentType(ContentType.Application.Json) }
                        install(ContentNegotiation) { json() }
                    }
                )

            api.getStop("stop/id")
            api.getBuses(-6.3, 106.7, -6.1, 106.9, listOf("1", "6A"))
            api.createTripPlan(
                TripPlanRequest(
                    origin = LocationResponse(-6.2, 106.8),
                    destination = LocationResponse(-6.17, 106.83),
                    alternatives = true,
                )
            )
            api.getSearchResults("Monas", LocationResponse(-6.175, 106.827), SearchCategory.ALL, 20)
            val mapDataResult = api.getMapData()

            requests[0] shouldBe (HttpMethod.Get to "http://localhost/v1/stops/stop%2Fid")
            requests[1].second shouldBe
                "http://localhost/v1/buses?bounds=-6.3%2C106.7%2C-6.1%2C106.9&routeCode=1&routeCode=6A"
            requests[2].first shouldBe HttpMethod.Post
            requests[2].second shouldBe "http://localhost/v1/trip-plans"
            requests[3].second shouldBe
                "http://localhost/v1/search-results?q=Monas&near=-6.175%2C106.827&category=all&limit=20"
            requests[4].second shouldBe "http://localhost/v1/map-data"
            mapDataResult shouldBe
                ApiResponse.Success(
                    MapDataResponse(
                        routes =
                            listOf(
                                MapRouteResponse(
                                    id = "route-1",
                                    name = "Blok M - Kota",
                                    shortName = "1",
                                    color = "123456",
                                    textColor = "ffffff",
                                    variants =
                                        listOf(
                                            MapVariantResponse(
                                                id = "variant-1",
                                                headsign = "Kota",
                                                polyline = "encoded-polyline",
                                            )
                                        ),
                                )
                            ),
                        stops =
                            listOf(
                                MapStopResponse(
                                    id = "stop-1",
                                    name = "Monas",
                                    location = LocationResponse(-6.175, 106.827),
                                )
                            ),
                        freshness =
                            FreshnessResponse(
                                observedAt = Instant.parse("2026-08-28T05:00:00Z"),
                                receivedAt = Instant.parse("2026-08-28T05:00:01Z"),
                                stale = false,
                            ),
                    )
                )
        }
    })
