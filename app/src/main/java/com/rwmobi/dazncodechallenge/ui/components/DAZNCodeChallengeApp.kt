/*
 * Copyright (c) 2025. Ryan Wong
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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import com.rwmobi.dazncodechallenge.ui.navigation.AppNavHost
import com.rwmobi.dazncodechallenge.ui.navigation.AppNavItem
import com.rwmobi.dazncodechallenge.ui.theme.VideoPlayerAppTheme
import com.rwmobi.dazncodechallenge.ui.theme.dazn_divider
import com.rwmobi.dazncodechallenge.ui.utils.getPreviewWindowSizeClass
import java.lang.ProcessBuilder.Redirect.to

private enum class NavigationLayoutType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL,
    FULL_SCREEN,
}

private fun WindowSizeClass.calculateNavigationLayout(currentRoute: String?, isInPictureInPictureMode: Boolean): NavigationLayoutType {
    if (currentRoute?.startsWith(AppNavItem.Exoplayer.screenRoute) == true || isInPictureInPictureMode) {
        return NavigationLayoutType.FULL_SCREEN
    }

    return when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            NavigationLayoutType.BOTTOM_NAVIGATION
        }

        else -> {
            // WindowWidthSizeClass.Medium, -- tablet portrait
            // WindowWidthSizeClass.Expanded, -- phone landscape mode
            NavigationLayoutType.NAVIGATION_RAIL
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DAZNCodeChallengeApp(
    modifier: Modifier = Modifier,
    isInPictureInPictureMode: Boolean,
    isPipModeSupported: Boolean,
    windowSizeClass: WindowSizeClass,
    imageLoader: ImageLoader,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    val lastDoubleTappedNavItem = remember { mutableStateOf<AppNavItem?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigationLayoutType = windowSizeClass.calculateNavigationLayout(
        currentRoute = currentRoute,
        isInPictureInPictureMode = isInPictureInPictureMode,
    )

    // 可滑动主页面
    val mainPages = remember {
        listOf(
            AppNavItem.Events,
            AppNavItem.Schedule,
        )
    }

    // ✅ 在这里指定 pageCount
    val pagerState = rememberPagerState(
        pageCount = { mainPages.size },
        initialPage = mainPages.indexOfFirst {
            currentRoute?.startsWith(it.screenRoute) == true
        }.coerceAtLeast(0),
    )

    val coroutineScope = rememberCoroutineScope()
    val actionLabel = stringResource(android.R.string.ok)

    // 滑动同步导航
    LaunchedEffect(pagerState.currentPage) {
        val targetRoute = mainPages[pagerState.currentPage].screenRoute
        if (currentRoute?.startsWith(targetRoute) != true) {
            navController.navigate(targetRoute) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    // 导航点击同步 pager
    LaunchedEffect(currentRoute) {
        val index = mainPages.indexOfFirst { currentRoute?.startsWith(it.screenRoute) == true }
        if (index != -1 && pagerState.currentPage != index) {
            pagerState.scrollToPage(index)
        }
    }

    Row(modifier = modifier) {
        AnimatedVisibility(
            visible = (navigationLayoutType == NavigationLayoutType.NAVIGATION_RAIL),
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
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                AnimatedVisibility(
                    visible = (navigationLayoutType == NavigationLayoutType.BOTTOM_NAVIGATION),
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

            // ✅ 新版 HorizontalPager 用法（pageCount 来源于 pagerState）
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                pageSpacing = 0.dp,
                contentPadding = PaddingValues(0.dp),
                verticalAlignment = Alignment.Top,
                userScrollEnabled = true,
                reverseLayout = false,
            ) { page ->

                AppNavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    imageLoader = imageLoader,
                    lastDoubleTappedNavItem = lastDoubleTappedNavItem.value,
                    isInPictureInPictureMode = isInPictureInPictureMode,
                    isPipModeSupported = isPipModeSupported,
                    onShowSnackbar = { errorMessageText ->
                        if (!isInPictureInPictureMode) {
                            snackbarHostState.showSnackbar(
                                message = errorMessageText,
                                actionLabel = actionLabel,
                                duration = SnackbarDuration.Long,
                            )
                        }
                    },
                    onScrolledToTop = { lastDoubleTappedNavItem.value = null },
                )
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
@PreviewScreenSizes
@Composable
private fun Preview() {
    VideoPlayerAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = VideoPlayerAppTheme.colorScheme.surface,
        ) {
            DAZNCodeChallengeApp(
                modifier = Modifier.fillMaxSize(),
                isInPictureInPictureMode = false,
                isPipModeSupported = false,
                windowSizeClass = getPreviewWindowSizeClass(),
                imageLoader = ImageLoader(LocalContext.current),
                navController = rememberNavController(),
                snackbarHostState = remember { SnackbarHostState() },
            )
        }
    }
}
