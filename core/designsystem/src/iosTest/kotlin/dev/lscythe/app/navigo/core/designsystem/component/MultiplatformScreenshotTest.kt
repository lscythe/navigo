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
package dev.lscythe.app.navigo.core.designsystem.component

import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RoborazziOptions
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class MultiplatformScreenshotTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun theme() = runComposeUiTest {
        setContent { NavigoTheme(disableDynamicTheming = true) { Text("Navigo") } }
        onRoot()
            .captureRoboImage(
                composeUiTest = this,
                filePath = "src/test/screenshots-multiplatform/ios/theme.png",
                roborazziOptions = RoborazziOptions(),
            )
    }
}
