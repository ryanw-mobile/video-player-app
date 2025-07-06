/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.navigation

import android.net.Uri
import android.util.Rational
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
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
import coil3.ImageLoader
import com.rwmobi.dazncodechallenge.ui.destinations.events.EventsScreen
import com.rwmobi.dazncodechallenge.ui.destinations.events.EventsUIEvent
import com.rwmobi.dazncodechallenge.ui.destinations.events.EventsViewModel
import com.rwmobi.dazncodechallenge.ui.destinations.exoplayer.ExoPlayerScreen
import com.rwmobi.dazncodechallenge.ui.destinations.exoplayer.ExoPlayerUIEvent
import com.rwmobi.dazncodechallenge.ui.destinations.exoplayer.ExoPlayerViewModel
import com.rwmobi.dazncodechallenge.ui.destinations.schedule.ScheduleScreen
import com.rwmobi.dazncodechallenge.ui.destinations.schedule.ScheduleUIEvent
import com.rwmobi.dazncodechallenge.ui.destinations.schedule.ScheduleViewModel
import com.rwmobi.dazncodechallenge.ui.theme.dazn_background
import com.rwmobi.dazncodechallenge.ui.theme.dazn_surface
import com.rwmobi.dazncodechallenge.ui.utils.enterPIPMode

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    isInPictureInPictureMode: Boolean,
    isPipModeSupported: Boolean,
    lastDoubleTappedNavItem: AppNavItem?,
    imageLoader: ImageLoader,
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
                imageLoader = imageLoader,
                uiState = uiState,
                uiEvent = EventsUIEvent(
                    onInitialLoad = { viewModel.fetchCacheAndRefresh() },
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
                imageLoader = imageLoader,
                uiState = uiState,
                uiEvent = ScheduleUIEvent(
                    onInitialLoad = { viewModel.fetchCacheAndRefresh() },
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

            val viewModel: ExoPlayerViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val activity = LocalActivity.current

            ExoPlayerScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = dazn_background),
                shouldShowPiPButton = !isInPictureInPictureMode && isPipModeSupported && uiState.videoWidth > 0 && uiState.videoHeight > 0,
                player = viewModel.getPlayer(),
                uiState = uiState,
                uiEvent = ExoPlayerUIEvent(
                    onPlayVideo = { viewModel.playVideo(videoUrl = videoUrl.toString()) },
                    onErrorShown = { viewModel.errorShown(it) },
                    onSavePlaybackState = { viewModel.savePlaybackState() },
                    onRestorePlaybackState = { viewModel.restorePlaybackState() },
                    onShowSnackbar = onShowSnackbar,
                    onEnterPictureInPictureMode = { activity?.enterPIPMode(Rational(uiState.videoWidth, uiState.videoHeight)) },
                    onToggleFullScreenMode = { viewModel.setFullScreenMode(isFullScreenMode = it) },
                ),
            )
        }
    }
}
