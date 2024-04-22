/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.events

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import com.rwmobi.dazncodechallenge.ui.components.NoDataScreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    imageLoader: ImageLoader,
    uiState: EventsUIState,
    uiEvent: EventsUIEvent,
) {
    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.message

        LaunchedEffect(errorMessage.id) {
            uiEvent.onShowSnackbar(errorMessageText)
            uiEvent.onErrorShown(errorMessage.id)
        }
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pullRefreshState = rememberPullToRefreshState()

    Box(modifier = modifier.nestedScroll(connection = pullRefreshState.nestedScrollConnection)) {
        uiState.events?.let { events ->
            if (events.isNotEmpty()) {
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Medium,
                    WindowWidthSizeClass.Expanded,
                    WindowWidthSizeClass.Compact,
                    -> {
                        EventsListCompact(
                            modifier = Modifier.fillMaxSize(),
                            events = events,
                            imageLoader = imageLoader,
                            requestScrollToTop = uiState.requestScrollToTop,
                            onScrolledToTop = uiEvent.onScrolledToTop,
                        )
                    }

//                    WindowWidthSizeClass.Medium,
//                    WindowWidthSizeClass.Expanded,
//                    -> {
//                        TrendingStaggeredGrid(
//                            modifier = Modifier.fillMaxSize(),
//                            giphyImageItems = giphyImageItems,
//                            imageLoader = imageLoader,
//                            requestScrollToTop = uiState.requestScrollToTop,
//                            onClickToDownload = { imageUrl ->
//                                downloadImage(
//                                    imageUrl = imageUrl,
//                                    coroutineScope = coroutineScope,
//                                    context = context,
//                                    onError = { uiEvent.onShowSnackbar(context.getString(R.string.failed_to_download_file)) },
//                                )
//                            },
//                            onClickToOpen = { url -> context.startBrowserActivity(url = url) },
//                            onClickToShare = { url -> clipboardHistory.add(url) },
//                            onScrolledToTop = uiEvent.onScrolledToTop,
//                        )
                    //                   }
                }
            } else if (!uiState.isLoading) {
                NoDataScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()), // to support pull to refresh
                )
            }
        }

        if (pullRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                if (!uiState.isLoading) {
                    delay(1000) // Trick to let user know work in progress
                    uiEvent.onRefresh()
                }
            }
        }

        LaunchedEffect(uiState.isLoading) {
            if (!uiState.isLoading) {
                pullRefreshState.endRefresh()
            } else {
                pullRefreshState.startRefresh()
            }
        }

        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullRefreshState,
        )

        DisposableEffect(true) {
            uiEvent.onRefresh()
            onDispose { }
        }
    }
}
