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
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import dev.lscythe.app.navigo.convention.COMPILE_SDK
import dev.lscythe.app.navigo.convention.JVM_TARGET
import dev.lscythe.app.navigo.convention.MIN_SDK
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

abstract class KotlinMultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.multiplatform")
            apply(plugin = "com.android.kotlin.multiplatform.library")

            extensions.configure<KotlinMultiplatformExtension> {
                applyDefaultHierarchyTemplate()

                targets.named(
                    "android",
                    KotlinMultiplatformAndroidLibraryTarget::class.java,
                ) {
                    compileSdk = COMPILE_SDK
                    minSdk = MIN_SDK
                    compilerOptions {
                        jvmTarget = JVM_TARGET
                    }
                }
                jvm("desktop") {
                    compilerOptions {
                        jvmTarget = JVM_TARGET
                    }
                }
                iosArm64()
                iosSimulatorArm64()

                sourceSets.commonTest.dependencies {
                    implementation(kotlin("test"))
                }
            }
        }
}
