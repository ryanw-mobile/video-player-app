/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.exoplayer

import android.app.Activity
import android.util.Rational
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.ui.utils.enterPIPMode
import timber.log.Timber

@OptIn(UnstableApi::class)
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

        val localContext = LocalContext.current
        val isInPipMode = uiState.isInPIPMode

        AndroidView(
            factory = { context ->
                PlayerView(context.applicationContext).also {
                    it.player = player
                    it.controllerAutoShow = false
                    it.controllerHideOnTouch = true
                }
            },
            update = {
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        if (!isInPipMode) {
                            it.onPause()
                            it.player?.pause()
                        }
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        if (!isInPipMode) {
                            it.onResume()
                        }
                    }

                    else -> {
                        Timber.d("Lifecycle event not handled: ${lifecycle.name}")
                        Unit
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = localContext.getString(R.string.content_description_video_player) },
        )

        if (!isInPipMode) {
            IconButton(
                modifier = Modifier.align(alignment = Alignment.TopEnd),
                onClick = { uiEvent.onTriggerPIPMode() },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.rounded_picture_in_picture_24),
                    contentDescription = "PIP Mode",
                )
            }
        }
    }

    LaunchedEffect(true) {
        if (!uiState.hasVideoLoaded) {
            uiEvent.onPlayVideo()
        }
    }

    val activity = LocalContext.current as Activity
    if (uiState.isInPIPMode) {
        LaunchedEffect(true) {
            activity.enterPIPMode(Rational(16, 9))
        }
    }
}
