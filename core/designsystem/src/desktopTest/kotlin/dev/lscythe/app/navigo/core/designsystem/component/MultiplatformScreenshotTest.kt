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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.brand.NavigoBrand
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoAvatar
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoBadge
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoCheckbox
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoCircularProgressIndicator
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoLinearProgressIndicator
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoPagerIndicator
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoRadioButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoSwitch
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoAlertCard
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoChoiceChip
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoDisclosureNote
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoFilterChip
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoSearchBar
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoStatusChip
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoTextField
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.status.Circle
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme
import dev.lscythe.app.navigo.core.testing.screenshot.captureDesktopScreenshot
import kotlin.test.Test

class MultiplatformScreenshotTest {
    @Test fun controls() = capture("controls", ::ControlsGallery)

    @Test fun content() = capture("content", ::ContentGallery)

    private fun capture(name: String, content: @Composable () -> Unit) =
        captureDesktopScreenshot("src/test/screenshots-multiplatform/desktop/$name.png") {
            NavigoTheme(disableDynamicTheming = true) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element),
                ) {
                    content()
                }
            }
        }
}

@Composable
private fun ControlsGallery() {
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
        NavigoButton({}) { Text("Continue") }
        NavigoCheckbox(true, null)
        NavigoRadioButton(true, null)
        NavigoSwitch(true, null)
    }
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
        NavigoChoiceChip(true, {}, "English")
        NavigoFilterChip(false, {}, "Buses", count = "12")
        NavigoStatusChip("Live · 20s", MaterialTheme.colorScheme.primary)
    }
    NavigoSearchBar("Melati", {}, {}, {})
    NavigoTextField("Jalan Melati", {}, helperText = "Complete address", maxLength = 120)
    NavigoLinearProgressIndicator(0.65f, Modifier.fillMaxWidth())
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
        NavigoCircularProgressIndicator(0.65f)
        NavigoPagerIndicator(4, 1, 0.65f)
    }
}

@Composable
private fun ContentGallery() {
    NavigoBrand()
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
        NavigoAvatar("AK")
        NavigoBadge("3")
        NavigoIcon(NavigoIcons.Circle, "Status")
    }
    NavigoAlertCard(
        source = "NAVIGO",
        category = "STOP ALARM",
        title = "Get off at Jalan Melati",
        timestamp = "now",
        icon = NavigoIcons.Circle,
    )
    NavigoDisclosureNote(
        text = "Location is shared only while you're on a bus.",
        icon = NavigoIcons.Circle,
    )
}
