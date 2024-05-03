/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.exoplayer

import android.app.Activity
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.ui.theme.getDimension
import com.rwmobi.dazncodechallenge.ui.utils.enterFullScreenMode
import com.rwmobi.dazncodechallenge.ui.utils.exitFullScreenMode
import timber.log.Timber

@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerScreen(
    modifier: Modifier = Modifier,
    isInPictureInPictureMode: Boolean,
    shouldShowPiPButton: Boolean,
    player: Player,
    uiState: ExoPlayerUIState,
    uiEvent: ExoPlayerUIEvent,
) {
    // Due to player lifecycle concerns it needs to know the state changes before entering the PIP mode
    var isInPipMode by remember(isInPictureInPictureMode) { mutableStateOf(isInPictureInPictureMode) }

    val localContext = LocalContext.current
    val dimension = LocalConfiguration.current.getDimension()
    var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.message

        LaunchedEffect(errorMessage.id) {
            uiEvent.onShowSnackbar(errorMessageText)
            uiEvent.onErrorShown(errorMessage.id)
        }
    }

    val activity = LocalContext.current as? Activity
    DisposableEffect(Unit) {
        onDispose {
            activity?.exitFullScreenMode()
        }
    }

    SideEffect {
        activity?.let {
            if (uiState.isFullScreenMode) {
                it.enterFullScreenMode()
            } else {
                it.exitFullScreenMode()
            }
        }
    }

    Box(
        modifier = modifier,
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context.applicationContext).also {
                    it.player = player
                    it.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                    it.controllerAutoShow = false
                    it.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    it.setFullscreenButtonClickListener { isFullScreen -> uiEvent.onToggleFullScreenMode(isFullScreen) }
                    it.setShowNextButton(false)
                }
            },
            update = { playerView ->
                Timber.v("🔥 Lifecycle $lifecycle: isInPictureInPictureMode = $isInPictureInPictureMode, shouldPlayOnResume = ${uiState.shouldPlayOnResume}")
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        // ON_STOP handles all applicable cases when we really have to pause the playback
                        if (isInPipMode) {
                            // The PIP screen overlay blocks access to buttons
                            playerView.hideController()
                            playerView.controllerAutoShow = false
                        }
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        // Generally ON_RESUME shouldn't happen under PIP mode, just to make sure
                        if (!isInPipMode) {
                            playerView.onResume()
                            playerView.controllerAutoShow = true
                            if (uiState.shouldPlayOnResume) {
                                playerView.player?.play()
                                uiEvent.onPlaybackResumed()
                            }
                        }
                    }

                    Lifecycle.Event.ON_STOP -> {
                        // On Stop is triggered without going through OnPause when:
                        // - Clicking close under PIP Mode, or
                        // - Home button is pressed, or
                        // - Switching to another app via the launcher, or
                        // - Navigating out
                        playerView.onPause()
                        if (playerView.player?.isPlaying == true) {
                            uiEvent.onRegisterPlaybackModeOnResume(true)
                            playerView.player?.pause()
                        }
                    }

                    else -> {}
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = localContext.getString(R.string.content_description_video_player) },
        )

        if (shouldShowPiPButton) {
            IconButton(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .padding(all = dimension.defaultFullPadding),
                onClick = {
                    isInPipMode = true
                    uiEvent.onTriggerPIPMode()
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.rounded_picture_in_picture_24),
                    contentDescription = stringResource(R.string.content_description_play_video_in_picture_in_picture_mode),
                )
            }
        }
    }

    LaunchedEffect(true) {
        if (!uiState.hasVideoLoaded) {
            uiEvent.onPlayVideo()
        }
    }
}
