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
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.navigo.desktop.application)
}

dependencies {
    implementation(project(":app:shared"))
    implementation(libs.compose.multiplatform.desktop.jvm)
    implementation(project(":core:analytics-local"))
    implementation(libs.maplibre.compose)
    runtimeOnly(libs.maplibre.native.ffi.runtime.metal)
}

compose.desktop {
    application {
        mainClass = "dev.lscythe.app.navigo.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg)
            packageName = "Navigo"
            packageVersion = "1.0.0"
        }
    }
}
