/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.exoplayer

import android.app.PictureInPictureParams
import android.graphics.Rect
import android.view.View
import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.ui.components.PictureInPictureButton
import com.rwmobi.dazncodechallenge.ui.model.ControllerTransitionState
import com.rwmobi.dazncodechallenge.ui.theme.VideoPlayerAppTheme
import com.rwmobi.dazncodechallenge.ui.utils.enterFullScreenMode
import com.rwmobi.dazncodechallenge.ui.utils.exitFullScreenMode

@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerScreen(
    modifier: Modifier = Modifier,
    shouldShowPiPButton: Boolean,
    player: Player,
    uiState: ExoPlayerUIState,
    uiEvent: ExoPlayerUIEvent,
) {
    val localContext = LocalContext.current
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

    val activity = LocalActivity.current
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

    var controllerVisibility by remember { mutableStateOf(ControllerTransitionState.GONE) }

    Box(
        modifier = modifier,
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = localContext.getString(R.string.content_description_video_player) },
            factory = { context ->
                PlayerView(context.applicationContext).apply {
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                    controllerAutoShow = false
                    setFullscreenButtonClickListener { isFullScreen -> uiEvent.onToggleFullScreenMode(isFullScreen) }
                    setShowNextButton(false)
                    setControllerVisibilityListener(
                        PlayerView.ControllerVisibilityListener { visibility ->
                            controllerVisibility = controllerVisibility.updateState(
                                visibility = visibility,
                                isControllerFullyVisible = isControllerFullyVisible,
                            )
                        },
                    )
                    addOnLayoutChangeListener { v: View?, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int, newLeft: Int, newTop: Int, newRight: Int, newBottom: Int ->
                        val sourceRectHint = Rect()
                        getGlobalVisibleRect(sourceRectHint)
                        val builder = PictureInPictureParams.Builder()
                            .setSourceRectHint(sourceRectHint)
                        activity?.setPictureInPictureParams(builder.build())
                    }
                    this.player = player
                    player.playWhenReady = true
                    controllerAutoShow = false
                }
            },
            update = { playerView ->
                when (lifecycle) {
                    /**
                     * In Android 7.0 and later, you should pause and resume video playback when the system calls your activity's onStop() and onStart(). By doing this, you can avoid having to check if your app is in PiP mode in onPause() and explicitly continuing playback.
                     */
                    Lifecycle.Event.ON_PAUSE -> {
                        playerView.hideController()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        //  ON_RESUME shouldn't happen under PIP mode
                        playerView.onResume()
                        uiEvent.onRestorePlaybackState()
                    }

                    Lifecycle.Event.ON_STOP -> {
                        playerView.onPause()
                        uiEvent.onSavePlaybackState()
                    }

                    else -> {}
                }
            },
        )

        AnimatedVisibility(
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .padding(all = VideoPlayerAppTheme.dimens.defaultFullPadding),
            visible = shouldShowPiPButton && (controllerVisibility == ControllerTransitionState.VISIBLE || controllerVisibility == ControllerTransitionState.APPEARING),
            enter = slideInVertically(),
            exit = slideOutVertically(),
        ) {
            PictureInPictureButton(
                onClick = {
                    controllerVisibility = ControllerTransitionState.DISAPPEARING
                    uiEvent.onEnterPictureInPictureMode()
                },
            )
        }
    }

    LaunchedEffect(true) {
        if (!uiState.hasVideoLoaded) {
            uiEvent.onPlayVideo()
        }
    }
}
