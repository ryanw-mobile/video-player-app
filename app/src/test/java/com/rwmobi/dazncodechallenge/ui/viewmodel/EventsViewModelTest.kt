/*
* Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
*
*/

package com.rwmobi.dazncodechallenge.ui.viewmodel

import coil.ImageLoader
import com.rwmobi.dazncodechallenge.data.repository.FakeRepository
import com.rwmobi.dazncodechallenge.test.EventSampleData.event1
import com.rwmobi.dazncodechallenge.test.EventSampleData.event2
import com.rwmobi.dazncodechallenge.test.EventSampleData.event3
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class EventsViewModelTest {
    private lateinit var fakeRepository: FakeRepository
    private lateinit var mockImageLoader: ImageLoader

    // Subject under test
    private lateinit var viewModel: EventsViewModel

    @Before
    fun init() {
        fakeRepository = FakeRepository()
        mockImageLoader = mockk(relaxed = true)
        viewModel = EventsViewModel(
            repository = fakeRepository,
            imageLoader = mockImageLoader,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun fetchCacheAndRefresh_ShouldDisplayRefreshedEvents_WhenSuccessful() {
        fakeRepository.setRemoteEventsForTest(listOf(event1, event2))
        fakeRepository.setLocalEventsForTest(listOf(event3))

        viewModel.fetchCacheAndRefresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(listOf(event1, event2), uiState.events)
    }

    @Test
    fun fetchCacheAndRefresh_ShouldShowError_WhenRepositoryFails() {
        val exceptionMessage = "repository error"
        fakeRepository.setExceptionForTest(IOException(exceptionMessage))
        fakeRepository.setRemoteEventsForTest(listOf(event1, event2))

        viewModel.fetchCacheAndRefresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNull(uiState.events)
        assertEquals(1, uiState.errorMessages.size)
        assertEquals("Error getting data: $exceptionMessage", uiState.errorMessages[0].message)
    }

    @Test
    fun refresh_ShouldUpdateEventsSuccessfully_WhenCalled() {
        fakeRepository.setRemoteEventsForTest(listOf(event1, event2))
        fakeRepository.setLocalEventsForTest(listOf(event3))
        viewModel.fetchCacheAndRefresh()

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(listOf(event1, event2), uiState.events)
    }

    @Test
    fun refresh_ShouldRetainCachedEventsAndShowError_OnFailure() {
        fakeRepository.setRemoteEventsForTest(listOf(event3))
        viewModel.fetchCacheAndRefresh()

        val exceptionMessage = "repository error"
        fakeRepository.setExceptionForTest(IOException(exceptionMessage))
        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(listOf(event3), uiState.events)
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(exceptionMessage, uiState.errorMessages[0].message)
    }

    @Test
    fun refresh_ShouldDisplayErrorMessage_OnFetchFailure() {
        viewModel.fetchCacheAndRefresh()
        val exceptionMessage = "Test error"
        fakeRepository.setExceptionForTest(Exception(exceptionMessage))

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(exceptionMessage, uiState.errorMessages[0].message)
    }

    @Test
    fun refresh_ShouldAccumulateErrorMessages_OnMultipleFailures() {
        viewModel.fetchCacheAndRefresh()
        val exceptionMessage1 = "Test error 1"
        val exceptionMessage2 = "Test error 2"

        fakeRepository.setExceptionForTest(Exception(exceptionMessage1))
        viewModel.refresh()
        fakeRepository.setExceptionForTest(Exception(exceptionMessage2))
        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertEquals(2, uiState.errorMessages.size)
        assertEquals(exceptionMessage1, uiState.errorMessages[0].message)
        assertEquals(exceptionMessage2, uiState.errorMessages[1].message)
    }

    @Test
    fun refresh_ShouldNotAccumulateDuplicatedErrorMessages_OnMultipleFailures() {
        viewModel.fetchCacheAndRefresh()
        val exceptionMessage = "Test error 1"

        repeat(times = 2) {
            fakeRepository.setExceptionForTest(Exception(exceptionMessage))
            viewModel.refresh()
        }

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(exceptionMessage, uiState.errorMessages[0].message)
    }

    @Test
    fun errorShown_ShouldClearErrorMessage_WhenCalledWithValidId() {
        viewModel.fetchCacheAndRefresh()
        val errorMessage = "Test error"
        fakeRepository.setExceptionForTest(Exception(errorMessage))

        viewModel.refresh()
        val errorId = viewModel.uiState.value.errorMessages.first().id
        viewModel.errorShown(errorId)

        val uiState = viewModel.uiState.value
        assertTrue(uiState.errorMessages.isEmpty())
    }

    @Test
    fun requestScrollToTop_ShouldEnableScrollToTop_WhenRequested() {
        val expectedRequestScrollToTop = true
        viewModel.fetchCacheAndRefresh()
        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)

        val uiState = viewModel.uiState.value
        assertEquals(expectedRequestScrollToTop, uiState.requestScrollToTop)
    }

    @Test
    fun getImageLoader_ShouldReturnCorrectInstance() {
        viewModel.fetchCacheAndRefresh()
        val imageLoader = viewModel.getImageLoader()
        val expectedImageLoader = mockImageLoader
        assertSame(expectedImageLoader, imageLoader)
    }
}
