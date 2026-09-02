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
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.android.lint)
}

group = "dev.lscythe.app.navigo.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    implementation(files(libs::class.java.superclass.protectionDomain.codeSource.location))
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.multiplatform.library.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    implementation(libs.kotest.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.kover.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    implementation(libs.sqldelight.gradlePlugin)
    implementation(libs.kotest.assertions.core)
    lintChecks(libs.androidx.lint.gradle)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "navigo.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "navigo.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplicationFlavors") {
            id = "navigo.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
        register("multiplatformFeatureApi") {
            id = "navigo.multiplatform.feature.api"
            implementationClass = "KotlinMultiplatformFeatureApiConventionPlugin"
        }
        register("multiplatformFeatureImpl") {
            id = "navigo.multiplatform.feature.impl"
            implementationClass = "MultiplatformFeatureImplConventionPlugin"
        }
        register("androidLibrary") {
            id = "navigo.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "navigo.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLint") {
            id = "navigo.android.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("androidTest") {
            id = "navigo.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("jvmLibrary") {
            id = "navigo.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("multiplatformLibrary") {
            id = "navigo.multiplatform.library"
            implementationClass = "KotlinMultiplatformLibraryConventionPlugin"
        }
        register("multiplatformLibraryCompose") {
            id = "navigo.multiplatform.library.compose"
            implementationClass = "KotlinMultiplatformLibraryComposeConventionPlugin"
        }
        register("metro") {
            id = "navigo.metro"
            implementationClass = "MetroConventionPlugin"
        }
        register("root") {
            id = "navigo.root"
            implementationClass = "RootPlugin"
        }
    }
}
