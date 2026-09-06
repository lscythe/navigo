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
package dev.lscythe.app.navigo.core.designsystem.brand

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.LightOnPrimaryColor
import dev.lscythe.app.navigo.core.designsystem.token.LightPrimaryColor
import dev.lscythe.app.navigo.core.resources.generated.resources.Res
import dev.lscythe.app.navigo.core.resources.generated.resources.core_designsystem_brand_name
import dev.lscythe.app.navigo.core.resources.generated.resources.core_designsystem_brand_tag
import kotlin.math.min
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

/**
 * Displays the Navigo logomark, wordmark, and optional tagline as a horizontal lockup.
 *
 * @param modifier modifier applied to the complete brand lockup.
 * @param showTagline whether to display the brand tagline below the wordmark.
 * @param logoSize size of the logomark.
 * @param pinColor color of the pin and wordmark; light themes use deep green and dark themes white.
 * @param accentColor color of the compass arrow and tagline.
 * @param wordmarkStyle typography used for the Navigo wordmark.
 * @param taglineStyle typography used for the tagline.
 */
@Composable
fun NavigoBrand(
    modifier: Modifier = Modifier,
    showTagline: Boolean = true,
    logoSize: Dp = 54.dp,
    pinColor: Color = Color.Unspecified,
    accentColor: Color = LightOnPrimaryColor,
    wordmarkStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    taglineStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val resolvedPinColor =
        if (pinColor != Color.Unspecified) pinColor
        else if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color.White
        else LightPrimaryColor
    Layout(
        modifier = modifier,
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val logo =
                    remember(resolvedPinColor, accentColor) {
                        navigoLogo(resolvedPinColor, accentColor)
                    }
                Image(
                    imageVector = logo,
                    contentDescription = null,
                    modifier = Modifier.size(logoSize),
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = stringResource(Res.string.core_designsystem_brand_name),
                        color = resolvedPinColor,
                        style = wordmarkStyle,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                    )
                    if (showTagline) {
                        Text(
                            text = stringResource(Res.string.core_designsystem_brand_tag),
                            color = accentColor,
                            style = taglineStyle,
                            maxLines = 1,
                        )
                    }
                }
            }
        },
    ) { measurables, constraints ->
        val lockup = measurables.single().measure(Constraints())
        val widthScale =
            if (constraints.hasBoundedWidth) constraints.maxWidth.toFloat() / lockup.width else 1f
        val heightScale =
            if (constraints.hasBoundedHeight) constraints.maxHeight.toFloat() / lockup.height
            else 1f
        val scale = min(1f, min(widthScale, heightScale))
        val width =
            (lockup.width * scale).roundToInt().coerceIn(constraints.minWidth, constraints.maxWidth)
        val height =
            (lockup.height * scale)
                .roundToInt()
                .coerceIn(constraints.minHeight, constraints.maxHeight)
        layout(width, height) {
            lockup.placeWithLayer(0, 0) {
                scaleX = scale
                scaleY = scale
                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0f)
            }
        }
    }
}

@NavigoThemePreview
@Composable
private fun NavigoBrandPreview() {
    NavigoPreview { NavigoBrand() }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoBrandMaterialKolorPreview() {
    NavigoMaterialKolorPreview { NavigoBrand() }
}
