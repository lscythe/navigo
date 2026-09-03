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
    alias(libs.plugins.roborazzi)
}

kotlin {
    android {
        namespace = "dev.lscythe.app.navigo.core.designsystem"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.multiplatform.foundation)
            api(libs.compose.multiplatform.material3)
            api(libs.compose.multiplatform.runtime)
            api(libs.compose.multiplatform.ui)
            api(libs.material.kolor)
            api(libs.kotlinx.collections)
            implementation(libs.compose.multiplatform.resources)
        }
        androidMain.dependencies {
            implementation(libs.androidx.compose.material3)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(project(":core:testing-screenshot"))
        }
        desktopTest.dependencies {
            implementation(libs.compose.multiplatform.ui.test)
        }
        appleTest.dependencies {
            implementation(libs.compose.multiplatform.ui.test)
        }
        androidHostTest.dependencies {
            implementation(libs.androidx.compose.ui.test)
            implementation(libs.androidx.compose.ui.testManifest)
            implementation(libs.robolectric)
            implementation(libs.junit.vintage.engine)
        }
    }
}

compose.resources {
    packageOfResClass = "dev.lscythe.app.navigo.core.designsystem.generated.resources"
    publicResClass = false
}
