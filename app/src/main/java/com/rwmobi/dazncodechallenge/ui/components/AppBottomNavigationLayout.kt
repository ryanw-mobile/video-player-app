/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.dazncodechallenge.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rwmobi.dazncodechallenge.ui.navigation.AppNavHost
import com.rwmobi.dazncodechallenge.ui.navigation.AppNavItem
import com.rwmobi.dazncodechallenge.ui.theme.DAZNCodeChallengeTheme
import com.rwmobi.dazncodechallenge.ui.theme.dazn_divider

@Composable
fun AppBottomNavigationLayout(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    val lastDoubleTappedNavItem = remember { mutableStateOf<AppNavItem?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isFullScreenPlayer = currentRoute?.startsWith(AppNavItem.Exoplayer.screenRoute) == true

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = !isFullScreenPlayer,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                Column {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = dazn_divider,
                    )

                    AppBottomNavigationBar(
                        navController = navController,
                        onCurrentRouteSecondTapped = { lastDoubleTappedNavItem.value = it },
                    )
                }
            }
        },
    ) { paddingValues ->
        val actionLabel = stringResource(android.R.string.ok)
        AppNavHost(
            modifier = Modifier
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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@PreviewFontScale
@PreviewDynamicColors
@PreviewLightDark
@Composable
private fun Preview() {
    DAZNCodeChallengeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            AppBottomNavigationLayout(
                modifier = Modifier.fillMaxSize(),
                windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
                navController = rememberNavController(),
                snackbarHostState = remember { SnackbarHostState() },
            )
        }
    }
}
