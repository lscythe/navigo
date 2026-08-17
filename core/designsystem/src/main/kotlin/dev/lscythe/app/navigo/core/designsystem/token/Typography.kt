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

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import dev.lscythe.app.navigo.core.designsystem.R

@OptIn(ExperimentalTextApi::class)
private fun variableFontFamily(resourceId: Int): FontFamily =
    FontFamily(
        listOf(
                FontWeight.Thin,
                FontWeight.ExtraLight,
                FontWeight.Light,
                FontWeight.Normal,
                FontWeight.Medium,
                FontWeight.SemiBold,
                FontWeight.Bold,
                FontWeight.ExtraBold,
                FontWeight.Black,
            )
            .map { weight ->
                Font(
                    resId = resourceId,
                    weight = weight,
                    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
                )
            }
    )

val PlusJakartaSansFontFamily = variableFontFamily(R.font.plus_jakarta_sans_variable_font_weight)

val JetBrainsMonoFontFamily = variableFontFamily(R.font.jetbrains_mono_variable_font_weight)

val NavigoTypography =
    Typography(
        displayLarge = navigoTextStyle(57, 64, -0.02f, FontWeight.ExtraBold),
        displayMedium = navigoTextStyle(45, 52, -0.02f, FontWeight.ExtraBold),
        displaySmall = navigoTextStyle(33, 40, -0.02f, FontWeight.ExtraBold),
        headlineLarge = navigoTextStyle(31, 38, -0.015f, FontWeight.ExtraBold),
        headlineMedium = navigoTextStyle(28, 35, -0.015f, FontWeight.ExtraBold),
        headlineSmall = navigoTextStyle(25, 32, -0.015f, FontWeight.ExtraBold),
        titleLarge = navigoTextStyle(19, 26, fontWeight = FontWeight.ExtraBold),
        titleMedium = navigoTextStyle(16, 22, fontWeight = FontWeight.ExtraBold),
        titleSmall = navigoTextStyle(14, 20, fontWeight = FontWeight.ExtraBold),
        bodyLarge = navigoTextStyle(15, 23, fontWeight = FontWeight.Medium),
        bodyMedium = navigoTextStyle(13, 19, fontWeight = FontWeight.Medium),
        bodySmall = navigoTextStyle(12, 17, fontWeight = FontWeight.Medium),
        labelLarge = navigoTextStyle(15, 20, 0.01f, FontWeight.ExtraBold),
        labelMedium = navigoTextStyle(12, 16, 0.01f, FontWeight.ExtraBold),
        labelSmall = navigoTextStyle(11, 16, 0.14f, FontWeight.Medium, JetBrainsMonoFontFamily),
    )

private fun navigoTextStyle(
    fontSize: Int,
    lineHeight: Int,
    letterSpacing: Float = 0f,
    fontWeight: FontWeight,
    fontFamily: FontFamily = PlusJakartaSansFontFamily,
) =
    TextStyle(
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize.sp,
        lineHeight = lineHeight.sp,
        letterSpacing = letterSpacing.em,
    )
