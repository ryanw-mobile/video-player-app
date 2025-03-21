/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.viewmodel

import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import com.rwmobi.dazncodechallenge.test.PlaybackExceptionSampleData
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class ExoPlayerViewModelTest {
    private lateinit var mockPlayer: Player
    private lateinit var listenerSlot: CapturingSlot<Player.Listener>

    // Subject under test
    private lateinit var viewModel: ExoPlayerViewModel

    @Before
    fun init() {
        mockPlayer = mockk(relaxed = true)
        listenerSlot = slot<Player.Listener>()
        every { mockPlayer.addListener(capture(listenerSlot)) } just runs
        viewModel = ExoPlayerViewModel(player = mockPlayer)
    }

    @Test
    fun uiState_initialState_ShouldBeCorrect() {
        val uiState = viewModel.uiState.value
        assertTrue(uiState.errorMessages.isEmpty())
        assertFalse(uiState.hasVideoLoaded)
    }

    @Test
    fun playVideo_withNewVideoUrl_ShouldPlayVideoAndSetLoaded() = runTest {
        val videoUrl = "http://fakevideo.com/video.mp4"
        viewModel.playVideo(videoUrl)
        val uiState = viewModel.uiState.value
        assertTrue(uiState.hasVideoLoaded)
    }

    @Test
    fun onVideoSizeChanged_ShouldUpdateUiState() = runTest {
        val expectedVideoWidth = 640
        val expectedVideoHeight = 1080
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onVideoSizeChanged(VideoSize(expectedVideoWidth, expectedVideoHeight))
        }

        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        with(viewModel.uiState.value) {
            assertEquals(expectedVideoWidth, this.videoWidth)
            assertEquals(expectedVideoHeight, this.videoHeight)
        }
    }

    @Test
    fun onPlayerError_withHTTPError_ShouldUpdateUIState() = runTest {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.invalidResponseCodeException)
        }

        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        val errorMessages = viewModel.uiState.value.errorMessages
        assertEquals(1, errorMessages.size)
        assertEquals("HTTP Error ${PlaybackExceptionSampleData.invalidResponseCodeExceptionErrorCode}", errorMessages[0].message)
    }

    @Test
    fun onPlayerError_withHttpDataSourceException_ShouldUpdateUIState() = runTest {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.httpDataSourceException)
        }

        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        val errorMessages = viewModel.uiState.value.errorMessages
        assertEquals(1, errorMessages.size)
        assertEquals(PlaybackExceptionSampleData.httpDataSourceExceptionMessage, errorMessages[0].message)
    }

    @Test
    fun onPlayerError_withOtherExceptions_ShouldUpdateUIState() = runTest {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.genericException)
        }

        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        val errorMessages = viewModel.uiState.value.errorMessages
        assertEquals(1, errorMessages.size)
        assertEquals(PlaybackExceptionSampleData.genericExceptionMessage, errorMessages[0].message)
    }

    @Test
    fun getPlayer_ShouldReturnCorrectInstance() {
        val expectedPlayer = mockPlayer
        val player = viewModel.getPlayer()
        assertSame(expectedPlayer, player)
    }

    @Test
    fun restorePlaybackState_whenPlayingBefore_ShouldResumePlaying() {
        every { mockPlayer.isPlaying } answers { true }
        viewModel.savePlaybackState()

        every { mockPlayer.isPlaying } answers { false }
        viewModel.restorePlaybackState()

        verify(exactly = 1) { mockPlayer.play() }
    }

    @Test
    fun restorePlaybackState_whenPausedBefore_ShouldNotResumePlaying() {
        every { mockPlayer.isPlaying } answers { false }
        viewModel.savePlaybackState()

        every { mockPlayer.isPlaying } answers { false }
        viewModel.restorePlaybackState()

        verify(exactly = 0) { mockPlayer.play() }
    }

    @Test
    fun setFullScreenMode_ShouldUpdateUiState() = runTest {
        viewModel.setFullScreenMode(isFullScreenMode = true)
        assertTrue(viewModel.uiState.value.isFullScreenMode)

        viewModel.setFullScreenMode(isFullScreenMode = false)
        assertFalse(viewModel.uiState.value.isFullScreenMode)
    }

    @Test
    fun refresh_ShouldAccumulateErrorMessages_OnMultipleFailures() {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.genericException)
        }
        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.ioException)
        }
        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        val uiState = viewModel.uiState.value
        assertEquals(2, uiState.errorMessages.size)
        assertEquals(PlaybackExceptionSampleData.genericExceptionMessage, uiState.errorMessages[0].message)
        assertEquals(PlaybackExceptionSampleData.ioExceptionMessage, uiState.errorMessages[1].message)
    }

    @Test
    fun refresh_ShouldNotAccumulateDuplicatedErrorMessages_OnMultipleFailures() {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.genericException)
        }

        repeat(times = 2) {
            viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")
        }

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(PlaybackExceptionSampleData.genericExceptionMessage, uiState.errorMessages[0].message)
    }

    @Test
    fun errorShown_ShouldClearErrorMessage_WhenCalledWithValidId() {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.genericException)
        }
        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        val errorId = viewModel.uiState.value.errorMessages.first().id
        viewModel.errorShown(errorId)

        val uiState = viewModel.uiState.value
        assertTrue(uiState.errorMessages.isEmpty())
    }
}
