/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.rwmobi.dazncodechallenge.ui.components.DAZNCodeChallengeApp
import com.rwmobi.dazncodechallenge.ui.theme.DAZNCodeChallengeTheme
import com.rwmobi.dazncodechallenge.ui.utils.hasPictureInPicturePermission
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isInPipMode by mutableStateOf(isInPictureInPictureMode)
    private var isPipModeSupported by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            DAZNCodeChallengeTheme {
                DAZNCodeChallengeApp(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .safeDrawingPadding(),
                    windowSizeClass = calculateWindowSizeClass(this),
                    isPipModeSupported = isPipModeSupported,
                    isInPictureInPictureMode = isInPipMode,
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                )
            }
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        Timber.v("\uD83D\uDD25  isInPictureInPictureMode = $isInPictureInPictureMode")
        this.isInPipMode = isInPictureInPictureMode
    }

    override fun onResume() {
        super.onResume()
        // This is not triggered when entering PIP mode but returning from PIP mode.
        isPipModeSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) &&
            applicationContext.hasPictureInPicturePermission()
    }
}
