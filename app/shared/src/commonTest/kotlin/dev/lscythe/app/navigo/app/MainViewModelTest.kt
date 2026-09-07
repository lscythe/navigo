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
package dev.lscythe.app.navigo.app

import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.model.AuthSession
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Instant
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest :
    FunSpec({
        test("routes incomplete user to onboarding after authentication") {
            runTest {
                val viewModel =
                    StartupCoordinator(false, { AuthResult.Success(session()) }, backgroundScope)
                runCurrent()
                viewModel.state.value shouldBe StartupState.Ready(StartupDestination.Onboarding)
            }
        }

        test("routes complete user to Home after authentication") {
            runTest {
                val viewModel =
                    StartupCoordinator(true, { AuthResult.Success(session()) }, backgroundScope)
                runCurrent()
                viewModel.state.value shouldBe StartupState.Ready(StartupDestination.Home)
            }
        }

        test("auth failure releases splash and blocks Home") {
            runTest {
                val failure = AuthFailure.Network("offline")
                val viewModel =
                    StartupCoordinator(true, { AuthResult.Failure(failure) }, backgroundScope)
                runCurrent()
                viewModel.state.value shouldBe
                    StartupState.AuthRequired(StartupDestination.Home, failure)
            }
        }

        test("startup deadline releases splash after ten seconds") {
            runTest {
                val viewModel =
                    StartupCoordinator(
                        true,
                        bootstrap = {
                            delay(Long.MAX_VALUE)
                            error("unreachable")
                        },
                        scope = backgroundScope,
                    )
                advanceTimeBy(9_999)
                runCurrent()
                viewModel.state.value shouldBe StartupState.Initializing
                advanceTimeBy(1)
                runCurrent()
                viewModel.state.value shouldBe
                    StartupState.AuthRequired(StartupDestination.Home, AuthFailure.Network())
            }
        }

        test("retry starts one new attempt and ignores stale completion") {
            runTest {
                val first = CompletableDeferred<AuthResult<AuthSession>>()
                var calls = 0
                val viewModel =
                    StartupCoordinator(
                        true,
                        bootstrap = {
                            calls++
                            if (calls == 1) first.await() else AuthResult.Success(session())
                        },
                        scope = backgroundScope,
                    )
                runCurrent()
                viewModel.retryAuthentication()
                runCurrent()
                first.complete(AuthResult.Failure(AuthFailure.Server()))
                runCurrent()
                viewModel.state.value shouldBe StartupState.Ready(StartupDestination.Home)
                calls shouldBe 2
            }
        }
    })

private fun session() =
    AuthSession(
        "id",
        "access",
        "refresh",
        Instant.parse("2026-09-08T13:00:00Z"),
        Instant.parse("2026-09-09T13:00:00Z"),
        "installation",
    )
