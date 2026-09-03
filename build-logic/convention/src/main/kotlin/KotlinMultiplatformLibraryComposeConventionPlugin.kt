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
import dev.lscythe.app.navigo.convention.configureComposeCompiler
import dev.lscythe.app.navigo.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "org.jetbrains.compose")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            configureComposeCompiler()

            tasks.configureEach {
                if (name == "copyAndroidMainComposeResourcesToAndroidAssets") {
                    val outputDirectory =
                        javaClass.methods
                            .single { method ->
                                method.name == "getOutputDirectory" && method.parameterCount == 0
                            }
                            .invoke(this) as DirectoryProperty
                    outputDirectory.convention(
                        layout.buildDirectory.dir("generated/compose/androidMain/assets")
                    )
                }
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.androidMain.dependencies {
                    implementation(libs.androidx.compose.ui.tooling.preview)
                }
            }
        }
}
