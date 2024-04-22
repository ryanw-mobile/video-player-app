/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.dazncodechallenge.ui.navigation

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rwmobi.dazncodechallenge.ui.destinations.events.EventsScreen
import com.rwmobi.dazncodechallenge.ui.destinations.events.EventsUIEvent
import com.rwmobi.dazncodechallenge.ui.destinations.schedule.ScheduleScreen
import com.rwmobi.dazncodechallenge.ui.destinations.schedule.ScheduleUIEvent
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
        startDestination = "events",
    ) {
        composable(route = "events") {
            val viewModel: EventsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(lastDoubleTappedNavItem) {
                val enabled = lastDoubleTappedNavItem?.equals(AppNavItem.Events) ?: false
                viewModel.requestScrollToTop(enabled = enabled)
            }

            EventsScreen(
                modifier = Modifier.fillMaxSize(),
                windowSizeClass = windowSizeClass,
                imageLoader = viewModel.getImageLoader(),
                uiState = uiState,
                uiEvent = EventsUIEvent(
                    onRefresh = { viewModel.refresh() },
                    onErrorShown = { viewModel.errorShown(it) },
                    onScrolledToTop = { onScrolledToTop(AppNavItem.Events) },
                    onShowSnackbar = onShowSnackbar,
                    onPlayVideo = { // Navigate to exoplayer
                    },
                ),
            )
        }

        composable(route = "schedule") {
            val viewModel: ScheduleViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(lastDoubleTappedNavItem) {
                val enabled = lastDoubleTappedNavItem?.equals(AppNavItem.Schedule) ?: false
                viewModel.requestScrollToTop(enabled = enabled)
            }

            ScheduleScreen(
                modifier = Modifier.fillMaxSize(),
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
    }
}
