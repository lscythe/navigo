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
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

private val coverageExclusions =
    listOf(
        "*.R",
        "*.R$*",
        "*.BuildConfig",
        "*.Manifest*",
    )

internal fun Project.configureKoverAndroid(commonExtension: CommonExtension) {
    commonExtension.buildTypes.named("debug") {
        enableAndroidTestCoverage = true
        enableUnitTestCoverage = true
    }

    pluginManager.apply("org.jetbrains.kotlinx.kover")

    configureKover()
}

internal fun Project.configureKover() {
    extensions.configure<KoverProjectExtension> {
        reports {
            filters {
                excludes {
                    classes(*coverageExclusions.toTypedArray())
                }
            }
        }
    }
}
