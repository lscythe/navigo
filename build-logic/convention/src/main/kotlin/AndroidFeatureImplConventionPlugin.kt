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
import com.android.build.api.dsl.LibraryExtension
import dev.lscythe.app.navigo.convention.androidTestImplementation
import dev.lscythe.app.navigo.convention.implementation
import dev.lscythe.app.navigo.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "navigo.android.library")
            apply(plugin = "navigo.metro")

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
            }

            dependencies {
                implementation(project(":core:ui"))
                implementation(project(":core:designsystem"))

                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.androidx.lifecycle.viewModelCompose)
                implementation(libs.metro.viewmodel)
                implementation(libs.metro.viewmodel.compose)
                implementation(libs.androidx.navigation3.runtime)
                implementation(libs.androidx.tracing.ktx)

                androidTestImplementation(libs.androidx.lifecycle.runtimeTesting)
            }
        }
}
