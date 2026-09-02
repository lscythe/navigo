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
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Navigo"

include(":app")

include(
    ":api:auth",
    ":api:legal",
    ":api:transit",
)

include(
    ":core:analytics",
    ":core:analytics-firebase",
    ":core:analytics-local",
    ":core:common",
    ":core:database",
    ":core:datastore",
    ":core:designsystem",
    ":core:monitoring",
    ":core:monitoring-kermit",
    ":core:monitoring-sentry",
    ":core:navigation",
    ":core:network",
    ":core:testing",
    ":core:testing-screenshot",
    ":core:ui",
)

include(
    ":feature:home:api",
    ":feature:home:impl",
    ":feature:onboarding:api",
    ":feature:onboarding:impl",
)

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_21)) {
    """
    Navigo requires JDK 21+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """
        .trimIndent()
}
