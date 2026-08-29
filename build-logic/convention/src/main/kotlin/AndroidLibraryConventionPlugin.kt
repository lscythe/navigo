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
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import dev.lscythe.app.navigo.convention.androidTestImplementation
import dev.lscythe.app.navigo.convention.configureFlavors
import dev.lscythe.app.navigo.convention.configureKotlinAndroid
import dev.lscythe.app.navigo.convention.configureKoverAndroid
import dev.lscythe.app.navigo.convention.configurePrintApksTask
import dev.lscythe.app.navigo.convention.disableUnnecessaryAndroidTests
import dev.lscythe.app.navigo.convention.implementation
import dev.lscythe.app.navigo.convention.libs
import dev.lscythe.app.navigo.convention.registerFlavorConfigurations
import dev.lscythe.app.navigo.convention.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

abstract class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "navigo.android.lint")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                testOptions.targetSdk = 36
                lint.targetSdk = 36
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                configureFlavors(this)

                configureKoverAndroid(this)

                resourcePrefix =
                    path
                        .split("""\W""".toRegex())
                        .drop(1)
                        .distinct()
                        .joinToString(separator = "_")
                        .lowercase() + "_"
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                configurePrintApksTask(this)
                disableUnnecessaryAndroidTests(target)
                target.registerFlavorConfigurations(this)
            }
            dependencies {
                testImplementation(kotlin("test"))
                testImplementation(libs.kotest.runner.junit5)
                testImplementation(libs.kotest.assertions.core)
                testImplementation(libs.kotest.property)

                implementation(libs.androidx.tracing.ktx)
            }
            if (projectDir.resolve("src/androidTest").exists()) {
                dependencies { androidTestImplementation(kotlin("test")) }
            }
        }
}
