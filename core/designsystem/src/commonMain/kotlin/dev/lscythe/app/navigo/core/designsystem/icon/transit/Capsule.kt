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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons

val NavigoIcons.Capsule: ImageVector
    get() {
        if (_capsule != null) {
            return _capsule!!
        }
        _capsule =
            ImageVector.Builder(
                    name = "Capsule",
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
                        moveTo(6f, 9f)
                        arcToRelative(
                            6f,
                            6f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            6f,
                            -6f,
                        )
                        arcToRelative(
                            6f,
                            6f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            6f,
                            6f,
                        )
                        verticalLineToRelative(6f)
                        arcToRelative(
                            6f,
                            6f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            -6f,
                            6f,
                        )
                        arcToRelative(
                            6f,
                            6f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            -6f,
                            -6f,
                        )
                        lineToRelative(0f, -6f)
                    }
                }
                .build()

        return _capsule!!
    }

private var _capsule: ImageVector? = null
