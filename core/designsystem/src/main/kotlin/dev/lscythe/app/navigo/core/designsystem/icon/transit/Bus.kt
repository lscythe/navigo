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
package dev.lscythe.app.navigo.core.designsystem.icon.transit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons

val NavigoIcons.Bus: ImageVector
    get() {
        if (_bus != null) {
            return _bus!!
        }
        _bus =
            ImageVector.Builder(
                    name = "Bus",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 24f,
                    viewportHeight = 24f,
                )
                .apply {
                    path(fill = SolidColor(Color.Black)) {
                        moveTo(8.5f, 17f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            1f,
                            -1f,
                        )
                        arcToRelative(
                            1.4f,
                            1.4f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0f,
                            -0.2f,
                        )
                        arcToRelative(
                            0.6f,
                            0.6f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.06f,
                            -0.18f,
                        )
                        arcToRelative(
                            0.8f,
                            0.8f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.09f,
                            -0.18f,
                        )
                        lineToRelative(-0.12f, -0.15f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.33f,
                            -0.21f,
                        )
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.6f,
                            -0.08f,
                        )
                        lineToRelative(-0.18f, 0.06f)
                        lineToRelative(-0.18f, 0.09f)
                        arcToRelative(
                            2f,
                            2f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.15f,
                            0.12f,
                        )
                        lineToRelative(-0.12f, 0.15f)
                        arcToRelative(
                            0.8f,
                            0.8f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.09f,
                            0.18f,
                        )
                        arcToRelative(
                            0.6f,
                            0.6f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.06f,
                            0.18f,
                        )
                        arcToRelative(
                            1.4f,
                            1.4f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0f,
                            0.2f,
                        )
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            1f,
                            1f,
                        )
                        close()
                        moveTo(16.5f, 17f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            1f,
                            -1f,
                        )
                        arcToRelative(
                            1.4f,
                            1.4f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0f,
                            -0.2f,
                        )
                        arcToRelative(
                            0.6f,
                            0.6f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.06f,
                            -0.18f,
                        )
                        arcToRelative(
                            0.8f,
                            0.8f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.09f,
                            -0.18f,
                        )
                        lineToRelative(-0.12f, -0.15f)
                        arcToRelative(
                            1.2f,
                            1.2f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.33f,
                            -0.21f,
                        )
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.76f,
                            0f,
                        )
                        arcToRelative(
                            1.2f,
                            1.2f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.33f,
                            0.21f,
                        )
                        lineToRelative(-0.12f, 0.15f)
                        arcToRelative(
                            0.8f,
                            0.8f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.09f,
                            0.18f,
                        )
                        arcToRelative(
                            0.6f,
                            0.6f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.06f,
                            0.18f,
                        )
                        arcToRelative(
                            1.4f,
                            1.4f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0f,
                            0.2f,
                        )
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0.29f,
                            0.7f,
                        )
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0.67f,
                            0.3f,
                        )
                        moveToRelative(-3f, -12f)
                        horizontalLineToRelative(-2f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0f,
                            2f,
                        )
                        horizontalLineToRelative(2f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0f,
                            -2f,
                        )
                        moveToRelative(5f, -3f)
                        horizontalLineToRelative(-12f)
                        arcToRelative(
                            3f,
                            3f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -3f,
                            3f,
                        )
                        verticalLineToRelative(12f)
                        arcToRelative(
                            3f,
                            3f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            2f,
                            2.82f,
                        )
                        lineTo(5.5f, 21f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            2f,
                            0f,
                        )
                        verticalLineToRelative(-1f)
                        horizontalLineToRelative(10f)
                        verticalLineToRelative(1f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            2f,
                            0f,
                        )
                        verticalLineToRelative(-1.18f)
                        arcToRelative(
                            3f,
                            3f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            2f,
                            -2.82f,
                        )
                        lineTo(21.5f, 5f)
                        arcToRelative(
                            3f,
                            3f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -3f,
                            -3f,
                        )
                        moveToRelative(1f, 15f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            -1f,
                            1f,
                        )
                        horizontalLineToRelative(-12f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            -1f,
                            -1f,
                        )
                        verticalLineToRelative(-3f)
                        horizontalLineToRelative(14f)
                        close()
                        moveTo(19.5f, 12f)
                        horizontalLineToRelative(-14f)
                        lineTo(5.5f, 5f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            1f,
                            -1f,
                        )
                        horizontalLineToRelative(12f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            1f,
                            1f,
                        )
                        close()
                    }
                }
                .build()

        return _bus!!
    }

private var _bus: ImageVector? = null
