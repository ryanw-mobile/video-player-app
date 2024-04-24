/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.exoplayer

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.controller.VideoPlayerControllerConfig
import io.sanghun.compose.video.uri.VideoPlayerMediaItem
import timber.log.Timber

@Composable
fun ExoPlayerScreen(
    modifier: Modifier = Modifier,
    videoUrl: Uri,
) {
    Box(
        modifier = modifier,
    ) {
        VideoPlayer(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            mediaItems = listOf(
                VideoPlayerMediaItem.NetworkMediaItem(url = videoUrl.toString()),
            ),
            handleLifecycle = true,
            autoPlay = true,
            usePlayerController = true,
            enablePip = false,
            handleAudioFocus = true,
            controllerConfig = VideoPlayerControllerConfig(
                showSpeedAndPitchOverlay = false,
                showSubtitleButton = false,
                showCurrentTimeAndTotalTime = true,
                showBufferingProgress = true,
                showForwardIncrementButton = true,
                showBackwardIncrementButton = true,
                showBackTrackButton = false,
                showNextTrackButton = false,
                showFullScreenButton = false, // We made it fullscreen already. Simply rotates the App and we support natively
                showRepeatModeButton = true,
                controllerShowTimeMilliSeconds = 5_000,
                controllerAutoShow = true,
            ),
            volume = 0.5f, // volume 0.0f to 1.0f
            repeatMode = RepeatMode.NONE, // or RepeatMode.ALL, RepeatMode.ONE
            onCurrentTimeChanged = { // long type, current player time (millisec)
                Timber.tag("CurrentTime").e(it.toString())
            },
        )
    }
}
