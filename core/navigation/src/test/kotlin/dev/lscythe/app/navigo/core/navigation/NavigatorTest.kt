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

private object TestFirstTopLevelKey : NavKey

private object TestSecondTopLevelKey : NavKey

private object TestThirdTopLevelKey : NavKey

private object TestKeyFirst : NavKey

private object TestKeySecond : NavKey

class NavigatorTest :
    FunSpec({
        lateinit var navigationState: NavigationState
        lateinit var navigator: Navigator

        beforeTest {
            val startKey = TestFirstTopLevelKey
            val topLevelStack = NavBackStack<NavKey>(startKey)
            val topLevelKeys =
                listOf(
                    startKey,
                    TestSecondTopLevelKey,
                    TestThirdTopLevelKey,
                )
            val subStacks = topLevelKeys.associateWith { key -> NavBackStack(key) }

            navigationState =
                NavigationState(
                    startKey = startKey,
                    topLevelStack = topLevelStack,
                    subStacks = subStacks,
                )
            navigator = Navigator(navigationState)
        }

        test("startKey") {
            navigationState.startKey shouldBe TestFirstTopLevelKey
            navigationState.currentTopLevelKey shouldBe TestFirstTopLevelKey
        }

        test("navigate") {
            navigator.navigate(TestKeyFirst)

            navigationState.currentTopLevelKey shouldBe TestFirstTopLevelKey
            navigationState.subStacks[TestFirstTopLevelKey]?.last() shouldBe TestKeyFirst
        }

        test("navigate top level") {
            navigator.navigate(TestSecondTopLevelKey)
            navigationState.currentTopLevelKey shouldBe TestSecondTopLevelKey
        }

        test("navigate single top") {
            navigator.navigate(TestKeyFirst)

            navigationState.currentSubStack shouldContainExactly
                listOf(
                    TestFirstTopLevelKey,
                    TestKeyFirst,
                )

            navigator.navigate(TestKeyFirst)

            navigationState.currentSubStack shouldContainExactly
                listOf(
                    TestFirstTopLevelKey,
                    TestKeyFirst,
                )
        }

        test("navigate top level single top") {
            navigator.navigate(TestSecondTopLevelKey)
            navigator.navigate(TestKeyFirst)

            navigationState.currentSubStack shouldContainExactly
                listOf(
                    TestSecondTopLevelKey,
                    TestKeyFirst,
                )

            navigator.navigate(TestSecondTopLevelKey)

            navigationState.currentSubStack shouldContainExactly listOf(TestSecondTopLevelKey)
        }

        test("sub stack") {
            navigator.navigate(TestKeyFirst)

            navigationState.currentKey shouldBe TestKeyFirst
            navigationState.currentTopLevelKey shouldBe TestFirstTopLevelKey

            navigator.navigate(TestKeySecond)

            navigationState.currentKey shouldBe TestKeySecond
            navigationState.currentTopLevelKey shouldBe TestFirstTopLevelKey
        }

        test("multi stack") {
            // add to start stack
            navigator.navigate(TestKeyFirst)

            navigationState.currentKey shouldBe TestKeyFirst
            navigationState.currentTopLevelKey shouldBe TestFirstTopLevelKey

            // navigate to new top level
            navigator.navigate(TestSecondTopLevelKey)

            navigationState.currentKey shouldBe TestSecondTopLevelKey
            navigationState.currentTopLevelKey shouldBe TestSecondTopLevelKey

            // add to new stack
            navigator.navigate(TestKeySecond)

            navigationState.currentKey shouldBe TestKeySecond
            navigationState.currentTopLevelKey shouldBe TestSecondTopLevelKey

            // go back to start stack
            navigator.navigate(TestFirstTopLevelKey)

            navigationState.currentKey shouldBe TestKeyFirst
            navigationState.currentTopLevelKey shouldBe TestFirstTopLevelKey
        }

        test("pop one non top level") {
            navigator.navigate(TestKeyFirst)
            navigator.navigate(TestKeySecond)

            navigationState.currentSubStack shouldContainExactly
                listOf(
                    TestFirstTopLevelKey,
                    TestKeyFirst,
                    TestKeySecond,
                )

            navigator.goBack()

            navigationState.currentSubStack shouldContainExactly
                listOf(
                    TestFirstTopLevelKey,
                    TestKeyFirst,
                )

            navigationState.currentKey shouldBe TestKeyFirst
            navigationState.currentTopLevelKey shouldBe TestFirstTopLevelKey
        }

        test("pop one top level") {
            navigator.navigate(TestKeyFirst)
            navigator.navigate(TestSecondTopLevelKey)

            navigationState.currentSubStack shouldContainExactly listOf(TestSecondTopLevelKey)

            navigationState.currentKey shouldBe TestSecondTopLevelKey
            navigationState.currentTopLevelKey shouldBe TestSecondTopLevelKey

            // remove TopLevel
            navigator.goBack()

            navigationState.currentSubStack shouldContainExactly
                listOf(
                    TestFirstTopLevelKey,
                    TestKeyFirst,
                )

            navigationState.currentKey shouldBe TestKeyFirst
            navigationState.currentTopLevelKey shouldBe TestFirstTopLevelKey
        }

        test("pop multiple non top level") {
            navigator.navigate(TestKeyFirst)
            navigator.navigate(TestKeySecond)

            navigationState.currentSubStack shouldContainExactly
                listOf(
                    TestFirstTopLevelKey,
                    TestKeyFirst,
                    TestKeySecond,
                )

            navigator.goBack()
            navigator.goBack()

            navigationState.currentSubStack shouldContainExactly listOf(TestFirstTopLevelKey)

            navigationState.currentKey shouldBe TestFirstTopLevelKey
            navigationState.currentTopLevelKey shouldBe TestFirstTopLevelKey
        }

        test("pop multiple top level") {
            // second sub-stack
            navigator.navigate(TestSecondTopLevelKey)
            navigator.navigate(TestKeyFirst)

            navigationState.currentSubStack shouldContainExactly
                listOf(
                    TestSecondTopLevelKey,
                    TestKeyFirst,
                )

            // third sub-stack
            navigator.navigate(TestThirdTopLevelKey)
            navigator.navigate(TestKeySecond)

            navigationState.currentSubStack shouldContainExactly
                listOf(
                    TestThirdTopLevelKey,
                    TestKeySecond,
                )

            repeat(4) {
                navigator.goBack()
            }

            navigationState.currentSubStack shouldContainExactly listOf(TestFirstTopLevelKey)

            navigationState.currentKey shouldBe TestFirstTopLevelKey
            navigationState.currentTopLevelKey shouldBe TestFirstTopLevelKey
        }

        test("throw on empty back stack") {
            shouldThrow<IllegalStateException> {
                navigator.goBack()
            }
        }
    })
