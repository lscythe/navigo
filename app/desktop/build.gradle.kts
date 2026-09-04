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

val operatingSystem = System.getProperty("os.name").lowercase()
val architecture = System.getProperty("os.arch").lowercase()
val isArm64 = architecture == "aarch64" || architecture == "arm64"

val maplibreRuntime =
    when {
        operatingSystem.contains("mac") && isArm64 ->
            libs.maplibre.compose.metal.runtime.macos.arm64
        operatingSystem.contains("linux") && isArm64 ->
            libs.maplibre.compose.vulkan.runtime.linux.arm64
        operatingSystem.contains("linux") -> libs.maplibre.compose.vulkan.runtime.linux.x64
        operatingSystem.contains("windows") && isArm64 ->
            libs.maplibre.compose.vulkan.runtime.windows.arm64
        operatingSystem.contains("windows") -> libs.maplibre.compose.vulkan.runtime.windows.x64
        else -> error("Unsupported Desktop host: ${System.getProperty("os.name")} $architecture")
    }

dependencies {
    implementation(project(":app:shared"))
    implementation(libs.compose.multiplatform.desktop.jvm)
    implementation(project(":core:analytics-local"))
    implementation(libs.maplibre.compose)
    runtimeOnly(maplibreRuntime)
}

compose.desktop {
    application {
        mainClass = "dev.lscythe.app.navigo.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)
            packageName = "Navigo"
            packageVersion = libs.versions.appVersionName.get()
        }
    }
}
