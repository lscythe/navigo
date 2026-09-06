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
    alias(libs.plugins.navigo.multiplatform.feature.impl)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "dev.lscythe.app.navigo.feature.onboarding.impl"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":feature:onboarding:api"))
            implementation(project(":feature:home:api"))
            implementation(project(":core:resources"))
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.compose.multiplatform.resources)
        }
        commonTest.dependencies {
            implementation(project(":core:testing-screenshot"))
        }
    }
}

compose.resources {
    packageOfResClass = "dev.lscythe.app.navigo.feature.onboarding.impl.generated.resources"
    publicResClass = false
}
