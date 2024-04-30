/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rwmobi.dazncodechallenge.ui.components.DAZNCodeChallengeApp
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var isInPipMode by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DAZNCodeChallengeApp(
                windowSizeClass = calculateWindowSizeClass(this),
                isInPipMode = isInPipMode,
            )
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        Timber.d("!!!")
        isInPipMode = isInPictureInPictureMode
        if (isInPictureInPictureMode) {
            Timber.d("Should hide the full UI")
            // Hide the full UI (controls, app bars, etc.)
        } else {
            // Restore the full UI
            Timber.d("Should restore the full UI")
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause()")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
    }
}
