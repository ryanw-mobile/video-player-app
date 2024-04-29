package com.rwmobi.dazncodechallenge.ui.viewmodel

import androidx.media3.common.Player
import com.rwmobi.dazncodechallenge.test.PlaybackExceptionSampleData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
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
        assert(uiState.errorMessages.isEmpty())
        assert(!uiState.hasVideoLoaded)
    }

    @Test
    fun playVideo_withNewVideoUrl_ShouldPlayVideoAndSetLoaded() = runTest {
        val videoUrl = "http://fakevideo.com/video.mp4"
        viewModel.playVideo(videoUrl)
        val uiState = viewModel.uiState.value
        uiState.hasVideoLoaded shouldBe true
    }

    @Test
    fun onPlayerError_withHTTPError_ShouldUpdateUIState() = runTest {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.invalidResponseCodeException)
        }

        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        val errorMessages = viewModel.uiState.value.errorMessages
        errorMessages.size shouldBe 1
        errorMessages[0].message shouldBe "HTTP Error ${PlaybackExceptionSampleData.invalidResponseCodeExceptionErrorCode}"
    }

    @Test
    fun onPlayerError_withHttpDataSourceException_ShouldUpdateUIState() = runTest {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.httpDataSourceException)
        }

        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        val errorMessages = viewModel.uiState.value.errorMessages
        errorMessages.size shouldBe 1
        errorMessages[0].message shouldBe PlaybackExceptionSampleData.httpDataSourceExceptionMessage
    }

    @Test
    fun onPlayerError_withOtherExceptions_ShouldUpdateUIState() = runTest {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.genericException)
        }

        viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")

        val errorMessages = viewModel.uiState.value.errorMessages
        errorMessages.size shouldBe 1
        errorMessages[0].message shouldBe PlaybackExceptionSampleData.genericExceptionMessage
    }

    @Test
    fun getPlayer_ShouldReturnCorrectInstance() {
        val expectedPlayer = mockPlayer
        val player = viewModel.getPlayer()
        player shouldBeSameInstanceAs expectedPlayer
    }

    @Test
    fun refresh_ShouldAccumulateErrorMessages_OnMultipleFailures() {
        every { mockPlayer.play() } answers {
            listenerSlot.captured.onPlayerError(PlaybackExceptionSampleData.genericException)
        }

        repeat(times = 2) {
            viewModel.playVideo(videoUrl = "http://somehost.com/someview.mp4")
        }

        val uiState = viewModel.uiState.value
        uiState.errorMessages.size shouldBe 2
        uiState.errorMessages[0].message shouldBe PlaybackExceptionSampleData.genericExceptionMessage
        uiState.errorMessages[1].message shouldBe PlaybackExceptionSampleData.genericExceptionMessage
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
        uiState.errorMessages shouldBe emptyList()
    }
}