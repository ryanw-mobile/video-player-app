# AI Agent Instructions

## Project Overview

This is a responsive Android sample application built with modern Android development practices. It demonstrates video playback capabilities, local caching, and adaptive UI layouts.

*   **Type:** Android Application (Kotlin)
*   **Architecture:** MVVM (Model-View-ViewModel) with Clean Architecture principles (Data, Domain, UI layers).
*   **Key Technologies:**
    *   **UI:** Jetpack Compose for declarative UI.
    *   **Navigation:** Jetpack Compose Navigation with adaptive layouts based on Window Size Classes.
    *   **Dependency Injection:** Dagger Hilt.
    *   **Local Database:** Room for offline caching of events and schedules.
    *   **Networking:** Retrofit with Moshi for JSON parsing.
    *   **Media:** Jetpack Media3 ExoPlayer for video playback.
    *   **Image Loading:** Coil.
    *   **Concurrency:** Kotlin Coroutines and Flow for reactive data streams.
    *   **Logging:** Timber.

## Building and Running

The project uses Gradle with the Kotlin DSL and Version Catalog.

*   **Build Debug APK:**
    ```bash
    ./gradlew assembleDebug
    ```
*   **Build Release Bundle (AAB):** (Requires `keystore.properties` or environment variables)
    ```bash
    ./gradlew bundleRelease
    ```
*   **Run Unit Tests:**
    ```bash
    ./gradlew test
    # Run a single test class or method:
    ./gradlew test --tests "com.rwmobi.dazncodechallenge.data.repository.LocalCacheRepositoryTest"
    ./gradlew test --tests "com.rwmobi.dazncodechallenge.data.repository.LocalCacheRepositoryTest.refreshEvents*"
    ```
*   **Run Instrumented Tests:**
    ```bash
    ./gradlew connectedAndroidTest
    ```
*   **Static Analysis (Detekt):**
    ```bash
    ./gradlew detekt
    ```
*   **Kotlin Linting/Formatting:**
    ```bash
    ./gradlew formatKotlin  # To fix formatting
    ./gradlew lintKotlin    # To check formatting
    ```
*   **Generate Baseline Profile:**
    ```bash
    ./gradlew :app:generateBaselineProfile
    ```

## Development Conventions

*   **Architecture Layers:**
    *   `domain`: Contains repository interfaces and business models (`Event`, `Schedule`). This layer should be free of Android dependencies.
    *   `data`: Implements the `domain` repository interfaces. Handles data fetching from `network` (Retrofit) and `local` (Room) sources.
    *   `ui`: Contains Compose components, ViewModels, and UI state/event models.
*   **Dependency Injection:** Always use Hilt for injecting dependencies. Use custom qualifiers (e.g., `@IoDispatcher`) where appropriate.
*   **State Management:** ViewModels expose UI state using `StateFlow`. UI events (actions) are handled through public methods in the ViewModel.
*   **UI Components:** Use the defined theme in `ui/theme`. Aim for responsive components that adapt to different screen sizes.
*   **Testing:**
    *   Write unit tests for ViewModels and Repositories.
    *   Use `testFixtures` for sharing test data across different test modules.
    *   UI tests should use the Robot pattern for better maintainability (see `MainActivityTestRobot.kt`).
*   **Coding Style:** Follow the project's formatting rules (enforced by `kotlinter` and `detekt`).
*   **Version Management:** All dependency versions are managed in `gradle/libs.versions.toml`.

## Architecture Details

### Cache Synchronization (Dirty Bit Pattern)

`RoomDbDataSource` replaces the cache atomically in three steps rather than delete-and-insert:
1. Mark all existing rows `dirty = true`
2. `insertAll()` with `REPLACE` conflict strategy (new rows arrive with `dirty = false`)
3. Delete remaining rows where `dirty = true`

This safely handles full-dataset API responses. The repository separates reads from refreshes — `get*()` always reads local DB only; `refresh*()` fetches from network and writes to local DB.

### UIEvent Function Interfaces

Screens do not call ViewModels directly. Each destination defines a `*UIEvent` data class of lambdas that is constructed in the navigation graph and passed into the screen composable. This decouples screens from ViewModel types and simplifies Compose Previews.

### Adaptive Layout

`DAZNCodeChallengeApp` maps `WindowWidthSizeClass` to an internal `NavigationLayoutType` enum:
- `COMPACT` → `BOTTOM_NAVIGATION`
- `MEDIUM` / `EXPANDED` → `NAVIGATION_RAIL`
- ExoPlayer route **or** PiP active → `FULL_SCREEN` (no navigation chrome)

The ExoPlayer destination is not listed in the navigation bar entries (`AppNavItem`); it is reached via `"exoplayer/{videoUrl}"` with a URL-encoded path parameter.

### Error Message Queue

ViewModels hold a `List<ErrorMessage>` in UI state, each entry keyed by a `UUID`. Calling `onErrorShown(id)` filters that ID from the list, preventing duplicate Snackbars and allowing per-error dismissal.

### Testing: testFixtures

Shared test data lives in `app/src/testFixtures/` and provides domain model instances, DB entity instances, network DTO instances, and fake implementations (`FakeRepository`, `FakeLocalDataSource`, `FakeRemoteDataSource`). Inject `UnconfinedTestDispatcher()` in place of the real IO dispatcher for synchronous coroutine execution in unit tests.

### Code Quality Gate

Detekt is configured with `maxIssues: 0` in `config/detekt/detekt.yml` — any new violation fails the build. Notable non-default thresholds: `LongMethod` is 120 lines but excludes `@Composable` functions; `LongParameterList` ignores `@Composable` and data class constructors.

## Key Files

*   `app/build.gradle.kts`: Main application module configuration.
*   `gradle/libs.versions.toml`: Centralized dependency management.
*   `app/src/main/java/.../MainActivity.kt`: Entry point for the Compose UI.
*   `app/src/main/java/.../ui/components/DAZNCodeChallengeApp.kt`: Root Composable handling navigation and responsive layout.
*   `app/src/main/java/.../domain/repository/Repository.kt`: Main data interface.
*   `app/src/main/java/.../data/repository/LocalCacheRepository.kt`: Repository implementation with caching logic.
