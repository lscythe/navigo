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
plugins {
    alias(libs.plugins.navigo.multiplatform.library)
    alias(libs.plugins.navigo.multiplatform.library.compose)
}

kotlin {
    android {
        namespace = "dev.lscythe.app.navigo.core.testing.screenshot"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.roborazzi.core)
            api(libs.compose.multiplatform.ui.test)
            api(libs.compose.multiplatform.foundation)
            api(libs.compose.multiplatform.runtime)
            api(libs.compose.multiplatform.ui)
        }
        androidMain.dependencies {
            api(libs.bundles.androidx.compose.ui.test)
            api(libs.roborazzi.accessibility.check)
            api(libs.roborazzi)
            implementation(libs.androidx.activity.compose)
            implementation(libs.robolectric)
        }
        desktopMain.dependencies {
            api(libs.roborazzi.compose.desktop)
        }
        appleMain.dependencies {
            api(libs.roborazzi.compose.ios)
        }
    }
}
