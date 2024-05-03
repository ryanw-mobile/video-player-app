/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.viewmodel

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.HttpDataSource
import com.rwmobi.dazncodechallenge.ui.destinations.exoplayer.ExoPlayerUIState
import com.rwmobi.dazncodechallenge.ui.model.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
@OptIn(UnstableApi::class)
class ExoPlayerViewModel @Inject constructor(
    private val player: Player,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ExoPlayerUIState> = MutableStateFlow(ExoPlayerUIState())
    val uiState = _uiState.asStateFlow()

    private var videoUrl: String? = null
    private var isPlayerPausedForResume = false

    init {
        player.prepare()
        player.addListener(
            object : Player.Listener {
                override fun onVideoSizeChanged(videoSize: VideoSize) {
                    super.onVideoSizeChanged(videoSize)
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            videoWidth = videoSize.width,
                            videoHeight = videoSize.height,
                        )
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    val cause = error.cause
                    if (cause is HttpDataSource.HttpDataSourceException) {
                        // An HTTP error occurred.
                        val httpError = cause
                        // It's possible to find out more about the error both by casting and by querying
                        // the cause.
                        if (httpError is HttpDataSource.InvalidResponseCodeException) {
                            // Cast to InvalidResponseCodeException and retrieve the response code, message
                            // and headers.
                            updateUIForError(message = "HTTP Error ${httpError.responseCode}")
                        } else {
                            // Try calling httpError.getCause() to retrieve the underlying cause, although
                            // note that it may be null.
                            httpError.cause?.message?.let { message ->
                                updateUIForError(message = message)
                            }
                        }
                    } else {
                        cause?.message?.let { message ->
                            updateUIForError(message = message)
                        }
                    }
                }
            },
        )
    }

    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    fun getPlayer() = player

    fun playVideo(videoUrl: String) {
        // Intended to resume playing position when device is rotated
        if (!_uiState.value.hasVideoLoaded || videoUrl != this.videoUrl) {
            player.apply {
                setMediaItem(
                    MediaItem.fromUri(videoUrl),
                )
                play()
            }

            _uiState.update { currentUiState ->
                currentUiState.copy(hasVideoLoaded = true)
            }
        }
    }

    fun savePlaybackState() {
        isPlayerPausedForResume = player.isPlaying
        player.pause()
    }

    fun restorePlaybackState() {
        if (isPlayerPausedForResume) {
            if (!player.isPlaying) {
                player.play()
            }
            isPlayerPausedForResume = false
        }
    }

    fun setFullScreenMode(isFullScreenMode: Boolean) {
        _uiState.update { currentUiState ->
            currentUiState.copy(isFullScreenMode = isFullScreenMode)
        }
    }

    private fun updateUIForError(message: String) {
        _uiState.update {
            addErrorMessage(
                currentUiState = it,
                message = message,
            )
        }
    }

    private fun addErrorMessage(currentUiState: ExoPlayerUIState, message: String): ExoPlayerUIState {
        val newErrorMessage = ErrorMessage(
            id = UUID.randomUUID().mostSignificantBits,
            message = message,
        )
        return currentUiState.copy(
            errorMessages = currentUiState.errorMessages + newErrorMessage,
        )
    }
}
