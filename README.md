# DAZN Code Challenge - Android TV ![Gradle Build](https://github.com/ryanw-mobile/dazn-code-challenge/actions/workflows/main_build.yml/badge.svg) [![codecov](https://codecov.io/gh/ryanw-mobile/dazn-code-challenge/graph/badge.svg?token=YK2DNKYBRO)](https://app.codecov.io/github/ryanw-mobile/dazn-code-challenge)

A responsive Android sample app written in Kotlin and Jetpack Compose, supporting different
navigation layout on screen sizes. The video player is currently Jetpack Media 3.

The events under the Events tab provides video playback.
The schedule under the Schedule tab refreshes automatically every 30 seconds.
For both tabs, you can always do swipe-to-refresh, or tap the navigation icon to scroll to the top
of the list.

## History

This was a code test assignment as a part of the interview process. The task covered
common RESTApi, SQLite, RecyclerView, Constraint Layout, MVVM, plus dependency injection and
testings.

Although the title carries "Android TV", it has nothing to do with that - this is an Android mobile
App as required by the specifications.

The interview process was concluded in October, 2021, but I am still keep on improving the codes for
demonstration purpose.

The original XML View version is no longer maintained, you
can [access to the XML branch here](https://github.com/ryanw-mobile/dazn-code-challenge/tree/xml).

Please note that the APIs are supplied by DAZN in 2021 for recruitment purpose. They may not work at
any time.

## Screenshots

&nbsp;

<p align="center">
  <img src="screenshots/240424_screen0.png" width="200" />
  <img src="screenshots/240424_screen1.png" width="200" />
  <img src="screenshots/240424_screen2.png" width="200" />
</p>
<p align="center">
  <img src="screenshots/240424_screen3.png" width="400" />
</p>

&nbsp;

## To-do lists

Planned enhancements are
now [logged as issues](https://github.com/ryanw-mobile/dazn-code-challenge/issues?q=is%3Aopen+is%3Aissue+label%3Arefactor%2Cfeature%2Cfix%2Ctest).

### High level architecture

* Kotlin
* MVVM & clean architecture
* Jetpack Compose - Single Activity
* Kotlin Coroutines and Flow
* Dependency Injection using Dagger Hilt
* Material 3
* Dynamic screen layout support using Windows Size Class
* Media 3 video player
* Gradle Kotlin DSL and Version Catalog
* Baseline Profile

### Major libraries used

* [Jetpack Compose](https://developer.android.com/jetpack/androidx/releases/compose) - Modern
  toolkit for building native UI
* [Jetpack Navigation for Compose](https://developer.android.com/jetpack/androidx/releases/navigation#navigation-compose) -
  Navigation library for Jetpack Compose applications
* [Jetpack Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) -
  Lifecycle-aware components, including ViewModel support for Jetpack Compose
* [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Asynchronous programming
  with coroutines
* [Kotlin Flow](https://kotlinlang.org/docs/flow.html) - Reactive streams for Kotlin
* [Jetpack Room](https://developer.android.com/jetpack/androidx/releases/room) - Database library
  for local data cache
* [Retrofit2](https://square.github.io/retrofit/) - HTTP client for Android and Java
* [Moshi](https://github.com/square/moshi) - Modern JSON library for Android and Java
* [Splash Screen API](https://developer.android.com/guide/topics/ui/splash-screen) - Official splash
  screen solution
* [Coil](https://coil-kt.github.io/coil/) - Image loading library for Android, leveraging Kotlin
  Coroutines
* [compose-video](https://github.com/dsa28s/compose-video) - Media3 Video Player wrapper (buggy, to
  be replaced/fixed)
* [Dagger Hilt](https://dagger.dev/hilt/) - Dependency injection framework
* [Timber](https://github.com/JakeWharton/timber) - Logging utility
* [LeakCanary](https://github.com/square/leakcanary) - Memory leak detection tool
* `JUnit 4` - Testing framework and runner
* [Robolectric](https://robolectric.org/) - Testing framework for Android-JVM tests
* [kotest](https://kotest.io/) - We use the assertion library only, not the runner
* [MockK](https://mockk.io/) - Mocking library for Kotlin
* [Ktlint Gradle](https://github.com/jlleitschuh/ktlint-gradle) - Plugin for linting and formatting
  Kotlin code
* [Kover](https://github.com/Kotlin/kotlinx-kover) - Kotlin code coverage tool
* [Github Action](https://github.com/features/actions) - CI/CD
* [codecov](https://codecov.io/) - code coverage

## Binaries download

If you want to try out the app without building it, check out
the [Releases section](https://github.com/ryanw-mobile/dazn-code-challenge/releases) where you can
find the APK and App Bundles for each major version.

## Requirements

* Android Studio Iguana | 2023.2.1
* Android device or simulator running Android 9.0+ (API 28)

## Building the App

### Setting up the keystore

Release builds will be signed if either the keystore file or environment variables are set.
Otherwise, the app will be built unsigned.

### Local

* Android Keystore is not being stored in this repository. You need your own Keystore to generate
  the apk / App Bundle

* If your project folder is at `/app/dazn-code-challenge/`, the Keystore file
  and `keystore.properties`
  should be placed at `/app/`

* The format of `keystore.properties` is:
  ```
     store=/app/release-key.keystore
     alias=<alias>
     pass=<alias password>
     storePass=<keystore password>
  ```

### CI environment

* This project has been configured to support automated CI builds.

* The following environment variables have been set to provide the keystore:
  ```
     CI = true
     HOME = <the home directory of the bitrise environment>
     CI_ANDROID_KEYSTORE_PASSWORD = <your keystore password>
     CI_ANDROID_KEYSTORE_ALIAS = <your keystore alias>
     CI_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD = <your keystore private key password>
  ```

### Build and install on the connected device

This app has two build variants: `Debug` and `Release`. The most common build commands are:

* `./gradlew clean installDebug`
* `./gradlew clean instal`
* `./gradlew clean bundleRelease`
* `./gradlew clean assembleRelease`

The generated apk(s) will be stored under `app/build/outputs/`
Debug builds will have an App package name suffix `.debug`
