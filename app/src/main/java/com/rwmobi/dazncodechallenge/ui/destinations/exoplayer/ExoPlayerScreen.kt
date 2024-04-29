/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.exoplayer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView

@Composable
fun ExoPlayerScreen(
    modifier: Modifier = Modifier,
    player: Player,
    uiState: ExoPlayerUIState,
    uiEvent: ExoPlayerUIEvent,
) {
    Box(
        modifier = modifier,
    ) {
        if (uiState.errorMessages.isNotEmpty()) {
            val errorMessage = remember(uiState) { uiState.errorMessages[0] }
            val errorMessageText = errorMessage.message

            LaunchedEffect(errorMessage.id) {
                uiEvent.onShowSnackbar(errorMessageText)
                uiEvent.onErrorShown(errorMessage.id)
            }
        }

        var lifecycle by remember {
            mutableStateOf(Lifecycle.Event.ON_CREATE)
        }
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

        AndroidView(
            factory = { context ->
                PlayerView(context.applicationContext).also {
                    it.player = player
                }
            },
            update = {
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                        it.player?.pause()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }

                    else -> Unit
                }
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }

    LaunchedEffect(true) {
        if (!uiState.hasVideoLoaded) {
            uiEvent.onPlayVideo()
        }
    }
}
