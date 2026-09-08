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
package dev.lscythe.app.navigo.core.designsystem.component.atom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

@NavigoThemePreview
@Composable
private fun NavigoCircularProgressIndicatorPreview() {
    NavigoPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.container)) {
            NavigoCircularProgressIndicator(progress = 0.25f)
            NavigoCircularProgressIndicator(progress = 0.65f)
            NavigoCircularProgressIndicator(progress = 1f)
        }
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoCircularProgressIndicatorMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        NavigoCircularProgressIndicator(progress = 0.65f)
    }
}

@NavigoThemePreview
@Composable
private fun NavigoCircularLoadingIndicatorPreview() {
    NavigoPreview {
        NavigoCircularLoadingIndicator()
    }
}

@NavigoMaterialKolorThemePreview
@Composable
private fun NavigoCircularLoadingIndicatorMaterialKolorPreview() {
    NavigoMaterialKolorPreview {
        NavigoCircularLoadingIndicator()
    }
}
