/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.exoplayer

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.media3.ui.PlayerView
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.ui.theme.getDimension
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
    var isInPipMode by remember { mutableStateOf(isInPictureInPictureMode) }

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

    Box(
        modifier = modifier,
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context.applicationContext).also {
                    it.player = player
                }
            },
            update = {
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        if (!isInPipMode) {
                            it.onPause()
                            it.player?.pause()
                        } else {
                            // The PIP screen overlay blocks access to buttons
                            it.hideController()
                            it.controllerAutoShow = false
                        }
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        if (!isInPipMode) {
                            it.onResume()
                            it.controllerAutoShow = true
                        }
                    }

                    else -> {
                        Timber.v("Lifecycle event not handled by PlayerView: ${lifecycle.name}")
                    }
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
