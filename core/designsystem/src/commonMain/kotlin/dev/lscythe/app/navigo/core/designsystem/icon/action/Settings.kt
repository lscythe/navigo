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
package dev.lscythe.app.navigo.core.designsystem.icon.action

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons

val NavigoIcons.Settings: ImageVector
    get() {
        if (_settings != null) {
            return _settings!!
        }
        _settings =
            ImageVector.Builder(
                    name = "Settings",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 24f,
                    viewportHeight = 24f,
                )
                .apply {
                    path(
                        stroke = SolidColor(Color.Black),
                        strokeLineWidth = 2f,
                        strokeLineCap = StrokeCap.Round,
                        strokeLineJoin = StrokeJoin.Round,
                    ) {
                        moveTo(10.325f, 4.317f)
                        curveToRelative(0.426f, -1.756f, 2.924f, -1.756f, 3.35f, 0f)
                        arcToRelative(
                            1.724f,
                            1.724f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            2.573f,
                            1.066f,
                        )
                        curveToRelative(1.543f, -0.94f, 3.31f, 0.826f, 2.37f, 2.37f)
                        arcToRelative(
                            1.724f,
                            1.724f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            1.065f,
                            2.572f,
                        )
                        curveToRelative(1.756f, 0.426f, 1.756f, 2.924f, 0f, 3.35f)
                        arcToRelative(
                            1.724f,
                            1.724f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -1.066f,
                            2.573f,
                        )
                        curveToRelative(0.94f, 1.543f, -0.826f, 3.31f, -2.37f, 2.37f)
                        arcToRelative(
                            1.724f,
                            1.724f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -2.572f,
                            1.065f,
                        )
                        curveToRelative(-0.426f, 1.756f, -2.924f, 1.756f, -3.35f, 0f)
                        arcToRelative(
                            1.724f,
                            1.724f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -2.573f,
                            -1.066f,
                        )
                        curveToRelative(-1.543f, 0.94f, -3.31f, -0.826f, -2.37f, -2.37f)
                        arcToRelative(
                            1.724f,
                            1.724f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -1.065f,
                            -2.572f,
                        )
                        curveToRelative(-1.756f, -0.426f, -1.756f, -2.924f, 0f, -3.35f)
                        arcToRelative(
                            1.724f,
                            1.724f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            1.066f,
                            -2.573f,
                        )
                        curveToRelative(-0.94f, -1.543f, 0.826f, -3.31f, 2.37f, -2.37f)
                        curveToRelative(1f, 0.608f, 2.296f, 0.07f, 2.572f, -1.065f)
                    }
                    path(
                        stroke = SolidColor(Color.Black),
                        strokeLineWidth = 2f,
                        strokeLineCap = StrokeCap.Round,
                        strokeLineJoin = StrokeJoin.Round,
                    ) {
                        moveTo(9f, 12f)
                        arcToRelative(
                            3f,
                            3f,
                            0f,
                            isMoreThanHalf = true,
                            isPositiveArc = false,
                            6f,
                            0f,
                        )
                        arcToRelative(
                            3f,
                            3f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -6f,
                            0f,
                        )
                    }
                }
                .build()

        return _settings!!
    }

private var _settings: ImageVector? = null
