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
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "NavigoApp"
            isStatic = true
            linkerOpts(
                "-lc++",
                "-lz",
                "-framework",
                "CoreFoundation",
                "-framework",
                "CoreGraphics",
                "-framework",
                "CoreText",
                "-framework",
                "Foundation",
                "-framework",
                "ImageIO",
                "-framework",
                "Metal",
                "-framework",
                "QuartzCore",
            )
        }
    }

    sourceSets.iosMain.dependencies {
        implementation(project(":app:shared"))
        implementation(project(":core:analytics-local"))
        implementation(libs.compose.multiplatform.runtime)
        implementation(libs.compose.multiplatform.ui)
    }
}

extensions.configure<KotlinMultiplatformExtension> {
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}
