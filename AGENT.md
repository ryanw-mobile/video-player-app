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

## Key Files

*   `app/build.gradle.kts`: Main application module configuration.
*   `gradle/libs.versions.toml`: Centralized dependency management.
*   `app/src/main/java/.../MainActivity.kt`: Entry point for the Compose UI.
*   `app/src/main/java/.../ui/components/DAZNCodeChallengeApp.kt`: Root Composable handling navigation and responsive layout.
*   `app/src/main/java/.../domain/repository/Repository.kt`: Main data interface.
*   `app/src/main/java/.../data/repository/LocalCacheRepository.kt`: Repository implementation with caching logic.
