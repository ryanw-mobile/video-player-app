/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.rwmobi.dazncodechallenge.ui.theme.DAZNCodeChallengeTheme

@Composable
fun DAZNCodeChallengeApp(
    isInPipMode: Boolean,
    windowSizeClass: WindowSizeClass,
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    DAZNCodeChallengeTheme {
        Surface(
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize(),
        ) {
            AppMasterNavigationLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                isInPipMode = isInPipMode,
                windowSizeClass = windowSizeClass,
                navController = navController,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}
