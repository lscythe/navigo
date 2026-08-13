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
import org.gradle.kotlin.dsl.invoke

enum class FlavorDimension {
    Channel
}

enum class NavigoFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    Prod(FlavorDimension.Channel),
    Beta(FlavorDimension.Channel, applicationIdSuffix = ".beta"),
    Rc(FlavorDimension.Channel, applicationIdSuffix = ".rc"),
}

fun configureFlavors(
    commonExtension: CommonExtension,
    flavorConfigurationBlock: ProductFlavor.(flavor: NavigoFlavor) -> Unit = {},
) {
    commonExtension.apply {
        FlavorDimension.entries.forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name
        }

        productFlavors {
            NavigoFlavor.entries.forEach { niaFlavor ->
                register(niaFlavor.name) {
                    dimension = niaFlavor.dimension.name
                    flavorConfigurationBlock(this, niaFlavor)
                    if (
                        commonExtension is ApplicationExtension && this is ApplicationProductFlavor
                    ) {
                        if (niaFlavor.applicationIdSuffix != null) {
                            applicationIdSuffix = niaFlavor.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}
