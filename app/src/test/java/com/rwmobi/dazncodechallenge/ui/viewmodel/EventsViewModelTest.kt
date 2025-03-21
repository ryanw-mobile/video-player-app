/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
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

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `displays refreshed events when fetchCacheAndRefresh is successful`() {
        // GIVEN: The repository returns remote events (event1, event2) and local events (event3)
        // WHEN: fetchCacheAndRefresh is called
        fakeRepository.setRemoteEventsForTest(listOf(event1, event2))
        fakeRepository.setLocalEventsForTest(listOf(event3))

        viewModel.fetchCacheAndRefresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(listOf(event1, event2), uiState.events)
    }

    @Test
    fun `shows error and no events when fetchCacheAndRefresh fails`() {
        // GIVEN: The repository is set to throw an error
        // WHEN: fetchCacheAndRefresh is called
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
    fun `updates events successfully when refresh is called`() {
        // GIVEN: The repository returns remote events (event1, event2) and local events (event3)
        // WHEN: refresh is called after initial fetch
        fakeRepository.setRemoteEventsForTest(listOf(event1, event2))
        fakeRepository.setLocalEventsForTest(listOf(event3))
        viewModel.fetchCacheAndRefresh()

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(listOf(event1, event2), uiState.events)
    }

    @Test
    fun `retains cached events and shows error when refresh fails`() {
        // GIVEN: Initial data fetch and an error set for the repository
        // WHEN: refresh is called
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
    fun `displays error message when refresh fetch fails`() {
        // GIVEN: Initial fetch and an error set for the repository
        // WHEN: refresh is called
        viewModel.fetchCacheAndRefresh()
        val exceptionMessage = "Test error"
        fakeRepository.setExceptionForTest(Exception(exceptionMessage))

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(exceptionMessage, uiState.errorMessages[0].message)
    }

    @Test
    fun `accumulates error messages on multiple refresh failures`() {
        // GIVEN: Initial fetch and multiple errors set for the repository
        // WHEN: refresh is called multiple times with different errors
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
    fun `does not accumulate duplicated error messages on multiple refresh failures`() {
        // GIVEN: Initial fetch and multiple identical errors set for the repository
        // WHEN: refresh is called multiple times with the same error
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
    fun `clears error message when errorShown is called with valid id`() {
        // GIVEN: An error message present in the UI state
        // WHEN: errorShown is called with the correct error id
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
    fun `enables scroll to top when requestScrollToTop is requested`() {
        // GIVEN: Any initial state
        // WHEN: requestScrollToTop is called with true
        val expectedRequestScrollToTop = true
        viewModel.fetchCacheAndRefresh()
        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)

        val uiState = viewModel.uiState.value
        assertEquals(expectedRequestScrollToTop, uiState.requestScrollToTop)
    }

    @Test
    fun `returns correct image loader instance`() {
        // GIVEN: Any initial state
        // WHEN: getImageLoader is called
        viewModel.fetchCacheAndRefresh()
        val imageLoader = viewModel.getImageLoader()
        val expectedImageLoader = mockImageLoader
        assertSame(expectedImageLoader, imageLoader)
    }
}
