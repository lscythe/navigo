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
package dev.lscythe.app.navigo.core.designsystem.icon.status

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons

val NavigoIcons.SocketConnected: ImageVector
    get() {
        if (_SocketConnected != null) {
            return _SocketConnected!!
        }
        _SocketConnected =
            ImageVector.Builder(
                    name = "SocketConnected",
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
                        moveToRelative(7f, 12f)
                        lineToRelative(5f, 5f)
                        lineToRelative(-1.5f, 1.5f)
                        arcToRelative(
                            3.536f,
                            3.536f,
                            0f,
                            isMoreThanHalf = true,
                            isPositiveArc = true,
                            -5f,
                            -5f,
                        )
                        close()
                        moveTo(17f, 12f)
                        lineToRelative(-5f, -5f)
                        lineToRelative(1.5f, -1.5f)
                        arcToRelative(
                            3.536f,
                            3.536f,
                            0f,
                            isMoreThanHalf = true,
                            isPositiveArc = true,
                            5f,
                            5f,
                        )
                        close()
                        moveTo(3f, 21f)
                        lineToRelative(2.5f, -2.5f)
                        moveToRelative(13f, -13f)
                        lineTo(21f, 3f)
                        moveToRelative(-11f, 8f)
                        lineToRelative(-2f, 2f)
                        moveToRelative(5f, 1f)
                        lineToRelative(-2f, 2f)
                    }
                }
                .build()

        return _SocketConnected!!
    }

@Suppress("ObjectPropertyName") private var _SocketConnected: ImageVector? = null
