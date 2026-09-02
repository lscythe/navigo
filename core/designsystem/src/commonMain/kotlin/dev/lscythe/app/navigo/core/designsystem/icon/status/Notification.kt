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

val NavigoIcons.Notification: ImageVector
    get() {
        if (_notification != null) {
            return _notification!!
        }
        _notification =
            ImageVector.Builder(
                    name = "Notification",
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
                        moveTo(10f, 5f)
                        arcToRelative(
                            2f,
                            2f,
                            0f,
                            isMoreThanHalf = true,
                            isPositiveArc = true,
                            4f,
                            0f,
                        )
                        arcToRelative(
                            7f,
                            7f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            4f,
                            6f,
                        )
                        verticalLineToRelative(3f)
                        arcToRelative(
                            4f,
                            4f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            2f,
                            3f,
                        )
                        horizontalLineToRelative(-16f)
                        arcToRelative(
                            4f,
                            4f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            2f,
                            -3f,
                        )
                        verticalLineToRelative(-3f)
                        arcToRelative(
                            7f,
                            7f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            4f,
                            -6f,
                        )
                    }
                    path(
                        stroke = SolidColor(Color.Black),
                        strokeLineWidth = 2f,
                        strokeLineCap = StrokeCap.Round,
                        strokeLineJoin = StrokeJoin.Round,
                    ) {
                        moveTo(9f, 17f)
                        verticalLineToRelative(1f)
                        arcToRelative(
                            3f,
                            3f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            6f,
                            0f,
                        )
                        verticalLineToRelative(-1f)
                    }
                }
                .build()

        return _notification!!
    }

private var _notification: ImageVector? = null
