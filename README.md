# WIP

[![CI](https://github.com/lscythe/navigo/actions/workflows/ci.yml/badge.svg?branch=develop)](https://github.com/lscythe/navigo/actions/workflows/ci.yml)
[![CodeQL](https://github.com/lscythe/navigo/actions/workflows/codeql.yml/badge.svg?branch=develop)](https://github.com/lscythe/navigo/actions/workflows/codeql.yml)
[![codecov](https://codecov.io/gh/lscythe/navigo/branch/develop/graph/badge.svg)](https://codecov.io/gh/lscythe/navigo)
[![Translation status](https://hosted.weblate.org/widget/navigo/navigo/svg-badge.svg)](https://hosted.weblate.org/engage/navigo/)
[![License](https://img.shields.io/github/license/lscythe/navigo)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.10-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-1.12.0-4285F4?logo=jetpackcompose&logoColor=white)](https://www.jetbrains.com/compose-multiplatform/)

Navigo is a work-in-progress public transport application built with Kotlin and Compose Multiplatform. It targets Android, Desktop, and iOS from shared application, navigation, design, and feature code.

## Platforms

- **Android:** Product flavors, Firebase and local analytics providers, monitoring, baseline profiles, and MapLibre OpenGL rendering.
- **Desktop:** Runnable macOS, Linux, and Windows hosts with native packages and platform-specific MapLibre runtimes.
- **iOS:** SwiftUI host backed by the shared Kotlin framework and MapLibre rendering.

## Current capabilities

- Shared onboarding flow with permissions, local profile setup, privacy controls, and bundled legal documents.
- Shared transit map UI powered by MapLibre Compose.
- Adaptive Navigation 3 application shell.
- Localized Compose Multiplatform resources in English and Indonesian.
- Cross-platform analytics and monitoring provider boundaries.

Navigo remains under active development. Public releases and production backend availability are not yet guaranteed.

## Project structure

```text
app/
├── android/    Android application host
├── desktop/    Desktop application host and native packaging
├── ios/        Kotlin framework and SwiftUI application host
└── shared/     Shared root composition and navigation
api/            Public feature and service contracts
core/           Shared infrastructure, design system, resources, and UI
feature/        Feature API and implementation modules
build-logic/    Convention plugins for supported targets
```

## Localization

Translations are managed through [Hosted Weblate](https://hosted.weblate.org/projects/navigo/navigo/). English is the source language and Indonesian is currently supported.

Shared translatable strings live in:

```text
core/resources/src/commonMain/composeResources/
├── values/strings.xml
└── values-id/strings.xml
```

Translation changes are validated for key and format-placeholder parity by:

```sh
./gradlew :core:resources:checkTranslations
```

## Development

### Prerequisites

- JDK 25
- Android SDK for Android builds
- Xcode and an iOS Simulator runtime for iOS builds
- Platform packaging tools required by `jpackage` for Desktop distributions
- [mise](https://mise.jdx.dev) for repository-managed development tools

Install pinned tools:

```sh
mise install
```

Useful checks:

```sh
./gradlew :app:android:assembleStagingGoogleDebug
./gradlew :app:shared:desktopTest
./gradlew :app:desktop:packageDistributionForCurrentOS
./gradlew :app:ios:compileKotlinIosSimulatorArm64
./gradlew :core:resources:checkTranslations
```

## Git hooks

Commits are formatted and validated by [kempt](https://github.com/ZacSweers/kempt) for Kotlin and whitespace formatting plus Apache license headers. [hk](https://github.com/jdx/hk) orchestrates Git hooks and validates conventional commit messages. Both are managed by mise.

From the repository root:

```sh
mise install
hk install --mise
```

This registers `pre-commit` and `commit-msg` hooks. Configuration lives in `mise.toml`, `hk.pkl`, and `.kempt.toml`.

## License

Navigo is licensed under the [Apache License 2.0](LICENSE).
