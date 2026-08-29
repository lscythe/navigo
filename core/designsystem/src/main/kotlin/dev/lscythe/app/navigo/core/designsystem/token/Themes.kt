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
package dev.lscythe.app.navigo.core.designsystem.token

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme

@Immutable
data class MaterialKolorConfig(
    val seedColor: Color = LightPrimaryColor,
    val isAmoled: Boolean = false,
    val paletteStyle: PaletteStyle = PaletteStyle.Expressive,
    val specVersion: ColorSpec.SpecVersion = ColorSpec.SpecVersion.SPEC_2025,
    val contrastLevel: Double = Contrast.Default.value,
    val primary: Color? = null,
    val secondary: Color? = null,
    val tertiary: Color? = null,
    val neutral: Color? = null,
    val neutralVariant: Color? = null,
    val error: Color? = null,
) {
    init {
        require(paletteStyle in supportedPaletteStyles(specVersion)) {
            "$paletteStyle is not supported by $specVersion"
        }
    }
}

fun supportedPaletteStyles(specVersion: ColorSpec.SpecVersion): List<PaletteStyle> =
    when (specVersion) {
        ColorSpec.SpecVersion.SPEC_2025 ->
            listOf(
                PaletteStyle.TonalSpot,
                PaletteStyle.Neutral,
                PaletteStyle.Vibrant,
                PaletteStyle.Expressive,
            )

        ColorSpec.SpecVersion.SPEC_2021 -> PaletteStyle.entries
    }

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavigoTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    disableDynamicTheming: Boolean = true,
    materialKolor: MaterialKolorConfig? = null,
    content: @Composable () -> Unit,
) {
    val defaultColorScheme =
        when {
            !disableDynamicTheming && supportsDynamicTheming() -> {
                val context = LocalContext.current
                if (isDarkTheme) dynamicDarkColorScheme(context)
                else dynamicLightColorScheme(context)
            }
            else -> if (isDarkTheme) DarkColorScheme else LightColorScheme
        }
    val colorScheme =
        materialKolor?.let { config ->
            rememberDynamicColorScheme(
                seedColor = config.seedColor,
                isDark = isDarkTheme,
                isAmoled = config.isAmoled,
                style = config.paletteStyle,
                specVersion = config.specVersion,
                contrastLevel = config.contrastLevel,
                primary = config.primary,
                secondary = config.secondary,
                tertiary = config.tertiary,
                neutral = config.neutral,
                neutralVariant = config.neutralVariant,
                error = config.error,
            )
        } ?: defaultColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        motionScheme = MotionScheme.expressive(),
        typography = NavigoTypography,
        shapes = NavigoShapes,
        content = content,
    )
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
