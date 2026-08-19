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
package dev.lscythe.app.navigo.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

private object TestStartKey : NavKey

private object TestFirstKey : NavKey

private object TestSecondKey : NavKey

class NavigatorTest :
    FunSpec({
        lateinit var navigationState: NavigationState
        lateinit var navigator: Navigator

        beforeTest {
            navigationState = NavigationState(NavBackStack(TestStartKey))
            navigator = Navigator(navigationState)
        }

        test("starts at the initial key") {
            navigationState.currentKey shouldBe TestStartKey
            navigationState.backStack shouldContainExactly listOf(TestStartKey)
        }

        test("navigates forward") {
            navigator.navigate(TestFirstKey)
            navigator.navigate(TestSecondKey)

            navigationState.backStack shouldContainExactly
                listOf(TestStartKey, TestFirstKey, TestSecondKey)
            navigationState.currentKey shouldBe TestSecondKey
        }

        test("does not duplicate the current key") {
            navigator.navigate(TestFirstKey)
            navigator.navigate(TestFirstKey)

            navigationState.backStack shouldContainExactly listOf(TestStartKey, TestFirstKey)
        }

        test("preserves history when revisiting an older key") {
            navigator.navigate(TestFirstKey)
            navigator.navigate(TestSecondKey)
            navigator.navigate(TestFirstKey)

            navigationState.backStack shouldContainExactly
                listOf(TestStartKey, TestFirstKey, TestSecondKey, TestFirstKey)
        }

        test("pops the current key") {
            navigator.navigate(TestFirstKey)

            navigator.goBack()

            navigationState.backStack shouldContainExactly listOf(TestStartKey)
            navigationState.currentKey shouldBe TestStartKey
        }

        test("throws when going back from the start key") {
            shouldThrow<IllegalStateException> {
                navigator.goBack()
            }
        }
    })
