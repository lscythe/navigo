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

val NavigoIcons.Signal: SignalIcons
    get() = SignalIcons

object SignalIcons {
    val None: ImageVector
        get() {
            if (_SignalNone != null) {
                return _SignalNone!!
            }
            _SignalNone =
                ImageVector.Builder(
                        name = "SignalNone",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    )
                    .apply {
                        path(
                            fill = SolidColor(Color(0xFF666666)),
                            stroke = SolidColor(Color(0xFF666666)),
                            fillAlpha = 0.5f,
                            strokeAlpha = 0f,
                            strokeLineWidth = 1.14f,
                        ) {
                            moveTo(6.282f, 18.847f)
                            lineToRelative(0f, -3.427f)
                            curveToRelative(0f, -0.944f, 0.766f, -1.71f, 1.71f, -1.71f)
                            curveToRelative(0.944f, 0f, 1.71f, 0.766f, 1.71f, 1.71f)
                            lineToRelative(0f, 3.427f)
                            curveToRelative(0f, 0.944f, -0.766f, 1.71f, -1.71f, 1.71f)
                            curveToRelative(-0.944f, 0f, -1.71f, -0.766f, -1.71f, -1.71f)
                            moveToRelative(4.569f, 0f)
                            lineToRelative(0f, -6.853f)
                            curveToRelative(0f, -0.944f, 0.766f, -1.71f, 1.71f, -1.71f)
                            curveToRelative(0.944f, 0f, 1.71f, 0.766f, 1.71f, 1.71f)
                            lineToRelative(0f, 6.853f)
                            curveToRelative(0f, 0.944f, -0.766f, 1.71f, -1.71f, 1.71f)
                            curveToRelative(-0.944f, 0f, -1.71f, -0.766f, -1.71f, -1.71f)
                            moveToRelative(4.569f, 0f)
                            lineToRelative(0f, -10.28f)
                            curveToRelative(0f, -0.944f, 0.766f, -1.71f, 1.71f, -1.71f)
                            curveToRelative(0.944f, 0f, 1.71f, 0.766f, 1.71f, 1.71f)
                            lineToRelative(0f, 10.28f)
                            curveToRelative(0f, 0.944f, -0.766f, 1.71f, -1.71f, 1.71f)
                            curveToRelative(-0.944f, 0f, -1.71f, -0.766f, -1.71f, -1.71f)
                        }
                    }
                    .build()

            return _SignalNone!!
        }

    private var _SignalNone: ImageVector? = null

    val Strong: ImageVector
        get() {
            if (_SignalStrong != null) {
                return _SignalStrong!!
            }
            _SignalStrong =
                ImageVector.Builder(
                        name = "SignalStrong",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    )
                    .apply {
                        path(
                            fill = SolidColor(Color.Black),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 1.14f,
                        ) {
                            moveTo(6.282f, 18.847f)
                            lineToRelative(0f, -3.427f)
                            curveToRelative(0f, -0.627f, 0.516f, -1.142f, 1.142f, -1.142f)
                            curveToRelative(0.627f, 0f, 1.142f, 0.516f, 1.142f, 1.142f)
                            lineToRelative(0f, 3.427f)
                            curveToRelative(0f, 0.627f, -0.516f, 1.142f, -1.142f, 1.142f)
                            curveToRelative(-0.627f, 0f, -1.142f, -0.516f, -1.142f, -1.142f)
                            moveToRelative(4.569f, 0f)
                            lineToRelative(0f, -6.853f)
                            curveToRelative(0f, -0.627f, 0.516f, -1.142f, 1.142f, -1.142f)
                            curveToRelative(0.627f, 0f, 1.142f, 0.516f, 1.142f, 1.142f)
                            lineToRelative(0f, 6.853f)
                            curveToRelative(0f, 0.627f, -0.516f, 1.142f, -1.142f, 1.142f)
                            curveToRelative(-0.627f, 0f, -1.142f, -0.516f, -1.142f, -1.142f)
                            moveToRelative(4.569f, 0f)
                            lineToRelative(0f, -10.28f)
                            curveToRelative(0f, -0.627f, 0.516f, -1.142f, 1.142f, -1.142f)
                            curveToRelative(0.627f, 0f, 1.142f, 0.516f, 1.142f, 1.142f)
                            lineToRelative(0f, 10.28f)
                            curveToRelative(0f, 0.627f, -0.516f, 1.142f, -1.142f, 1.142f)
                            curveToRelative(-0.627f, 0f, -1.142f, -0.516f, -1.142f, -1.142f)
                        }
                    }
                    .build()

            return _SignalStrong!!
        }

    private var _SignalStrong: ImageVector? = null

    val Medium: ImageVector
        get() {
            if (_SignalMedium != null) {
                return _SignalMedium!!
            }
            _SignalMedium =
                ImageVector.Builder(
                        name = "SignalMedium",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    )
                    .apply {
                        path(
                            fill = SolidColor(Color(0xFF666666)),
                            stroke = SolidColor(Color(0xFF666666)),
                            strokeLineWidth = 1.14f,
                            fillAlpha = 0.5f,
                            strokeAlpha = 0f,
                        ) {
                            moveTo(6.282f, 18.847f)
                            lineToRelative(0f, -3.427f)
                            curveToRelative(0f, -0.944f, 0.766f, -1.71f, 1.71f, -1.71f)
                            curveToRelative(0.944f, 0f, 1.71f, 0.766f, 1.71f, 1.71f)
                            lineToRelative(0f, 3.427f)
                            curveToRelative(0f, 0.944f, -0.766f, 1.71f, -1.71f, 1.71f)
                            curveToRelative(-0.944f, 0f, -1.71f, -0.766f, -1.71f, -1.71f)
                            moveToRelative(4.569f, 0f)
                            lineToRelative(0f, -6.853f)
                            curveToRelative(0f, -0.944f, 0.766f, -1.71f, 1.71f, -1.71f)
                            curveToRelative(0.944f, 0f, 1.71f, 0.766f, 1.71f, 1.71f)
                            lineToRelative(0f, 6.853f)
                            curveToRelative(0f, 0.944f, -0.766f, 1.71f, -1.71f, 1.71f)
                            curveToRelative(-0.944f, 0f, -1.71f, -0.766f, -1.71f, -1.71f)
                            moveToRelative(4.569f, 0f)
                            lineToRelative(0f, -10.28f)
                            curveToRelative(0f, -0.944f, 0.766f, -1.71f, 1.71f, -1.71f)
                            curveToRelative(0.944f, 0f, 1.71f, 0.766f, 1.71f, 1.71f)
                            lineToRelative(0f, 10.28f)
                            curveToRelative(0f, 0.944f, -0.766f, 1.71f, -1.71f, 1.71f)
                            curveToRelative(-0.944f, 0f, -1.71f, -0.766f, -1.71f, -1.71f)
                        }
                        path(
                            fill = SolidColor(Color.Black),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 1.14f,
                        ) {
                            moveTo(6.282f, 18.847f)
                            lineToRelative(0f, -3.427f)
                            curveToRelative(0f, -0.627f, 0.516f, -1.142f, 1.142f, -1.142f)
                            curveToRelative(0.627f, 0f, 1.142f, 0.516f, 1.142f, 1.142f)
                            lineToRelative(0f, 3.427f)
                            curveToRelative(0f, 0.627f, -0.516f, 1.142f, -1.142f, 1.142f)
                            curveToRelative(-0.627f, 0f, -1.142f, -0.516f, -1.142f, -1.142f)
                            moveToRelative(4.569f, 0f)
                            lineToRelative(0f, -6.853f)
                            curveToRelative(0f, -0.627f, 0.516f, -1.142f, 1.142f, -1.142f)
                            curveToRelative(0.627f, 0f, 1.142f, 0.516f, 1.142f, 1.142f)
                            lineToRelative(0f, 6.853f)
                            curveToRelative(0f, 0.627f, -0.516f, 1.142f, -1.142f, 1.142f)
                            curveToRelative(-0.627f, 0f, -1.142f, -0.516f, -1.142f, -1.142f)
                        }
                    }
                    .build()

            return _SignalMedium!!
        }

    private var _SignalMedium: ImageVector? = null

    val Weak: ImageVector
        get() {
            if (_SignalWeak != null) {
                return _SignalWeak!!
            }
            _SignalWeak =
                ImageVector.Builder(
                        name = "SignalWeak",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    )
                    .apply {
                        path(
                            fill = SolidColor(Color(0xFF666666)),
                            stroke = SolidColor(Color(0xFF666666)),
                            strokeLineWidth = 1.14f,
                            fillAlpha = 0.5f,
                            strokeAlpha = 0f,
                        ) {
                            moveTo(6.282f, 18.847f)
                            lineToRelative(0f, -3.427f)
                            curveToRelative(0f, -0.944f, 0.766f, -1.71f, 1.71f, -1.71f)
                            curveToRelative(0.944f, 0f, 1.71f, 0.766f, 1.71f, 1.71f)
                            lineToRelative(0f, 3.427f)
                            curveToRelative(0f, 0.944f, -0.766f, 1.71f, -1.71f, 1.71f)
                            curveToRelative(-0.944f, 0f, -1.71f, -0.766f, -1.71f, -1.71f)
                            moveToRelative(4.569f, 0f)
                            lineToRelative(0f, -6.853f)
                            curveToRelative(0f, -0.944f, 0.766f, -1.71f, 1.71f, -1.71f)
                            curveToRelative(0.944f, 0f, 1.71f, 0.766f, 1.71f, 1.71f)
                            lineToRelative(0f, 6.853f)
                            curveToRelative(0f, 0.944f, -0.766f, 1.71f, -1.71f, 1.71f)
                            curveToRelative(-0.944f, 0f, -1.71f, -0.766f, -1.71f, -1.71f)
                            moveToRelative(4.569f, 0f)
                            lineToRelative(0f, -10.28f)
                            curveToRelative(0f, -0.944f, 0.766f, -1.71f, 1.71f, -1.71f)
                            curveToRelative(0.944f, 0f, 1.71f, 0.766f, 1.71f, 1.71f)
                            lineToRelative(0f, 10.28f)
                            curveToRelative(0f, 0.944f, -0.766f, 1.71f, -1.71f, 1.71f)
                            curveToRelative(-0.944f, 0f, -1.71f, -0.766f, -1.71f, -1.71f)
                        }
                        path(
                            fill = SolidColor(Color.Black),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 1.14f,
                        ) {
                            moveTo(6.282f, 18.847f)
                            lineToRelative(0f, -3.427f)
                            curveToRelative(0f, -0.627f, 0.516f, -1.142f, 1.142f, -1.142f)
                            curveToRelative(0.627f, 0f, 1.142f, 0.516f, 1.142f, 1.142f)
                            lineToRelative(0f, 3.427f)
                            curveToRelative(0f, 0.627f, -0.516f, 1.142f, -1.142f, 1.142f)
                            curveToRelative(-0.627f, 0f, -1.142f, -0.516f, -1.142f, -1.142f)
                        }
                    }
                    .build()

            return _SignalWeak!!
        }

    private var _SignalWeak: ImageVector? = null
}
