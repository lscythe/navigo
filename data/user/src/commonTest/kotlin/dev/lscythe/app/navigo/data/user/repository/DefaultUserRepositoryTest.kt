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
package dev.lscythe.app.navigo.data.user.repository

import app.cash.turbine.test
import dev.lscythe.app.navigo.core.persistence.ThemeMode
import dev.lscythe.app.navigo.core.persistence.ThemePreference
import dev.lscythe.app.navigo.core.persistence.UserPreference
import dev.lscythe.app.navigo.domain.user.model.UserProfile
import dev.lscythe.app.navigo.domain.user.repository.UserFailure
import dev.lscythe.app.navigo.domain.user.repository.UserResult
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultUserRepositoryTest :
    FunSpec({
        test("blank persisted profile maps to null") {
            DefaultUserRepository(FakeUserPreferenceStore()).profile.test {
                awaitItem() shouldBe null
                cancelAndIgnoreRemainingEvents()
            }
        }

        test("persisted profile maps to domain profile") {
            val store =
                FakeUserPreferenceStore(
                    UserPreference(displayName = "Nara", avatarColorArgb = 0xff123456u)
                )

            DefaultUserRepository(store).profile.test {
                awaitItem() shouldBe UserProfile("Nara", 0xff123456u)
                cancelAndIgnoreRemainingEvents()
            }
        }

        test("update trims display name and preserves unrelated preferences") {
            val theme = ThemePreference(mode = ThemeMode.Dark)
            val store = FakeUserPreferenceStore(UserPreference(theme = theme))

            DefaultUserRepository(store)
                .updateProfile(UserProfile("  Nara  ", 0xff123456u)) shouldBe
                UserResult.Success(Unit)

            store.value.displayName shouldBe "Nara"
            store.value.avatarColorArgb shouldBe 0xff123456u
            store.value.theme shouldBe theme
        }

        test("blank update fails before persistence") {
            val store = FakeUserPreferenceStore()

            DefaultUserRepository(store).updateProfile(UserProfile("  ", 1u)) shouldBe
                UserResult.Failure(UserFailure.InvalidDisplayName)
            store.updateCalls shouldBe 0
        }

        test("persistence exception becomes typed failure") {
            val repository =
                DefaultUserRepository(
                    FakeUserPreferenceStore(failure = IllegalStateException("disk"))
                )

            repository.updateProfile(UserProfile("Nara", 1u)) shouldBe
                UserResult.Failure(UserFailure.Persistence("disk"))
        }

        test("cancellation propagates") {
            val repository =
                DefaultUserRepository(FakeUserPreferenceStore(failure = CancellationException()))

            shouldThrow<CancellationException> {
                repository.updateProfile(UserProfile("Nara", 1u))
            }
        }
    })

private class FakeUserPreferenceStore(
    initial: UserPreference = UserPreference(),
    private val failure: Throwable? = null,
) : UserPreferenceStore {
    private val state = MutableStateFlow(initial)
    var updateCalls = 0
    val value: UserPreference
        get() = state.value

    override val data = state

    override suspend fun setProfile(displayName: String, avatarColorArgb: UInt) {
        updateCalls++
        failure?.let { throw it }
        state.value = state.value.copy(displayName = displayName, avatarColorArgb = avatarColorArgb)
    }
}
