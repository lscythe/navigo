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
package dev.lscythe.app.navigo.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class NavigoTestSemanticsTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun exposesComposeTestTagsAsAndroidResourceIds() {
        composeRule.setContent {
            Box(Modifier.navigoTestSemantics().testTag("shared-test-tag"))
        }

        composeRule
            .onNodeWithTag("shared-test-tag")
            .assert(
                SemanticsMatcher("test tags exposed as resource IDs") { node ->
                    node.config.any { entry ->
                        entry.key.name == "TestTagsAsResourceId" && entry.value == true
                    }
                }
            )
    }
}
