/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rwmobi.dazncodechallenge.ui.navigation.AppNavHost
import com.rwmobi.dazncodechallenge.ui.navigation.AppNavItem
import com.rwmobi.dazncodechallenge.ui.theme.DAZNCodeChallengeTheme
import com.rwmobi.dazncodechallenge.ui.theme.dazn_divider
import com.rwmobi.dazncodechallenge.ui.utils.getPreviewWindowSizeClass

private enum class NavigationLayoutType {
    BottomNavigation,
    NavigationRail,
    FullScreen,
}

private fun calculateNavigationLayout(windowWidthSizeClass: WindowWidthSizeClass, currentRoute: String?, isInPipMode: Boolean): NavigationLayoutType {
    if (currentRoute?.startsWith(AppNavItem.Exoplayer.screenRoute) == true || isInPipMode) {
        return NavigationLayoutType.FullScreen
    }
    return when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            NavigationLayoutType.BottomNavigation
        }

        else -> {
            // WindowWidthSizeClass.Medium, -- tablet portrait
            // WindowWidthSizeClass.Expanded, -- phone landscape mode
            NavigationLayoutType.NavigationRail
        }
    }
}

@Composable
fun DAZNCodeChallengeApp(
    modifier: Modifier = Modifier,
    isInPipMode: Boolean,
    isPipModeSupported: Boolean,
    windowSizeClass: WindowSizeClass,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onTriggerPIPMode: () -> Unit,
) {
    val lastDoubleTappedNavItem = remember { mutableStateOf<AppNavItem?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navigationLayoutType = calculateNavigationLayout(windowWidthSizeClass = windowSizeClass.widthSizeClass, currentRoute = currentRoute, isInPipMode = isInPipMode)

    Row(modifier = modifier) {
        AnimatedVisibility(
            visible = (navigationLayoutType == NavigationLayoutType.NavigationRail),
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = shrinkHorizontally() + fadeOut(),
        ) {
            Row {
                AppNavigationRail(
                    modifier = Modifier.fillMaxHeight(),
                    navController = navController,
                    onCurrentRouteSecondTapped = { lastDoubleTappedNavItem.value = it },
                )

                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    color = dazn_divider,
                )
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                )
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = (navigationLayoutType == NavigationLayoutType.BottomNavigation),
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = shrinkVertically() + fadeOut(),
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
            // Note that we take MaxSize and expect individual screens to handle screen size
            val actionLabel = stringResource(android.R.string.ok)
            AppNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                navController = navController,
                lastDoubleTappedNavItem = lastDoubleTappedNavItem.value,
                isInPipMode = isInPipMode,
                onShowSnackbar = { errorMessageText ->
                    snackbarHostState.showSnackbar(
                        message = errorMessageText,
                        actionLabel = actionLabel,
                        duration = SnackbarDuration.Long,
                    )
                },
                onScrolledToTop = { lastDoubleTappedNavItem.value = null },
                onTriggerPIPMode = onTriggerPIPMode,
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun Preview() {
    DAZNCodeChallengeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            DAZNCodeChallengeApp(
                modifier = Modifier.fillMaxSize(),
                isInPipMode = false,
                isPipModeSupported = false,
                windowSizeClass = getPreviewWindowSizeClass(),
                navController = rememberNavController(),
                snackbarHostState = remember { SnackbarHostState() },
                onTriggerPIPMode = {},
            )
        }
    }
}
