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
package dev.lscythe.app.navigo.feature.home.impl.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.ui.rememberOpenWebsite
import dev.lscythe.app.navigo.feature.home.impl.R
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.overlay.AttributionLinks
import org.maplibre.compose.overlay.ExpandingAttributionButton
import org.maplibre.compose.overlay.MapOverlay
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

private val defaultPosition = Position(106.8999865, -6.2054059)
private const val defaultZoom = 12.0
private const val MapAttributionHtml =
    """<a href="https://maplibre.org/">MapLibre</a> | <a href="https://www.maptoolkit.com/copyright/">© Maptoolkit</a> <a href="https://www.openstreetmap.org/copyright">© OpenStreetMap</a>"""

@Composable
internal fun HomeMap() {
    val variant = if (isSystemInDarkTheme()) "dark" else "light"
    val mapStyle = "https://transit-basemap.pages.dev/style-$variant.json"

    val camera =
        rememberCameraState(
            firstPosition = CameraPosition(target = defaultPosition, zoom = defaultZoom)
        )

    MaplibreMap(
        baseStyle = BaseStyle.Uri(mapStyle),
        cameraState = camera,
        options = MapOptions(gestureOptions = GestureOptions(isTwoFingerTiltEnabled = false)),
        overlay =
            MapOverlay {
                val moCamera = cameraState
                val moStyle = styleState
                Row(
                    Modifier.align(Alignment.BottomStart).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MaptoolkitLogo()
                    ExpandingAttributionButton(
                        cameraState = moCamera,
                        styleState = moStyle,
                        expandedContent = { _, textStyle ->
                            AttributionLinks(
                                attributions = listOf(MapAttributionHtml),
                                textStyle = textStyle,
                                breakWithinAttribution = true,
                            )
                        },
                    )
                }
            },
    )
}

@Composable
private fun MaptoolkitLogo(
    modifier: Modifier = Modifier,
    contentDescription: String? =
        stringResource(R.string.feature_home_impl_maptoolkit_logo_description),
    onClick: (() -> Unit)? = rememberOpenWebsite("https://www.maptoolkit.com"),
) {
    Image(
        painter = painterResource(R.drawable.feature_home_impl_img_maptoolkit),
        contentDescription = contentDescription,
        modifier =
            modifier
                .height(24.dp)
                .then(
                    onClick?.let { Modifier.clickable(onClick = it, role = Role.Image) } ?: Modifier
                ),
    )
}
