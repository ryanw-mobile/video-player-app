/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.events

data class EventsUIEvent(
    val onInitialLoad: () -> Unit,
    val onRefresh: () -> Unit,
    val onScrolledToTop: () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onShowSnackbar: suspend (message: String) -> Unit,
    val onPlayVideo: (videoUrl: String) -> Unit,
)
