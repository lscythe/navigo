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

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

enum class FlavorDimension {
    Channel,
    Distribution,
}

enum class NavigoFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    Staging(FlavorDimension.Channel, applicationIdSuffix = ".staging"),
    Prod(FlavorDimension.Channel),
    Beta(FlavorDimension.Channel, applicationIdSuffix = ".beta"),
    Rc(FlavorDimension.Channel, applicationIdSuffix = ".rc"),
    Google(FlavorDimension.Distribution),
    Huawei(FlavorDimension.Distribution),
}

fun configureFlavors(
    commonExtension: CommonExtension,
    flavorConfigurationBlock: ProductFlavor.(flavor: NavigoFlavor) -> Unit = {},
) {
    commonExtension.apply {
        FlavorDimension.entries.forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name.lowercase()
        }

        productFlavors {
            NavigoFlavor.entries.forEach { navigoFlavor ->
                register(navigoFlavor.name.lowercase()) {
                    dimension = navigoFlavor.dimension.name.lowercase()
                    flavorConfigurationBlock(this, navigoFlavor)
                    if (
                        commonExtension is ApplicationExtension && this is ApplicationProductFlavor
                    ) {
                        if (navigoFlavor.applicationIdSuffix != null) {
                            applicationIdSuffix = navigoFlavor.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}

private fun <T> cartesianProduct(lists: List<List<T>>): List<List<T>> =
    lists.fold(listOf(listOf())) { acc, list -> acc.flatMap { combo -> list.map { combo + it } } }

/**
 * Registers a `<flavor><Flavor>Implementation` config per cross-dimension flavor combo (e.g.
 * `prodGoogleImplementation`), extending into each matching variant once AGP creates it.
 */
fun Project.registerFlavorConfigurations(
    androidComponentsExtension: AndroidComponentsExtension<*, *, *>
) {
    val flavorsByDimension =
        FlavorDimension.entries.map { dimension ->
            NavigoFlavor.entries.filter { it.dimension == dimension }
        }

    cartesianProduct(flavorsByDimension).forEach { combo ->
        val configName =
            combo
                .mapIndexed { index, flavor ->
                    if (index == 0) flavor.name.replaceFirstChar(Char::lowercase) else flavor.name
                }
                .joinToString("") + "Implementation"

        val bucket =
            configurations.maybeCreate(configName).apply {
                isCanBeConsumed = false
                isCanBeResolved = false
            }

        val selector =
            combo.fold(androidComponentsExtension.selector()) { selector, flavor ->
                selector.withFlavor(flavor.dimension.name.lowercase() to flavor.name.lowercase())
            }
        androidComponentsExtension.onVariants(selector) { variant ->
            configurations.named("${variant.name}Implementation") { extendsFrom(bucket) }
        }
    }
}
