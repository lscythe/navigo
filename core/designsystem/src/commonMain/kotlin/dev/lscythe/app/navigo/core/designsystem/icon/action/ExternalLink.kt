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

val NavigoIcons.ExternalLink: ImageVector
    get() {
        if (_externalLink != null) {
            return _externalLink!!
        }
        _externalLink =
            ImageVector.Builder(
                    name = "ExternalLink",
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
                        moveTo(12f, 6f)
                        horizontalLineToRelative(-6f)
                        arcToRelative(
                            2f,
                            2f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            -2f,
                            2f,
                        )
                        verticalLineToRelative(10f)
                        arcToRelative(
                            2f,
                            2f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            2f,
                            2f,
                        )
                        horizontalLineToRelative(10f)
                        arcToRelative(
                            2f,
                            2f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = false,
                            2f,
                            -2f,
                        )
                        verticalLineToRelative(-6f)
                    }
                    path(
                        stroke = SolidColor(Color.Black),
                        strokeLineWidth = 2f,
                        strokeLineCap = StrokeCap.Round,
                        strokeLineJoin = StrokeJoin.Round,
                    ) {
                        moveTo(11f, 13f)
                        lineToRelative(9f, -9f)
                    }
                    path(
                        stroke = SolidColor(Color.Black),
                        strokeLineWidth = 2f,
                        strokeLineCap = StrokeCap.Round,
                        strokeLineJoin = StrokeJoin.Round,
                    ) {
                        moveTo(15f, 4f)
                        horizontalLineToRelative(5f)
                        verticalLineToRelative(5f)
                    }
                }
                .build()

        return _externalLink!!
    }

private var _externalLink: ImageVector? = null
