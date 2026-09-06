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
package dev.lscythe.app.navigo.core.testing.screenshot

import android.graphics.Bitmap.CompressFormat.PNG
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.DarkMode
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziATFAccessibilityCheckOptions
import com.github.takahirom.roborazzi.RoborazziATFAccessibilityChecker
import com.github.takahirom.roborazzi.RoborazziATFAccessibilityChecker.CheckLevel
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import com.github.takahirom.roborazzi.checkRoboAccessibility
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckPreset
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityViewCheckResult
import com.google.android.apps.common.testing.accessibility.framework.integrations.espresso.AccessibilityViewCheckException
import com.google.android.apps.common.testing.accessibility.framework.utils.contrast.BitmapImage
import java.io.File
import java.io.FileOutputStream
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.robolectric.RuntimeEnvironment

enum class DefaultTestDevices(val description: String, val spec: String) {
    PHONE("phone", "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480"),
    FOLDABLE("foldable", "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480"),
    TABLET("tablet", "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480"),
}

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.captureMultiDevice(
    screenshotName: String,
    accessibilitySuppressions: Matcher<in AccessibilityViewCheckResult> =
        Matchers.not(Matchers.anything()),
    body: @Composable () -> Unit,
) {
    DefaultTestDevices.entries.forEach {
        this.captureForDevice(
            deviceName = it.description,
            deviceSpec = it.spec,
            screenshotName = screenshotName,
            body = body,
            accessibilitySuppressions = accessibilitySuppressions,
        )
    }
}

@OptIn(ExperimentalRoborazziApi::class)
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.captureForDevice(
    deviceName: String,
    deviceSpec: String,
    screenshotName: String,
    roborazziOptions: RoborazziOptions = DefaultRoborazziOptions,
    accessibilitySuppressions: Matcher<in AccessibilityViewCheckResult> =
        Matchers.not(Matchers.anything()),
    darkMode: Boolean = false,
    body: @Composable () -> Unit,
) {
    val (width, height, dpi) = extractSpecs(deviceSpec)

    // Set qualifiers from specs
    RuntimeEnvironment.setQualifiers("w${width}dp-h${height}dp-${dpi}dpi")

    this.activity.setContent {
        CompositionLocalProvider(LocalInspectionMode provides true) {
            DeviceConfigurationOverride(
                override = DeviceConfigurationOverride.Companion.DarkMode(darkMode)
            ) {
                body()
            }
        }
    }

    // Run Accessibility checks first so logging is included
    val accessibilityException =
        try {
            this.onRoot()
                .checkRoboAccessibility(
                    roborazziATFAccessibilityCheckOptions =
                        RoborazziATFAccessibilityCheckOptions(
                            failureLevel = CheckLevel.Error,
                            checker =
                                RoborazziATFAccessibilityChecker(
                                    preset = AccessibilityCheckPreset.LATEST,
                                    suppressions = accessibilitySuppressions,
                                ),
                        )
                )
            null
        } catch (e: AccessibilityViewCheckException) {
            e
        }

    this.onRoot()
        .captureRoboImage(
            "src/test/screenshots/${screenshotName}_$deviceName.png",
            roborazziOptions = roborazziOptions,
        )

    // Rethrow the Accessibility exception once screenshots have passed
    if (accessibilityException != null) {
        accessibilityException.results.forEachIndexed { index, check ->
            val viewImage = check.viewImage
            if (viewImage is BitmapImage) {
                val file =
                    File("build/outputs/roborazzi/${screenshotName}_${deviceName}_$index.png")
                println("Writing check.viewImage to $file")
                FileOutputStream(file).use {
                    viewImage.bitmap.compress(PNG, 100, it)
                }
            }
        }

        throw accessibilityException
    }
}

/** Captures deterministic dark/light and default/alternate theme combinations. */
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.captureMultiTheme(
    name: String,
    overrideFileName: String? = null,
    shouldCompareDarkMode: Boolean = true,
    shouldCompareAlternateTheme: Boolean = true,
    theme:
        @Composable
        (isDark: Boolean, useAlternateTheme: Boolean, content: @Composable () -> Unit) -> Unit,
    content: @Composable (description: String) -> Unit,
) {
    var isDarkMode by mutableStateOf(true)
    var useAlternateTheme by mutableStateOf(false)
    setContent {
        CompositionLocalProvider(LocalInspectionMode provides true) {
            theme(isDarkMode, useAlternateTheme) {
                key(isDarkMode, useAlternateTheme) {
                    content(
                        generateDescription(
                            shouldCompareDarkMode,
                            isDarkMode,
                            shouldCompareAlternateTheme,
                            useAlternateTheme,
                        )
                    )
                }
            }
        }
    }

    val filename = overrideFileName ?: name
    val darkModes = if (shouldCompareDarkMode) listOf(true, false) else listOf(false)
    val alternateThemes = if (shouldCompareAlternateTheme) listOf(true, false) else listOf(false)
    darkModes.forEach { darkMode ->
        isDarkMode = darkMode
        alternateThemes.forEach { alternateTheme ->
            useAlternateTheme = alternateTheme
            onRoot()
                .captureRoboImage(
                    "src/test/screenshots/$name/" +
                        "${filename}_${if (darkMode) "dark" else "light"}_" +
                        "${if (alternateTheme) "materialKolor" else "androidTheme"}_notDynamic.png",
                    roborazziOptions = DefaultRoborazziOptions,
                )
        }
    }
}

private fun generateDescription(
    shouldCompareDarkMode: Boolean,
    darkMode: Boolean,
    shouldCompareAlternateTheme: Boolean,
    useAlternateTheme: Boolean,
): String = buildList {
    if (shouldCompareDarkMode) add(if (darkMode) "Dark" else "Light")
    if (shouldCompareAlternateTheme) add(if (useAlternateTheme) "Alternate" else "Default")
}
    .joinToString(" ")

/** Extracts some properties from the spec string. Note that this function is not exhaustive. */
private fun extractSpecs(deviceSpec: String): TestDeviceSpecs {
    val specs =
        deviceSpec
            .substringAfter("spec:")
            .split(",")
            .map { it.split("=") }
            .associate { it[0] to it[1] }
    val width = specs["width"]?.toInt() ?: 640
    val height = specs["height"]?.toInt() ?: 480
    val dpi = specs["dpi"]?.toInt() ?: 480
    return TestDeviceSpecs(width, height, dpi)
}

data class TestDeviceSpecs(val width: Int, val height: Int, val dpi: Int)
