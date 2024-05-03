/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.exoplayer

data class ExoPlayerUIEvent(
    val onPlayVideo: () -> Unit,
    val onTriggerPIPMode: () -> Unit,
    val onSavePlaybackState: () -> Unit,
    val onRestorePlaybackState: () -> Unit,
    val onToggleFullScreenMode: (fullScreenMode: Boolean) -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onShowSnackbar: suspend (message: String) -> Unit,
)
