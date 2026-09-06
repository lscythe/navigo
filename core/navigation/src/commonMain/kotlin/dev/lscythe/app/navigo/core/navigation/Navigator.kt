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

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events (forward and back) by updating the navigation state.
 *
 * @param state - The navigation state that will be updated in response to navigation events.
 */
class Navigator(val state: NavigationState) {

    /**
     * Navigate to a navigation key
     *
     * @param key - the navigation key to navigate to.
     */
    fun navigate(key: NavKey) {
        if (key != state.currentKey) state.backStack.add(key)
    }

    /** Replace the current navigation key while preserving earlier history. */
    fun replaceCurrent(key: NavKey) {
        if (key == state.currentKey) return
        state.backStack[state.backStack.lastIndex] = key
    }

    /** Clear all navigation history and make [key] the new root. */
    fun resetTo(key: NavKey) {
        state.backStack.clear()
        state.backStack.add(key)
    }

    /** Remove every key after the most recent occurrence of [key]. */
    fun backTo(key: NavKey) {
        val targetIndex = state.backStack.indexOfLast { it == key }
        check(targetIndex >= 0) { "Navigation key $key is not in the back stack" }
        if (targetIndex < state.backStack.lastIndex) {
            state.backStack.subList(targetIndex + 1, state.backStack.size).clear()
        }
    }

    /** Go back to the previous navigation key. */
    fun goBack() {
        check(state.backStack.size > 1) { "You cannot go back from the start route" }
        state.backStack.removeLastOrNull()
    }
}
