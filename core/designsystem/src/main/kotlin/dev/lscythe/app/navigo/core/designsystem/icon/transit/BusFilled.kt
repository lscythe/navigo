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

val NavigoIcons.BusFilled: ImageVector
    get() {
        if (_RoentgenBus != null) {
            return _RoentgenBus!!
        }
        _RoentgenBus =
            ImageVector.Builder(
                    name = "BusFilled",
                    defaultWidth = 16.dp,
                    defaultHeight = 16.dp,
                    viewportWidth = 16f,
                    viewportHeight = 16f,
                )
                .apply {
                    path(fill = SolidColor(Color.Black)) {
                        moveTo(4f, 2f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -1f,
                            1f,
                        )
                        curveToRelative(-0.585f, 0f, -1.111f, 0.154f, -1.479f, 0.521f)
                        curveTo(1.154f, 3.889f, 1f, 4.416f, 1f, 4.999f)
                        lineTo(1f, 5.5f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            1f,
                            0f,
                        )
                        verticalLineToRelative(-0.499f)
                        curveToRelative(0f, -0.418f, 0.097f, -0.64f, 0.229f, -0.772f)
                        curveTo(2.361f, 4.096f, 2.583f, 4f, 3f, 4f)
                        verticalLineToRelative(9.5f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0.5f,
                            0.5f,
                        )
                        horizontalLineToRelative(1f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0.5f,
                            -0.5f,
                        )
                        lineTo(5f, 13f)
                        horizontalLineToRelative(6f)
                        verticalLineToRelative(0.5f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0.5f,
                            0.5f,
                        )
                        horizontalLineToRelative(1f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            0.5f,
                            -0.5f,
                        )
                        lineTo(13f, 4f)
                        curveToRelative(0.417f, 0f, 0.639f, 0.096f, 0.771f, 0.229f)
                        curveToRelative(0.132f, 0.132f, 0.229f, 0.354f, 0.229f, 0.77f)
                        lineTo(14f, 5.5f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            1f,
                            0f,
                        )
                        verticalLineToRelative(-0.499f)
                        curveToRelative(0f, -0.585f, -0.154f, -1.112f, -0.521f, -1.48f)
                        curveToRelative(-0.368f, -0.367f, -0.894f, -0.521f, -1.476f, -0.521f)
                        curveTo(13f, 2.448f, 12.552f, 2f, 12f, 2f)
                        close()
                        moveTo(6.499f, 3f)
                        lineTo(9.5f, 3f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            0.001f,
                            1f,
                        )
                        lineTo(6.5f, 4f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            -0.001f,
                            -1f,
                        )
                        moveTo(4f, 6f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            1f,
                            -1f,
                        )
                        horizontalLineToRelative(6f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            1f,
                            1f,
                        )
                        verticalLineToRelative(2.5f)
                        arcToRelative(
                            1.5f,
                            1.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            -1.5f,
                            1.5f,
                        )
                        horizontalLineToRelative(-5f)
                        arcTo(
                            1.5f,
                            1.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            4f,
                            8.5f,
                        )
                        close()
                        moveTo(4f, 11.5f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            0.5f,
                            -0.5f,
                        )
                        horizontalLineToRelative(1f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            0f,
                            1f,
                        )
                        horizontalLineToRelative(-1f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            -0.5f,
                            -0.5f,
                        )
                        moveToRelative(6f, 0f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            0.5f,
                            -0.5f,
                        )
                        horizontalLineToRelative(1f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            0f,
                            1f,
                        )
                        horizontalLineToRelative(-1f)
                        arcToRelative(
                            0.5f,
                            0.5f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            -0.5f,
                            -0.5f,
                        )
                    }
                }
                .build()

        return _RoentgenBus!!
    }

@Suppress("ObjectPropertyName") private var _RoentgenBus: ImageVector? = null
