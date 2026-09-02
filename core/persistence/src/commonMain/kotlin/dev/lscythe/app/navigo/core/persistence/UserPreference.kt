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
package dev.lscythe.app.navigo.core.persistence

import kotlinx.serialization.Serializable

@Serializable
enum class Language {
    System,
    English,
    Indonesian,
}

@Serializable
enum class ThemeMode {
    System,
    Light,
    Dark,
}

@Serializable
enum class PaletteStyle {
    Unspecified,
    TonalSpot,
    Neutral,
    Vibrant,
    Expressive,
    Rainbow,
    FruitSalad,
    Monochrome,
    Fidelity,
    Content,
}

@Serializable
enum class ColorSpecVersion {
    Unspecified,
    Version2021,
    Version2025,
}

@Serializable
data class MaterialKolorPreference(
    val seedColorArgb: UInt = 0u,
    val isAmoled: Boolean = false,
    val paletteStyle: PaletteStyle = PaletteStyle.Unspecified,
    val specVersion: ColorSpecVersion = ColorSpecVersion.Unspecified,
    val contrastLevel: Double = 0.0,
    val primaryArgb: UInt? = null,
    val secondaryArgb: UInt? = null,
    val tertiaryArgb: UInt? = null,
    val neutralArgb: UInt? = null,
    val neutralVariantArgb: UInt? = null,
    val errorArgb: UInt? = null,
)

@Serializable
data class ThemePreference(
    val mode: ThemeMode = ThemeMode.System,
    val materialKolor: MaterialKolorPreference? = null,
)

@Serializable
data class UserPreference(
    val language: Language = Language.System,
    val theme: ThemePreference = ThemePreference(),
    val hasCompletedOnboarding: Boolean = false,
)
