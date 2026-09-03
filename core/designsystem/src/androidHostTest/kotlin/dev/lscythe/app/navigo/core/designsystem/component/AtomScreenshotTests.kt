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

// TODO(wip): restore with icon-button atoms removed from Button.kt mid-refactor.
// import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoFilledIconButton
// import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoFilledTonalIconButton
// import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIconButton
// import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoOutlinedIconButton
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoAvatar
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoBadge
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoCheckbox
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoCircularLoadingIndicator
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoCircularProgressIndicator
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoDot
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoElevatedButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoFilledTonalButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoIcon
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoLinearLoadingIndicator
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoLinearProgressIndicator
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoOutlinedButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoPagerIndicator
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoProgressSegment
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoRadioButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoSegmentedLinearProgressIndicator
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoSwitch
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoTextButton
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons
import dev.lscythe.app.navigo.core.designsystem.icon.status.Circle
import dev.lscythe.app.navigo.core.designsystem.token.MaterialKolorConfig
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme
import dev.lscythe.app.navigo.core.testing.screenshot.captureMultiTheme
import kotlinx.collections.immutable.persistentListOf
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
class AtomScreenshotTests {
    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test fun avatar_multipleThemes() = capture("Avatar") { AvatarContent() }

    @Test fun badge_multipleThemes() = capture("Badge") { BadgeContent() }

    @Test fun dot_multipleThemes() = capture("Dot") { DotContent() }

    @Test
    fun elevatedButton_multipleThemes() =
        capture("ElevatedButton") { NavigoElevatedButton({}) { Text("Elevated") } }

    @Test fun button_multipleThemes() = capture("Button") { NavigoButton({}) { Text("Filled") } }

    @Test
    fun filledTonalButton_multipleThemes() =
        capture("FilledTonalButton") { NavigoFilledTonalButton({}) { Text("Tonal") } }

    @Test
    fun outlinedButton_multipleThemes() =
        capture("OutlinedButton") { NavigoOutlinedButton({}) { Text("Outlined") } }

    @Test
    fun textButton_multipleThemes() =
        capture("TextButton") { NavigoTextButton({}) { Text("Text") } }

    // TODO(wip): restore these four tests together with the icon-button atoms that were
    // removed from component/atom/Button.kt mid-refactor. Bodies preserved verbatim.
    // @Test
    // fun iconButton_multipleThemes() =
    //     capture("IconButton") { NavigoIconButton({}) { PreviewIcon() } }
    //
    // @Test
    // fun filledIconButton_multipleThemes() =
    //     capture("FilledIconButton") { NavigoFilledIconButton({}) { PreviewIcon() } }
    //
    // @Test
    // fun filledTonalIconButton_multipleThemes() =
    //     capture("FilledTonalIconButton") { NavigoFilledTonalIconButton({}) { PreviewIcon() } }
    //
    // @Test
    // fun outlinedIconButton_multipleThemes() =
    //     capture("OutlinedIconButton") { NavigoOutlinedIconButton({}) { PreviewIcon() } }

    @Test fun icon_multipleThemes() = capture("Icon") { IconContent() }

    @Test fun checkbox_multipleThemes() = capture("Checkbox") { CheckboxContent() }

    @Test fun radioButton_multipleThemes() = capture("RadioButton") { RadioButtonContent() }

    @Test fun switch_multipleThemes() = capture("Switch") { SwitchContent() }

    @Test
    fun circularProgressIndicator_multipleThemes() =
        capture("CircularProgressIndicator") { CircularProgressContent() }

    @Test
    fun circularLoadingIndicator_multipleThemes() =
        capture("CircularLoadingIndicator") { NavigoCircularLoadingIndicator() }

    @Test
    fun linearProgressIndicator_multipleThemes() =
        capture("LinearProgressIndicator") {
            NavigoLinearProgressIndicator(0.65f, Modifier.fillMaxWidth())
        }

    @Test
    fun segmentedLinearProgressIndicator_multipleThemes() =
        capture("SegmentedLinearProgressIndicator") { SegmentedLinearProgressContent() }

    @Test
    fun linearLoadingIndicator_multipleThemes() =
        capture("LinearLoadingIndicator") { NavigoLinearLoadingIndicator() }

    @Test
    fun pagerIndicator_multipleThemes() =
        capture("PagerIndicator") { NavigoPagerIndicator(4, 1, 0.65f) }

    private fun capture(name: String, content: @Composable () -> Unit) {
        composeTestRule.captureMultiTheme(
            name = name,
            theme = { isDark, useMaterialKolor, themedContent ->
                NavigoTheme(
                    isDarkTheme = isDark,
                    disableDynamicTheming = true,
                    materialKolor = MaterialKolorConfig().takeIf { useMaterialKolor },
                    content = themedContent,
                )
            },
        ) {
            content()
        }
    }
}

@Composable
private fun AvatarContent() =
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
        NavigoAvatar(text = "AK", shape = MaterialTheme.shapes.extraLarge)
        NavigoAvatar(text = "27B")
        NavigoAvatar(ColorPainter(MaterialTheme.colorScheme.tertiaryContainer), "Avatar image")
    }

@Composable
private fun BadgeContent() =
    Column(verticalArrangement = Arrangement.spacedBy(NavigoSpacing.element)) {
        NavigoBadge("3")
        NavigoBadge("REQUIRED", containerColor = MaterialTheme.colorScheme.surfaceVariant)
    }

@Composable
private fun DotContent() =
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.container)) {
        NavigoDot(color = MaterialTheme.colorScheme.primary)
        NavigoDot(color = MaterialTheme.colorScheme.secondary)
        NavigoDot(color = MaterialTheme.colorScheme.tertiary)
    }

// @Composable private fun PreviewIcon() = Icon(NavigoIcons.Circle, contentDescription = null)

@Composable
private fun IconContent() =
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.container)) {
        NavigoIcon(NavigoIcons.Circle, "Circle")
        NavigoIcon(NavigoIcons.Circle, "Large circle", size = 32.dp)
        NavigoIcon(NavigoIcons.Circle, null, tint = MaterialTheme.colorScheme.primary)
    }

@Composable
private fun CheckboxContent() = Column {
    NavigoCheckbox(false, {})
    NavigoCheckbox(true, null)
    NavigoCheckbox(false, null, enabled = false)
    NavigoCheckbox(true, null, enabled = false)
}

@Composable
private fun RadioButtonContent() = Column {
    NavigoRadioButton(false, {})
    NavigoRadioButton(true, null)
    NavigoRadioButton(false, null, enabled = false)
    NavigoRadioButton(true, null, enabled = false)
}

@Composable
private fun SwitchContent() = Column {
    NavigoSwitch(false, {})
    NavigoSwitch(true, null)
    NavigoSwitch(false, null, enabled = false)
    NavigoSwitch(true, null, enabled = false)
}

@Composable
private fun CircularProgressContent() =
    Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.container)) {
        NavigoCircularProgressIndicator(0.25f)
        NavigoCircularProgressIndicator(0.65f)
        NavigoCircularProgressIndicator(1f)
    }

@Composable
private fun SegmentedLinearProgressContent() {
    NavigoSegmentedLinearProgressIndicator(
        persistentListOf(
            NavigoProgressSegment(0.3f, MaterialTheme.colorScheme.primary),
            NavigoProgressSegment(0.25f, MaterialTheme.colorScheme.tertiary),
        ),
        Modifier.fillMaxWidth(),
    )
}
