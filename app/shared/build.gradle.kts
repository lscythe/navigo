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
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "dev.lscythe.app.navigo.app.shared"
    }

    sourceSets.commonMain.dependencies {
        api(project(":core:analytics"))
        implementation(project(":core:designsystem"))
        implementation(project(":core:navigation"))
        implementation(project(":core:ui"))
        implementation(project(":feature:home:api"))
        implementation(project(":feature:home:impl"))
        implementation(project(":feature:onboarding:api"))
        implementation(project(":feature:onboarding:impl"))
        implementation(libs.androidx.navigation3.runtime)
        implementation(libs.compose.multiplatform.navigation3.ui)
        implementation(libs.compose.multiplatform.material3.adaptive)
        implementation(libs.compose.multiplatform.material3.adaptive.navigation3)
    }

    sourceSets.commonTest.dependencies {
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotlinx.serialization.json)
    }
}
