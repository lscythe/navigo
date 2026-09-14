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

import dev.lscythe.app.navigo.api.transit.constant.TransitEndpoint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ViewportSubscriptionTest :
    FunSpec({
        val validBounds = ViewportBounds(-6.3, 106.7, -6.1, 106.9)

        test("accepts a valid viewport subscription") {
            ViewportSubscription("request-1", validBounds, listOf("1", "6A"), true) shouldBe
                ViewportSubscription("request-1", validBounds, listOf("1", "6A"), true)
        }

        listOf<() -> ViewportBounds>(
                { ViewportBounds(-91.0, 106.7, -6.1, 106.9) },
                { ViewportBounds(-6.3, -181.0, -6.1, 106.9) },
                { ViewportBounds(-6.1, 106.7, -6.1, 106.9) },
                { ViewportBounds(-6.3, 106.9, -6.1, 106.9) },
                { ViewportBounds(Double.NaN, 106.7, -6.1, 106.9) },
            )
            .forEachIndexed { index, createInvalid ->
                test("rejects invalid viewport case $index") {
                    shouldThrow<IllegalArgumentException> {
                        createInvalid()
                    }
                }
            }
        test("rejects blank request IDs") {
            shouldThrow<IllegalArgumentException> {
                ViewportSubscription(" ", validBounds, emptyList(), true)
            }
        }

        test("rejects blank and duplicate route codes") {
            shouldThrow<IllegalArgumentException> {
                ViewportSubscription("request-1", validBounds, listOf("1", "1"), true)
            }
            shouldThrow<IllegalArgumentException> {
                ViewportSubscription("request-1", validBounds, listOf(" "), true)
            }
        }

        test("defines the viewport stream endpoint contract") {
            TransitEndpoint.VIEWPORT_STREAM shouldBe "v1/viewport-stream"
            TransitEndpoint.VIEWPORT_STREAM_PROTOCOL shouldBe "navigo.viewport.v1"
        }
    })
