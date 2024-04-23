/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.dazncodechallenge.ui.navigation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rwmobi.dazncodechallenge.ui.destinations.events.EventsScreen
import com.rwmobi.dazncodechallenge.ui.destinations.events.EventsUIEvent
import com.rwmobi.dazncodechallenge.ui.destinations.exoplayer.ExoPlayerScreen
import com.rwmobi.dazncodechallenge.ui.destinations.schedule.ScheduleScreen
import com.rwmobi.dazncodechallenge.ui.destinations.schedule.ScheduleUIEvent
import com.rwmobi.dazncodechallenge.ui.theme.dazn_background
import com.rwmobi.dazncodechallenge.ui.theme.dazn_surface
import com.rwmobi.dazncodechallenge.ui.viewmodel.EventsViewModel
import com.rwmobi.dazncodechallenge.ui.viewmodel.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    lastDoubleTappedNavItem: AppNavItem?,
    windowSizeClass: WindowSizeClass,
    onShowSnackbar: suspend (String) -> Unit,
    onScrolledToTop: (AppNavItem) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppNavItem.Events.screenRoute,
    ) {
        composable(route = AppNavItem.Events.screenRoute) {
            val viewModel: EventsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(lastDoubleTappedNavItem) {
                val enabled = lastDoubleTappedNavItem?.equals(AppNavItem.Events) ?: false
                viewModel.requestScrollToTop(enabled = enabled)
            }

            EventsScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = dazn_surface),
                windowSizeClass = windowSizeClass,
                imageLoader = viewModel.getImageLoader(),
                uiState = uiState,
                uiEvent = EventsUIEvent(
                    onRefresh = { viewModel.refresh() },
                    onErrorShown = { viewModel.errorShown(it) },
                    onScrolledToTop = { onScrolledToTop(AppNavItem.Events) },
                    onShowSnackbar = onShowSnackbar,
                    onPlayVideo = { videoUrl ->
                        val uri = Uri.encode(videoUrl)
                        navController.navigate(route = "${AppNavItem.Exoplayer.screenRoute}/$uri")
                    },
                ),
            )
        }

        composable(route = AppNavItem.Schedule.screenRoute) {
            val viewModel: ScheduleViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(lastDoubleTappedNavItem) {
                val enabled = lastDoubleTappedNavItem?.equals(AppNavItem.Schedule) ?: false
                viewModel.requestScrollToTop(enabled = enabled)
            }

            ScheduleScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = dazn_surface),
                windowSizeClass = windowSizeClass,
                imageLoader = viewModel.getImageLoader(),
                uiState = uiState,
                uiEvent = ScheduleUIEvent(
                    onRefresh = { viewModel.refresh() },
                    onErrorShown = { viewModel.errorShown(it) },
                    onScrolledToTop = { onScrolledToTop(AppNavItem.Events) },
                    onShowSnackbar = onShowSnackbar,
                ),
            )
        }

        composable(
            route = "${AppNavItem.Exoplayer.screenRoute}/{videoUrl}",
            arguments = listOf(
                navArgument(name = "videoUrl", builder = { type = NavType.StringType }),
            ),
        ) { backStackEntry ->
            val videoUrlStr = backStackEntry.arguments?.getString("videoUrl") ?: ""
            val videoUrl = Uri.parse(videoUrlStr)

            ExoPlayerScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = dazn_background),
                videoUrl = videoUrl,
            )
        }
    }
}
