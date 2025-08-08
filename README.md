# Video Player App<br/>![Gradle Build](https://github.com/ryanw-mobile/video-player-app/actions/workflows/main_build.yml/badge.svg) [![Codacy Coverage Badge](https://app.codacy.com/project/badge/Coverage/cfdcc6c589174dabad2241af04c8eddd)](https://app.codacy.com/gh/ryanw-mobile/video-player-app/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage) [![Codacy Quality Badge](https://app.codacy.com/project/badge/Grade/cfdcc6c589174dabad2241af04c8eddd)](https://app.codacy.com/gh/ryanw-mobile/video-player-app/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade) [![Renovate enabled](https://img.shields.io/badge/renovate-enabled-brightgreen.svg)](https://renovatebot.com/)

> [!WARNING]  
> I do not support the use of my code for your job applications, as it creates unfair competition. If you copy or fork this for that purpose, you risk disqualification. Please respect the integrity of the process and create your own work.

A responsive Android sample app written in Kotlin and Jetpack Compose, supporting different
navigation layout on screen sizes. The Media 3 Exoplayer is implemented on top of the single
activity architecture. It is fully functional with Picture-in-Picture support.

The events under the Events tab provides video playback.
The schedule under the Schedule tab refreshes automatically every 30 seconds.
For both tabs, you can always do swipe-to-refresh, or tap the navigation icon to scroll to the top
of the list.

## Download the App

If you want to try out the app without building it, check out
the [Releases section](https://github.com/ryanw-mobile/dazn-code-challenge/releases) where you can
find the APK and App Bundles for each major release.

&nbsp;

## History

This was a code test assignment done in October, 2021, but I am still keep on improving the codes for
demonstration purpose.. The task covered common RESTApi, SQLite, RecyclerView, Constraint Layout, MVVM,
plus dependency injection and testings.

The original XML View version is no longer maintained, you
can [access to the XML branch here](https://github.com/ryanw-mobile/video-player-app/tree/xml).

Please note that the APIs are supplied by DAZN in 2021. They may not work at any time.
There is a plan to replace them with my own APIs and contents.

## Screenshots

&nbsp;

<p align="center">
  <img src="screenshots/240424_screen0.png" width="150" />
  <img src="screenshots/240424_screen1.png" width="150" />
  <img src="screenshots/240503_screen2.png" width="150" />
  <img src="screenshots/240501_screen4.png" width="150" />
</p>
<p align="center">
  <img src="screenshots/240424_screen3.png" width="400" />
</p>

&nbsp;

## To-do lists

Planned enhancements are
now [logged as issues](https://github.com/ryanw-mobile/video-player-app/issues?q=is%3Aopen+is%3Aissue+label%3Arefactor%2Cfeature%2Cfix%2Ctest).

## High level architecture

* Kotlin
* MVVM & clean architecture
* Jetpack Compose - Single Activity
* Kotlin Coroutines and Flow
* Dependency Injection using Dagger Hilt
* Material 3
* Dynamic screen layout support using Windows Size Class
* Jetpack Media 3 video player
* Gradle Kotlin DSL and Version Catalog
* Baseline Profile
* Full unit test and UI (Journey) test suite

### Dependencies

* [AndroidX Core KTX](https://developer.android.com/jetpack/androidx/releases/core) - Apache 2.0 - Kotlin extensions for core Android APIs
* [AndroidX Activity Compose](https://developer.android.com/jetpack/androidx/releases/activity) - Apache 2.0 - Jetpack Compose integration with Activity
* [Jetpack Compose](https://developer.android.com/jetpack/androidx/releases/compose) - Apache 2.0 - Modern toolkit for building native UI
* [AndroidX Material 3](https://developer.android.com/jetpack/androidx/releases/compose-material3) - Apache 2.0 - Material Design 3 components for Compose
* [AndroidX Material 3 Window Size Class](https://developer.android.com/jetpack/compose/layouts/adaptive) - Apache 2.0 - Adaptive layout window size classes for Compose
* [AndroidX DataStore Preferences](https://developer.android.com/jetpack/androidx/releases/datastore) - Apache 2.0 - Typed data storage backed by Kotlin coroutines and Flow
* [AndroidX Core SplashScreen](https://developer.android.com/jetpack/androidx/releases/core) - Apache 2.0 - Splash screen API for consistent app startup
* [AndroidX Legacy Support v4](https://developer.android.com/jetpack/androidx/releases/legacy) - Apache 2.0 - Backwards-compatibility support libraries
* [AndroidX Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) - Apache 2.0 - Lifecycle-aware components including Runtime KTX, ViewModel KTX, Runtime Compose, and Extensions
* [AndroidX Room](https://developer.android.com/jetpack/androidx/releases/room) - Apache 2.0 - SQLite object mapping library (Runtime, KTX, Compiler)
* [AndroidX Benchmark](https://developer.android.com/jetpack/androidx/releases/benchmark) - Apache 2.0 - Macrobenchmarking for performance testing
* [AndroidX Profile Installer](https://developer.android.com/jetpack/androidx/releases/profileinstaller) - Apache 2.0 - Install baseline profiles to improve app startup
* [AndroidX Test](https://developer.android.com/jetpack/androidx/releases/test) - Apache 2.0 - Testing libraries including Test Core KTX, JUnit extensions, and Test Rules
* [AndroidX UI Automator](https://developer.android.com/training/testing/ui-automator) - Apache 2.0 - UI automation testing framework
* [AndroidX Media3](https://developer.android.com/guide/topics/media/media3/getting-started) - Apache 2.0 - Media components including ExoPlayer, DASH, Session, UI, and Test Utils
* [JUnit](https://junit.org/junit4/) - EPL 1.0 - Unit testing framework for Java
* [Espresso](https://developer.android.com/training/testing/espresso) - Apache 2.0 - Android UI testing framework
* [Retrofit](https://square.github.io/retrofit/) - Apache 2.0 - Type-safe HTTP client for Android and Java
* [Moshi](https://github.com/square/moshi) - Apache 2.0 - JSON library for Android and Java (Adapters, Kotlin)
* [OkHttp Logging Interceptor](https://square.github.io/okhttp/) - Apache 2.0 - HTTP request/response logging
* [Coil 3](https://github.com/coil-kt/coil) - Apache 2.0 - Image loading for Android (Compose, GIF, Network OkHttp, Test)
* [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Apache 2.0 - Asynchronous programming for Kotlin (Android, Test)
* [Kotlinx DateTime](https://github.com/Kotlin/kotlinx-datetime) - Apache 2.0 - Multiplatform date and time library
* [Robolectric](http://robolectric.org/) - Apache 2.0 - Unit testing framework for Android
* [MockK](https://mockk.io/) - Apache 2.0 - Mocking library for Kotlin
* [Timber](https://github.com/JakeWharton/timber) - Apache 2.0 - Logging utility for Android

### Plugins

* [Android Application Plugin](https://developer.android.com/studio/build/gradle-plugin-3-0-0-migration) - Google - Plugin for building Android applications
* [Kotlin Android Plugin](https://kotlinlang.org/docs/gradle.html#targeting-android) - JetBrains - Kotlin Android support
* [Hilt Android Plugin](https://dagger.dev/hilt/gradle-setup) - Google - Dependency injection with Hilt
* [Compose Compiler Plugin](https://developer.android.com/jetpack/compose) - JetBrains - Kotlin compiler plugin for Jetpack Compose
* [Kover Plugin](https://github.com/Kotlin/kotlinx-kover) - JetBrains - Code coverage tool for Kotlin
* [KSP Plugin](https://github.com/google/ksp) - Google - Kotlin Symbol Processing API
* [Android Test Plugin](https://developer.android.com/studio/build/gradle-plugin-3-0-0-migration) - Google - Plugin for Android test projects
* [Baseline Profile Plugin](https://developer.android.com/studio/profile/baselineprofile) - AndroidX - Generate and package baseline profiles
* [Serialization Plugin](https://github.com/Kotlin/kotlinx.serialization) - JetBrains - Kotlin serialization compiler plugin
* [Detekt Plugin](https://detekt.dev/) - Artur Bosch - Static code analysis for Kotlin
* [Kotlinter Plugin](https://github.com/jeremymailen/kotlinter-gradle) - Jeremy Mailen - Kotlin linter and formatter

## Building the App

### Without Keystore (Debug Builds)

By default, debug builds do not require a keystore. You can run:

```bash
./gradlew assembleDebug
```

No signing config is required unless you explicitly build a release variant.

### With Keystore (Release Builds)

Signing configuration is only triggered when:
- the task includes "Release" or "Bundle"
- or the environment variable `CI=true` is set

There are two ways to supply the keystore:

#### 1. Environment Variables (For CI)

Provide the following environment variables (e.g. in GitHub Secrets):

```
KEYSTORE_LOCATION=./keystore.jks
CI_ANDROID_KEYSTORE_ALIAS=yourAlias
CI_ANDROID_KEYSTORE_PASSWORD=yourKeystorePassword
CI_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD=yourPrivateKeyPassword
```

#### 2. `keystore.properties` File (For Local Builds)

Create a `keystore.properties` file at the root:

```properties
alias=yourAlias
pass=yourPrivateKeyPassword
store=path/to/keystore.jks
storePass=yourKeystorePassword
```

Then build:

```bash
./gradlew bundleRelease
```

### Output Format

Release builds are timestamped using the format:

```
<app-name>-<buildType>-<versionName>-<yyyyMMdd-HHmmss>.apk
```

This applies to both APK and AAB artifacts.

## Workflow Overview

The `.github/workflows/tag_create_release.yml` file defines the CI/CD pipeline. It performs the following:

1. Checks out the code.
2. Sets up JDK 17.
3. Builds the APK and AAB using Gradle.
4. Creates a GitHub Release.
5. Uploads the outputs as release assets.

Trigger the workflow by tagging a commit with the format: `release/*`, e.g. `release/1.2.3`.

### Usage

To trigger the CI pipeline:

```bash
git tag release/1.0.0
git push origin release/1.0.0
```

GitHub Actions will build the release and publish it with assets.
