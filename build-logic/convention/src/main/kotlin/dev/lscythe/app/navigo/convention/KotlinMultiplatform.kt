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
package dev.lscythe.app.navigo.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension) {
    commonExtension.apply {
        compileSdk = COMPILE_SDK

        defaultConfig.apply {
            minSdk = MIN_SDK
        }

        compileOptions.apply {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }

        testOptions.unitTests.all {
            it.useJUnitPlatform()
        }
    }

    configureKotlin<KotlinAndroidProjectExtension>()
}

internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JAVA_VERSION
        targetCompatibility = JAVA_VERSION
    }
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        jvmArgs("--enable-native-access=ALL-UNNAMED")
    }

    configureKotlin<KotlinJvmProjectExtension>()
}

internal fun Project.configureMultiplatformLibrary() {
    extensions.configure<KotlinMultiplatformExtension> {
        applyDefaultHierarchyTemplate()

        jvm("desktop") {
            compilerOptions {
                jvmTarget = JVM_TARGET
            }
        }
        iosArm64()
        iosSimulatorArm64()

        extensions.configure<KotlinMultiplatformAndroidLibraryExtension> {
            compileSdk = COMPILE_SDK
            minSdk = MIN_SDK
            withHostTest {
                isIncludeAndroidResources = true
            }
        }
    }

    tasks.withType<Test>().configureEach {
        jvmArgs("--enable-native-access=ALL-UNNAMED")
    }

    configureKotlin<KotlinMultiplatformExtension>()
}

private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() =
    configure<T> {
        val warningsAsErrors =
            providers
                .gradleProperty("warningsAsErrors")
                .map {
                    it.toBoolean()
                }
                .orElse(false)
        when (this) {
            is KotlinMultiplatformExtension ->
                compilerOptions.apply {
                    allWarningsAsErrors = warningsAsErrors
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            is KotlinAndroidProjectExtension ->
                compilerOptions.apply {
                    jvmTarget = JVM_TARGET
                    allWarningsAsErrors = warningsAsErrors
                }
            is KotlinJvmProjectExtension ->
                compilerOptions.apply {
                    jvmTarget = JVM_TARGET
                    allWarningsAsErrors = warningsAsErrors
                }
            else -> error("Unsupported project extension $this ${T::class}")
        }
    }
