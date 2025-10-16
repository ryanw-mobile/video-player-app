/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hiltAndroidPlugin)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.devtoolsKsp)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
}

// Configuration
val productApkName = "dazncodechallenge"
val productNamespace = "com.rwmobi.dazncodechallenge"
val isRunningOnCI = System.getenv("CI") == "true"

android {
    namespace = productNamespace

    setupSdkVersionsFromVersionCatalog()
    setupSigningAndBuildTypes()
    setupPackagingResourcesDeduplication()

    defaultConfig {
        applicationId = productNamespace

        resourceConfigurations += setOf("en")

        testInstrumentationRunner = "com.rwmobi.dazncodechallenge.ui.test.CustomTestRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    sourceSets.named("test") { java.srcDirs("src/testFixtures/java") }

    testOptions {
        animationsDisabled = true

        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }

        managedDevices {
            allDevices {
                create<ManagedVirtualDevice>("pixel2Api34") {
                    device = "Pixel 2"
                    apiLevel = 34
                    systemImageSource = "aosp-atd"
                    testedAbi = "arm64-v8a" // better performance on CI and Macs
                }
            }
        }
    }

    /**
     * Source sets can no longer contain shared roots as this is impossible to represent in the IDE.
     * In order to share sources between test and androidTest we should be able to use test fixtures.
     */
    testFixtures {
        enable = true
        androidResources = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        optIn.add("kotlin.time.ExperimentalTime")
        freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
    jvmToolchain(17)
}

dependencies {
    // Pager
//    implementation("androidx.compose.foundation:foundation:1.9.2")
    implementation(libs.androidx.compose.foundation)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.windowsizeclass)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.material3.adaptive.android)
    implementation(libs.androidx.profileinstaller)

    // Video Player Library
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.dash)
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.media3.ui)
    "baselineProfile"(project(":baselineprofile"))

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.leakcanary.android)

    // Dagger-Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    kspAndroidTest(libs.hilt.android.compiler)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Glide for Images
    implementation(libs.coil)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.gif)
    implementation(libs.coil.test)

    // Retrofit 2
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)

    // Room 2
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.extensions)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.timber)

    // testing
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockk.android)
    testImplementation(libs.androidx.media3.test.utils)

    // For instrumented tests - with Kotlin
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.uiautomator)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.jetbrains.kotlinx.coroutines.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.media3.test.utils)
}

tasks {
    copyBaselineProfileAfterBuild()
    check { dependsOn("detekt") }
    preBuild { dependsOn("formatKotlin") }
}

detekt { parallel = true }

kover {
    useJacoco()
    reports.filters.excludes {
        packages(
            "$productNamespace.ui.*",
            "$productNamespace.di",
            "$productNamespace.ui.components",
            "$productNamespace.ui.destinations",
            "$productNamespace.ui.navigation",
            "$productNamespace.ui.previewparameter",
            "$productNamespace.ui.theme",
            "$productNamespace.ui.utils",
            "androidx",
            "dagger.hilt.internal.aggregatedroot.codegen",
            "hilt_aggregated_deps",
        )

        classes(
            "dagger.hilt.internal.aggregatedroot.codegen.*",
            "hilt_aggregated_deps.*",
            "$productNamespace.*.Hilt_*",
            "$productNamespace.*.*_Factory*",
            "$productNamespace.*.*_HiltModules*",
            "$productNamespace.*.*Module_*",
            "$productNamespace.*.*MembersInjector*",
            "$productNamespace.*.*_Impl*",
            "$productNamespace.ComposableSingletons*",
            "$productNamespace.BuildConfig*",
            "$productNamespace.*.Fake*",
            "$productNamespace.*.previewparameter*",
            "$productNamespace.DaznApplication*",
            "$productNamespace.data.source.local.*_Impl*",
            "$productNamespace.data.source.local.*Impl_Factory",
            "$productNamespace.BR",
            "$productNamespace.Hilt*",
            "*Fragment",
            "*Fragment\$*",
            "*Activity",
            "*Activity\$*",
            "*.databinding.*",
            "*.BuildConfig",
            "*.DebugUtil",
        )
    }
}

// Gradle Build Utilities
private fun BaseAppModuleExtension.setupSdkVersionsFromVersionCatalog() {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }
}

private fun BaseAppModuleExtension.setupPackagingResourcesDeduplication() {
    packaging.resources {
        excludes.addAll(
            listOf(
                "META-INF/*.md",
                "META-INF/proguard/*",
                "META-INF/*.kotlin_module",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.*",
                "META-INF/LICENSE-notice.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.*",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/*.properties",
                "/*.properties",
            ),
        )
    }
}

private fun BaseAppModuleExtension.setupSigningAndBuildTypes() {
    val releaseSigningConfigName = "releaseSigningConfig"
    val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
    val baseName = "$productApkName-${libs.versions.versionName.get()}-$timestamp"
    val isReleaseBuild = gradle.startParameter.taskNames.any {
        it.contains("Release", ignoreCase = true)
                || it.contains("Bundle", ignoreCase = true)
                || it.equals("build", ignoreCase = true)
    }

    extensions.configure<BasePluginExtension> { archivesName.set(baseName) }

    signingConfigs.create(releaseSigningConfigName) {
        // Only initialise the signing config when a Release or Bundle task is being executed.
        // This prevents Gradle sync or debug builds from attempting to load the keystore,
        // which could fail if the keystore or environment variables are not available.
        // SigningConfig itself is only wired to the 'release' build type, so this guard avoids unnecessary setup.
        if (isReleaseBuild) {
            val keystorePropertiesFile = file("../../keystore.properties")

            if (isRunningOnCI || !keystorePropertiesFile.exists()) {
                println("⚠\uFE0F Signing Config: using environment variables")
                keyAlias = System.getenv("CI_ANDROID_KEYSTORE_ALIAS")
                keyPassword = System.getenv("CI_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD")
                storeFile = file(System.getenv("KEYSTORE_LOCATION"))
                storePassword = System.getenv("CI_ANDROID_KEYSTORE_PASSWORD")
            } else {
                println("⚠\uFE0F Signing Config: using keystore properties")
                val properties = Properties()
                InputStreamReader(
                    FileInputStream(keystorePropertiesFile),
                    Charsets.UTF_8,
                ).use { reader ->
                    properties.load(reader)
                }

                keyAlias = properties.getProperty("alias")
                keyPassword = properties.getProperty("pass")
                storeFile = file(properties.getProperty("store"))
                storePassword = properties.getProperty("storePass")
            }
        } else {
            println("⚠\uFE0F Signing Config: not created for non-release builds.")
        }
    }

    buildTypes {
        fun setOutputFileName() {
            applicationVariants.all {
                outputs
                    .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                    .forEach { output ->
                        val outputFileName = "$productApkName-$name-$versionName-$timestamp.apk"
                        output.outputFileName = outputFileName
                    }
            }
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isDebuggable = true
            setOutputFileName()
        }

        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                ),
            )
            signingConfig = signingConfigs.getByName(name = releaseSigningConfigName)
            setOutputFileName()
        }
    }
}

private fun TaskContainerScope.copyBaselineProfileAfterBuild() {
    afterEvaluate {
        named("generateReleaseBaselineProfile") {
            doLast {
                val outputFile =
                    File(
                        "$projectDir/src/release/generated/baselineProfiles/baseline-prof.txt",
                    )
                val destinationDir = File("$projectDir/src/main")
                destinationDir.mkdirs()
                val destinationFile = File(destinationDir, outputFile.name)
                println("Moving file $outputFile to $destinationDir")
                outputFile.renameTo(destinationFile)
            }
        }
    }
}
