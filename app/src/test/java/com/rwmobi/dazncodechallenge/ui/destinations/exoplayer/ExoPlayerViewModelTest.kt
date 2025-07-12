/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.exoplayer

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
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@Config(sdk = [35])
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

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `has empty error messages and video not loaded initially`() {
        val uiState = viewModel.uiState.value
        assertTrue(uiState.errorMessages.isEmpty())
        assertFalse(uiState.hasVideoLoaded)
    }

    @Test
    fun `sets hasVideoLoaded to true when new video URL is played`() = runTest {
        val videoUrl = "http://fakevideo.com/video.mp4"
        viewModel.playVideo(videoUrl)
        val uiState = viewModel.uiState.value
        assertTrue(uiState.hasVideoLoaded)
    }

    @Test
    fun `updates video width and height in UI state when video size changes`() = runTest {
        val expectedVideoWidth = 640
        val expectedVideoHeight = 1080
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onVideoSizeChanged(
                VideoSize(
                    expectedVideoWidth,
                    expectedVideoHeight,
                ),
            )
        }

        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        with(viewModel.uiState.value) {
            assertEquals(expectedVideoWidth, this.videoWidth)
            assertEquals(expectedVideoHeight, this.videoHeight)
        }
    }

    @Test
    fun `updates UI state with HTTP error message when player encounters HTTP error`() = runTest {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.invalidResponseCodeException)
        }

        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        val errorMessages = viewModel.uiState.value.errorMessages
        assertEquals(1, errorMessages.size)
        assertEquals(
            "HTTP Error ${PlaybackExceptionSampleData.invalidResponseCodeExceptionErrorCode}",
            errorMessages[0].message,
        )
    }

    @Test
    fun `updates UI state with HttpDataSourceException message when player encounters HttpDataSourceException`() = runTest {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.httpDataSourceException)
        }

        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        val errorMessages = viewModel.uiState.value.errorMessages
        assertEquals(1, errorMessages.size)
        assertEquals(
            PlaybackExceptionSampleData.httpDataSourceExceptionMessage,
            errorMessages[0].message,
        )
    }

    @Test
    fun `updates UI state with generic exception message when player encounters generic exception`() = runTest {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.genericException)
        }

        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        val errorMessages = viewModel.uiState.value.errorMessages
        assertEquals(1, errorMessages.size)
        assertEquals(
            PlaybackExceptionSampleData.genericExceptionMessage,
            errorMessages[0].message,
        )
    }

    @Test
    fun `returns the correct player instance`() {
        val expectedPlayer = mockPlayer
        val player = viewModel.getPlayer()
        assertSame(expectedPlayer, player)
    }

    @Test
    fun `resumes playing when playback was active before state restoration`() {
        every { mockPlayer.isPlaying } answers { true }
        viewModel.savePlaybackState()

        every { mockPlayer.isPlaying } answers { false }
        viewModel.restorePlaybackState()

        verify(exactly = 1) { mockPlayer.play() }
    }

    @Test
    fun `does not resume playing when playback was paused before state restoration`() {
        every { mockPlayer.isPlaying } answers { false }
        viewModel.savePlaybackState()

        every { mockPlayer.isPlaying } answers { false }
        viewModel.restorePlaybackState()

        verify(exactly = 0) { mockPlayer.play() }
    }

    @Test
    fun `updates UI state with full-screen mode when set`() = runTest {
        viewModel.setFullScreenMode(isFullScreenMode = true)
        assertTrue(viewModel.uiState.value.isFullScreenMode)

        viewModel.setFullScreenMode(isFullScreenMode = false)
        assertFalse(viewModel.uiState.value.isFullScreenMode)
    }

    @Test
    fun `accumulates error messages on multiple failures`() {
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
        assertEquals(
            PlaybackExceptionSampleData.genericExceptionMessage,
            uiState.errorMessages[0].message,
        )
        assertEquals(
            PlaybackExceptionSampleData.ioExceptionMessage,
            uiState.errorMessages[1].message,
        )
    }

    @Test
    fun `does not accumulate duplicate error messages on multiple failures`() {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.genericException)
        }

        repeat(times = 2) {
            viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")
        }

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(
            PlaybackExceptionSampleData.genericExceptionMessage,
            uiState.errorMessages[0].message,
        )
    }

    @Test
    fun `clears error message when errorShown is called with valid ID`() {
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
