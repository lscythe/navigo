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
import dev.lscythe.app.navigo.convention.configureMultiplatformLibrary
import dev.lscythe.app.navigo.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

abstract class KotlinMultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("io.kotest")
                apply("com.google.devtools.ksp")
            }

            configureMultiplatformLibrary()

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.commonTest.dependencies {
                    implementation(kotlin("test"))
                    implementation(libs.kotest.framework.engine)
                    implementation(libs.kotest.assertions.core)
                    implementation(libs.kotest.property)
                }
                sourceSets.getByName("desktopTest").dependencies {
                    implementation(libs.kotest.runner.junit5)
                }
            }

            tasks.named<Test>("desktopTest") {
                useJUnitPlatform()
            }
        }
    }
}
