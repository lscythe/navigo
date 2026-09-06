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
import dev.lscythe.app.navigo.convention.TranslationParityTask

plugins {
    alias(libs.plugins.navigo.multiplatform.library)
    alias(libs.plugins.navigo.multiplatform.library.compose)
}

kotlin {
    android {
        namespace = "dev.lscythe.app.navigo.core.resources"
        androidResources.enable = true
    }

    sourceSets.commonMain.dependencies {
        implementation(libs.compose.multiplatform.runtime)
        api(libs.compose.multiplatform.resources)
    }
}

compose.resources {
    packageOfResClass = "dev.lscythe.app.navigo.core.resources.generated.resources"
    publicResClass = true
}

val checkTranslations =
    tasks.register<TranslationParityTask>("checkTranslations") {
        sourceFile.set(
            layout.projectDirectory.file("src/commonMain/composeResources/values/strings.xml")
        )
        translationFile.set(
            layout.projectDirectory.file("src/commonMain/composeResources/values-id/strings.xml")
        )
    }

tasks.named("check").configure {
    dependsOn(checkTranslations)
}
