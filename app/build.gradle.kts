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
import dev.lscythe.app.navigo.convention.NavigoBuildType

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
plugins {
    alias(libs.plugins.navigo.android.application)
    alias(libs.plugins.navigo.android.application.flavors)
    alias(libs.plugins.navigo.android.application.compose)
    alias(libs.plugins.navigo.metro)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.kotlin.serialization)
}

val monitoringProvider = providers.gradleProperty("monitoringProvider").orElse("Sentry")

android {
    namespace = "dev.lscythe.app.navigo"

    defaultConfig {
        applicationId = "dev.lscythe.app.navigo"
        versionCode = libs.versions.appVersionCode.get().toInt()
        versionName = libs.versions.appVersionName.get()
        buildConfigField(
            "String",
            "SENTRY_DSN",
            "\"${providers.gradleProperty("sentryDsn").get()}\"",
        )
    }

    productFlavors {
        getByName("staging") {
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"${providers.gradleProperty("navigoStagingApiBaseUrl").get()}\"",
            )
        }
        getByName("beta") {
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"${providers.gradleProperty("navigoApiBaseUrl").get()}\"",
            )
        }
        getByName("rc") {
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"${providers.gradleProperty("navigoApiBaseUrl").get()}\"",
            )
        }
        getByName("prod") {
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"${providers.gradleProperty("navigoApiBaseUrl").get()}\"",
            )
        }
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file(providers.gradleProperty("debugKeystoreFile").get())
            storePassword = providers.gradleProperty("debugKeystorePassword").get()
            keyAlias = providers.gradleProperty("debugKeyAlias").get()
            keyPassword = providers.gradleProperty("debugKeyPassword").get()
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = NavigoBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled =
                providers
                    .gradleProperty("minifyWithR8")
                    .map(String::toBooleanStrict)
                    .getOrElse(true)
            applicationIdSuffix = NavigoBuildType.RELEASE.applicationIdSuffix
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))

            signingConfig = signingConfigs.named("debug").get()
        }
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
        jniLibs.keepDebugSymbols +=
            setOf(
                "**/libandroidx.graphics.path.so",
                "**/libdatastore_shared_counter.so",
            )
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

baselineProfile {
    automaticGenerationDuringBuild = true
    dexLayoutOptimization = true
}

dependencies {
    implementation(project(":api:auth"))
    implementation(project(":feature:onboarding:api"))
    implementation(project(":feature:onboarding:impl"))
    implementation(project(":feature:home:api"))
    implementation(project(":feature:home:impl"))

    implementation(project(":core:analytics"))
    prodGoogleImplementation(project(":core:analytics-firebase"))
    stagingGoogleImplementation(project(":core:analytics-local"))
    stagingHuaweiImplementation(project(":core:analytics-local"))
    betaGoogleImplementation(project(":core:analytics-local"))
    betaHuaweiImplementation(project(":core:analytics-local"))
    rcGoogleImplementation(project(":core:analytics-local"))
    rcHuaweiImplementation(project(":core:analytics-local"))
    prodHuaweiImplementation(project(":core:analytics-local"))
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:datastore"))
    implementation(project(":core:monitoring"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.adaptive.navigation3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.androidx.lifecycle.viewModel.navigation3)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
    when (monitoringProvider.get()) {
        "Kermit" -> implementation(project(":core:monitoring-kermit"))
        "Sentry" -> implementation(project(":core:monitoring-sentry"))
        else -> error("Unsupported monitoringProvider: ${monitoringProvider.get()}")
    }
    implementation(libs.androidx.window.core)

    implementation(libs.metro.android)
    implementation(libs.metro.viewmodel)
    implementation(libs.metro.viewmodel.compose)

    debugImplementation(libs.androidx.compose.ui.testManifest)
}

dependencyGuard {
    configuration("prodGoogleReleaseRuntimeClasspath")
    configuration("prodHuaweiReleaseRuntimeClasspath")
}
