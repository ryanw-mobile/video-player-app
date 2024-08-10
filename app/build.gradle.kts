/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

import com.android.build.api.dsl.ManagedVirtualDevice
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hiltAndroidPlugin)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.devtoolsKsp)
    alias(libs.plugins.gradleKtlint)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.rwmobi.dazncodechallenge"
    compileSdk = libs.versions.compileSdk.get().toInt()

    signingConfigs {
        create("release") {
            val isRunningOnCI = System.getenv("CI") == "true"
            val keystorePropertiesFile = file("../../keystore.properties")

            if (isRunningOnCI) {
                println("Signing Config: using environment variables")
                keyAlias = System.getenv("CI_ANDROID_KEYSTORE_ALIAS")
                keyPassword = System.getenv("CI_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD")
                storeFile = file(System.getenv("KEYSTORE_LOCATION"))
                storePassword = System.getenv("CI_ANDROID_KEYSTORE_PASSWORD")
            } else if (keystorePropertiesFile.exists()) {
                println("Signing Config: using keystore properties")
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
            } else {
                println("Signing Config: skipping signing")
            }
        }
    }

    defaultConfig {
        applicationId = "com.rwmobi.dazncodechallenge"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        buildToolsVersion = libs.versions.buildToolsVersion.get()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        resourceConfigurations += setOf("en")

        testInstrumentationRunner = "com.rwmobi.dazncodechallenge.ui.test.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Bundle output filename
        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
        setProperty("archivesBaseName", "dazncodechallenge-$versionName-$timestamp")
    }

    buildTypes {
        fun setOutputFileName() {
            applicationVariants.all {
                val variant = this
                variant.outputs
                    .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                    .forEach { output ->
                        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
                        val outputFileName =
                            "dazncodechallenge-${variant.versionName}-$timestamp-${variant.name}.apk"
                        output.outputFileName = outputFileName
                    }
            }
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false
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

            signingConfigs.getByName("release").keyAlias?.let {
                signingConfig = signingConfigs.getByName("release")
            }

            setOutputFileName()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/licenses/ASM",
            )
            pickFirsts += listOf(
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86/attach_hotspot_windows.dll",
            )
        }
    }

    sourceSets {
        named("test") {
            java.srcDirs("src/testFixtures/java")
        }
    }

    testOptions {
        animationsDisabled = true

        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }

        managedDevices {
            devices {
                create<ManagedVirtualDevice>("pixel2Api34") {
                    device = "Pixel 2"
                    apiLevel = 34
                    systemImageSource = "aosp-atd"
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
}

kotlin {
    jvmToolchain(17)
}

dependencies {
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
    implementation(libs.coil.gif)

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
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockk.android)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.androidx.media3.test.utils)

    // For instrumented tests - with Kotlin
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.uiautomator)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.kotest.assertions.core)
    androidTestImplementation(libs.jetbrains.kotlinx.coroutines.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.media3.test.utils)
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    android.set(true)
    ignoreFailures.set(true)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.SARIF)
    }
}

tasks.named("preBuild") {
    dependsOn(tasks.named("ktlintFormat"))
}

kover {
    reports {
        // common filters for all reports of all variants
        filters {
            // exclusions for reports
            excludes {
                // excludes class by fully-qualified JVM class name, wildcards '*' and '?' are available
                classes(
                    listOf(
                        "com.rwmobi.dazncodechallenge.DaznApplication",
                        "com.rwmobi.dazncodechallenge.*.*MembersInjector",
                        "com.rwmobi.dazncodechallenge.*.*Factory",
                        "com.rwmobi.dazncodechallenge.*.*HiltModules*",
                        "com.rwmobi.dazncodechallenge.data.source.local.*_Impl*",
                        "com.rwmobi.dazncodechallenge.data.source.local.*Impl_Factory",
                        "com.rwmobi.dazncodechallenge.BR",
                        "com.rwmobi.dazncodechallenge.BuildConfig",
                        "com.rwmobi.dazncodechallenge.Hilt*",
                        "com.rwmobi.dazncodechallenge.*.Hilt_*",
                        "com.rwmobi.dazncodechallenge.ComposableSingletons*",
                        "*Fragment",
                        "*Fragment\$*",
                        "*Activity",
                        "*Activity\$*",
                        "*.BuildConfig",
                        "*.DebugUtil",
                    ),
                )
                // excludes all classes located in specified package and it subpackages, wildcards '*' and '?' are available
                packages(
                    listOf(
                        "com.rwmobi.dazncodechallenge.di",
                        "com.rwmobi.dazncodechallenge.ui.components",
                        "com.rwmobi.dazncodechallenge.ui.destinations",
                        "com.rwmobi.dazncodechallenge.ui.navigation",
                        "com.rwmobi.dazncodechallenge.ui.previewparameter",
                        "com.rwmobi.dazncodechallenge.ui.theme",
                        "com.rwmobi.dazncodechallenge.ui.utils",
                        "androidx",
                        "dagger.hilt.internal.aggregatedroot.codegen",
                        "hilt_aggregated_deps",
                    ),
                )
            }
        }
    }
}
