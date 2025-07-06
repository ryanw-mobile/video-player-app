/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.events

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import coil3.ImageLoader
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.ui.components.NoDataScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    modifier: Modifier = Modifier,
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
    PullToRefreshBox(
        modifier = modifier.semantics { contentDescription = context.getString(R.string.content_description_pull_to_refresh) },
        isRefreshing = uiState.isLoading,
        onRefresh = {
            uiEvent.onRefresh()
        },
    ) {
        uiState.events?.let { events ->
            if (events.isNotEmpty()) {
                EventsList(
                    modifier = Modifier.fillMaxSize(),
                    events = events,
                    imageLoader = imageLoader,
                    requestScrollToTop = uiState.requestScrollToTop,
                    onScrolledToTop = uiEvent.onScrolledToTop,
                    onPlayVideo = uiEvent.onPlayVideo,
                )
            } else if (!uiState.isLoading) {
                NoDataScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()), // to support pull to refresh
                )
            }
        }
    }

    LaunchedEffect(true) {
        uiEvent.onInitialLoad()
    }
}
