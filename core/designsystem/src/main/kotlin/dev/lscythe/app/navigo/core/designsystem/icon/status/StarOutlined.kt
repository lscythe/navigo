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

val NavigoIcons.StarOutlined: ImageVector
    get() {
        if (_starOutlined != null) {
            return _starOutlined!!
        }
        _starOutlined =
            ImageVector.Builder(
                    name = "StarOutlined",
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
                        moveTo(12f, 17.75f)
                        lineToRelative(-6.172f, 3.245f)
                        lineToRelative(1.179f, -6.873f)
                        lineToRelative(-5f, -4.867f)
                        lineToRelative(6.9f, -1f)
                        lineToRelative(3.086f, -6.253f)
                        lineToRelative(3.086f, 6.253f)
                        lineToRelative(6.9f, 1f)
                        lineToRelative(-5f, 4.867f)
                        lineToRelative(1.179f, 6.873f)
                        lineToRelative(-6.158f, -3.245f)
                    }
                }
                .build()

        return _starOutlined!!
    }

private var _starOutlined: ImageVector? = null
