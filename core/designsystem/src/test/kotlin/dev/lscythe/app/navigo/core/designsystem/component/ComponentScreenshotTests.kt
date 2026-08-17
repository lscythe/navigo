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

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoAlertCard
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoDisclosureNote
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoFilterChip
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoListContainer
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoSearchBar
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoStatusChip
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoTextField
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoUnderlinedTextField
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.status.ExclamationCircle
import dev.lscythe.app.navigo.core.designsystem.icon.status.Notification
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.testing.screenshot.captureMultiTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class ComponentScreenshotTests {
    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test fun filterChip_multipleThemes() = capture("FilterChip") { FilterChipContent() }

    @Test fun statusChip_multipleThemes() = capture("StatusChip") { StatusChipContent() }

    @Test fun searchBar_multipleThemes() = capture("SearchBar") { SearchBarContent() }

    @Test fun textField_multipleThemes() = capture("TextField") { TextFieldContent() }

    @Test
    fun underlinedTextField_multipleThemes() =
        capture("UnderlinedTextField") { UnderlinedTextFieldContent() }

    @Test
    fun disclosureNote_multipleThemes() = capture("DisclosureNote") { DisclosureNoteContent() }

    @Test fun alertCard_multipleThemes() = capture("AlertCard") { AlertCardContent() }

    @Test fun listContainer_multipleThemes() = capture("ListContainer") { ListContainerContent() }

    private fun capture(name: String, content: @Composable () -> Unit) {
        composeTestRule.captureMultiTheme(name) { content() }
    }
}

@Composable
private fun FilterChipContent() {
    Row(modifier = Modifier.padding(NavigoSpacing.cardPadding)) {
        NavigoFilterChip(selected = true, onClick = {}, label = "Buses", count = "12")
        NavigoFilterChip(selected = false, onClick = {}, label = "Stops")
    }
}

@Composable
private fun StatusChipContent() {
    NavigoStatusChip(label = "Live · 20s", dotColor = MaterialTheme.colorScheme.primary)
}

@Composable
private fun SearchBarContent() {
    NavigoSearchBar(query = "Melati", onQueryChange = {}, onSearch = {}, onClear = {})
}

@Composable
private fun TextFieldContent() {
    NavigoTextField(
        value = "Jalan Melati",
        onValueChange = {},
        helperText = "Complete address",
        maxLength = 120,
        modifier = Modifier.fillMaxWidth(),
        minLines = 2,
        maxLines = 4,
    )
}

@Composable
private fun UnderlinedTextFieldContent() {
    NavigoUnderlinedTextField(
        value = "Ayu K.",
        onValueChange = {},
        label = "DISPLAY NAME",
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun DisclosureNoteContent() {
    NavigoDisclosureNote(
        text = "Reports are anonymous. Location is shared only while you're on a bus.",
        icon = NavigoIcons.ExclamationCircle,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun AlertCardContent() {
    NavigoAlertCard(
        source = "NAVIGO",
        category = "STOP ALARM",
        message = "Get off at Jalan Melati — next stop",
        timestamp = "now",
        icon = NavigoIcons.Notification,
        modifier = Modifier.fillMaxWidth(),
        onClick = {},
    )
}

@Composable
private fun ListContainerContent() {
    NavigoListContainer(itemCount = 3, modifier = Modifier.fillMaxWidth()) { index ->
        Row(modifier = Modifier.fillMaxWidth().padding(NavigoSpacing.cardPadding)) {
            Text(listOf("First option", "Second option", "Third option")[index])
        }
    }
}
