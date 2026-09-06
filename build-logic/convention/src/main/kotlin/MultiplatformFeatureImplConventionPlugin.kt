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
import dev.lscythe.app.navigo.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformFeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "navigo.multiplatform.library")
            apply(plugin = "navigo.multiplatform.library.compose")
            apply(plugin = "navigo.metro")

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.commonMain.dependencies {
                    implementation(project(":core:ui"))
                    implementation(project(":core:designsystem"))
                    implementation(libs.androidx.lifecycle.runtimeCompose)
                    implementation(libs.androidx.lifecycle.viewModelCompose)
                    implementation(libs.metro.viewmodel)
                    implementation(libs.metro.viewmodel.compose)
                    implementation(libs.androidx.navigation3.runtime)
                }
                sourceSets.androidMain.dependencies {
                    implementation(libs.androidx.tracing.ktx)
                }
                sourceSets.getByName("androidHostTest").dependencies {
                    implementation(libs.robolectric)
                    implementation(libs.junit.vintage.engine)
                    implementation(project(":core:testing-screenshot"))
                }
            }
        }
}
