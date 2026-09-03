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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.icon.NavigoIcons

val NavigoIcons.StarFilled: ImageVector
    get() {
        if (_starFilled != null) {
            return _starFilled!!
        }
        _starFilled =
            ImageVector.Builder(
                    name = "StarFilled",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 24f,
                    viewportHeight = 24f,
                )
                .apply {
                    path(fill = SolidColor(Color.Black)) {
                        moveTo(8.243f, 7.34f)
                        lineToRelative(-6.38f, 0.925f)
                        lineToRelative(-0.113f, 0.023f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.44f,
                            1.684f,
                        )
                        lineToRelative(4.622f, 4.499f)
                        lineToRelative(-1.09f, 6.355f)
                        lineToRelative(-0.013f, 0.11f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            1.464f,
                            0.944f,
                        )
                        lineToRelative(5.706f, -3f)
                        lineToRelative(5.693f, 3f)
                        lineToRelative(0.1f, 0.046f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            1.352f,
                            -1.1f,
                        )
                        lineToRelative(-1.091f, -6.355f)
                        lineToRelative(4.624f, -4.5f)
                        lineToRelative(0.078f, -0.085f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -0.633f,
                            -1.62f,
                        )
                        lineToRelative(-6.38f, -0.926f)
                        lineToRelative(-2.852f, -5.78f)
                        arcToRelative(
                            1f,
                            1f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -1.794f,
                            0f,
                        )
                        lineToRelative(-2.853f, 5.78f)
                        close()
                    }
                }
                .build()

        return _starFilled!!
    }

private var _starFilled: ImageVector? = null
