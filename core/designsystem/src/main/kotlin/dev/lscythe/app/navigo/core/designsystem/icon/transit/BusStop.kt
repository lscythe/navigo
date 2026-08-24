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

val NavigoIcons.BusStop: ImageVector
    get() {
        if (_RoentgenBusStopSign != null) {
            return _RoentgenBusStopSign!!
        }
        _RoentgenBusStopSign =
            ImageVector.Builder(
                    name = "BusStop",
                    defaultWidth = 16.dp,
                    defaultHeight = 16.dp,
                    viewportWidth = 16f,
                    viewportHeight = 16f,
                )
                .apply {
                    path(fill = SolidColor(Color.Black)) {
                        moveTo(1.5f, 3f)
                        curveToRelative(-0.277f, 0f, -0.5f, 0.223f, -0.5f, 0.5f)
                        verticalLineToRelative(5f)
                        curveToRelative(0f, 0.277f, 0.237f, 0.588f, 0.5f, 0.5f)
                        lineTo(3f, 9f)
                        verticalLineToRelative(5.5f)
                        arcToRelative(
                            0.499f,
                            0.499f,
                            0f,
                            isMoreThanHalf = true,
                            isPositiveArc = false,
                            1f,
                            0f,
                        )
                        lineTo(4f, 9f)
                        horizontalLineToRelative(1.5f)
                        curveToRelative(0.277f, 0f, 0.5f, -0.223f, 0.5f, -0.5f)
                        verticalLineToRelative(-5f)
                        curveToRelative(0f, -0.277f, -0.223f, -0.5f, -0.5f, -0.5f)
                        close()
                        moveTo(2.5f, 4f)
                        horizontalLineToRelative(2f)
                        curveToRelative(0.277f, 0f, 0.5f, 0.223f, 0.5f, 0.5f)
                        verticalLineToRelative(1f)
                        curveToRelative(0f, 0.277f, -0.223f, 0.5f, -0.5f, 0.5f)
                        horizontalLineToRelative(-2f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            -0.5f,
                            -0.5f,
                        )
                        verticalLineToRelative(-1f)
                        curveToRelative(0f, -0.277f, 0.223f, -0.5f, 0.5f, -0.5f)
                    }
                }
                .build()

        return _RoentgenBusStopSign!!
    }

@Suppress("ObjectPropertyName") private var _RoentgenBusStopSign: ImageVector? = null
