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
package dev.lscythe.app.navigo.core.designsystem.preview

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.lscythe.app.navigo.core.designsystem.token.MaterialKolorConfig
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.designsystem.token.NavigoTheme

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class NavigoThemePreview

@Preview(
    name = "Material Kolor Light",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    name = "Material Kolor Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
annotation class NavigoMaterialKolorThemePreview

@Composable
fun NavigoPreview(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(NavigoSpacing.cardPadding),
    materialKolor: MaterialKolorConfig? = null,
    content: @Composable () -> Unit,
) {
    NavigoTheme(
        isDarkTheme = isSystemInDarkTheme(),
        materialKolor = materialKolor,
    ) {
        Surface(modifier = modifier) {
            Box(modifier = Modifier.padding(contentPadding)) {
                content()
            }
        }
    }
}

@Composable
fun NavigoMaterialKolorPreview(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(NavigoSpacing.cardPadding),
    materialKolor: MaterialKolorConfig = MaterialKolorConfig(),
    content: @Composable () -> Unit,
) {
    NavigoPreview(
        modifier = modifier,
        contentPadding = contentPadding,
        materialKolor = materialKolor,
        content = content,
    )
}
