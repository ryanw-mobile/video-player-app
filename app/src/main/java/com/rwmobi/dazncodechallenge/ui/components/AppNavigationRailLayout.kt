/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.dazncodechallenge.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rwmobi.dazncodechallenge.ui.navigation.AppNavHost
import com.rwmobi.dazncodechallenge.ui.navigation.AppNavItem
import com.rwmobi.dazncodechallenge.ui.theme.DAZNCodeChallengeTheme
import com.rwmobi.dazncodechallenge.ui.theme.dazn_divider
import com.rwmobi.dazncodechallenge.ui.theme.dazn_surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationRailLayout(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    val lastDoubleTappedNavItem = remember { mutableStateOf<AppNavItem?>(null) }

    Row(modifier = modifier) {
        AppNavigationRail(
            modifier = Modifier.fillMaxHeight(),
            navController = navController,
            onCurrentRouteSecondTapped = { lastDoubleTappedNavItem.value = it },
        )

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            color = dazn_divider,
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                )
            },
        ) { paddingValues ->
            // Note that we take MaxSize and expect individual screens to handle screen size
            val actionLabel = stringResource(android.R.string.ok)
            AppNavHost(
                modifier = Modifier
                    .background(color = dazn_surface)
                    .fillMaxSize()
                    .padding(paddingValues),
                windowSizeClass = windowSizeClass,
                navController = navController,
                lastDoubleTappedNavItem = lastDoubleTappedNavItem.value,
                onShowSnackbar = { errorMessageText ->
                    snackbarHostState.showSnackbar(
                        message = errorMessageText,
                        actionLabel = actionLabel,
                        duration = SnackbarDuration.Long,
                    )
                },
                onScrolledToTop = { lastDoubleTappedNavItem.value = null },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Preview(
    name = "Phone - Landscape Light",
    device = "spec:width = 411dp, height = 891dp, orientation = landscape, dpi = 420",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Phone - Landscape Dark",
    device = "spec:width = 411dp, height = 891dp, orientation = landscape, dpi = 420",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun Preview() {
    DAZNCodeChallengeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            AppNavigationRailLayout(
                modifier = Modifier.fillMaxSize(),
                windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
                navController = rememberNavController(),
                snackbarHostState = remember { SnackbarHostState() },
            )
        }
    }
}
