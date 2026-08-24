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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val NavigoLogo: ImageVector
    get() = navigoLogo(pinColor = Color(0xFF0B3D33), arrowColor = Color(0xFFA3E635))

internal fun navigoLogo(pinColor: Color, arrowColor: Color): ImageVector =
    ImageVector.Builder(
            name = "Navigo-${pinColor.value}-${arrowColor.value}",
            defaultWidth = 642.dp,
            defaultHeight = 642.dp,
            viewportWidth = 642f,
            viewportHeight = 642f,
        )
        .apply {
            path(
                fill = SolidColor(pinColor),
                stroke = SolidColor(pinColor),
                strokeLineWidth = 24f,
            ) {
                moveTo(358.8f, 577.67f)
                lineTo(321f, 615.46f)
                lineTo(150.76f, 445.23f)
                curveTo(105.64f, 400.1f, 80.25f, 338.82f, 80.25f, 274.99f)
                curveTo(80.25f, 142.93f, 188.94f, 34.24f, 321f, 34.24f)
                curveTo(453.06f, 34.24f, 561.75f, 142.93f, 561.75f, 274.99f)
                curveTo(561.75f, 338.82f, 536.36f, 400.1f, 491.24f, 445.23f)
                lineTo(396.46f, 540f)
                lineTo(358.64f, 502.18f)
                lineTo(453.41f, 407.4f)
                curveTo(488.51f, 372.31f, 508.25f, 324.64f, 508.25f, 275.02f)
                curveTo(508.25f, 172.27f, 423.72f, 87.77f, 321f, 87.77f)
                curveTo(218.28f, 87.77f, 133.75f, 172.27f, 133.75f, 275.02f)
                curveTo(133.75f, 324.64f, 153.49f, 372.31f, 188.59f, 407.4f)
                lineTo(312.44f, 531.31f)
                lineTo(358.8f, 577.67f)
                close()
            }
            path(fill = SolidColor(arrowColor)) {
                moveTo(390.26f, 203.01f)
                curveTo(398.79f, 199.95f, 407.03f, 208.18f, 403.96f, 216.71f)
                lineTo(350.1f, 366.3f)
                curveTo(346.76f, 375.59f, 333.68f, 375.78f, 330.08f, 366.57f)
                lineTo(304.85f, 302.12f)
                lineTo(240.4f, 276.9f)
                curveTo(231.2f, 273.3f, 231.38f, 260.21f, 240.67f, 256.87f)
                lineTo(390.26f, 203.01f)
                close()
            }
        }
        .build()
