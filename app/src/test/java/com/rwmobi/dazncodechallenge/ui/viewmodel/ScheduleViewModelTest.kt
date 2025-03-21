/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.viewmodel

import coil.ImageLoader
import com.rwmobi.dazncodechallenge.data.repository.FakeRepository
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule1
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule2
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule3
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
internal class ScheduleViewModelTest {
    private lateinit var fakeRepository: FakeRepository
    private lateinit var mockImageLoader: ImageLoader

    // Subject under test
    private lateinit var viewModel: ScheduleViewModel

    @Before
    fun init() {
        fakeRepository = FakeRepository()
        mockImageLoader = mockk(relaxed = true)
        viewModel = ScheduleViewModel(
            repository = fakeRepository,
            imageLoader = mockImageLoader,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `displays refreshed schedules when fetchCacheAndRefresh is successful`() {
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule1, schedule2))
        fakeRepository.setLocalSchedulesForTest(listOf(schedule3))

        viewModel.fetchCacheAndRefresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(listOf(schedule1, schedule2), uiState.schedules)
    }

    @Test
    fun `shows error when fetchCacheAndRefresh fails`() {
        val exceptionMessage = "repository error"
        fakeRepository.setExceptionForTest(IOException(exceptionMessage))
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule1, schedule2))

        viewModel.fetchCacheAndRefresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNull(uiState.schedules)
        assertEquals(1, uiState.errorMessages.size)
        assertEquals("Error getting data: $exceptionMessage", uiState.errorMessages[0].message)
    }

    @Test
    fun `updates schedules successfully when refresh is called`() {
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule1, schedule2))
        fakeRepository.setLocalSchedulesForTest(listOf(schedule3))
        viewModel.fetchCacheAndRefresh()

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(listOf(schedule1, schedule2), uiState.schedules)
    }

    @Test
    fun `retains cached schedules and shows error when refresh fails`() {
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule3))
        viewModel.fetchCacheAndRefresh()

        val exceptionMessage = "repository error"
        fakeRepository.setExceptionForTest(IOException(exceptionMessage))
        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(listOf(schedule3), uiState.schedules)
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(exceptionMessage, uiState.errorMessages[0].message)
    }

    @Test
    fun `displays error message when refresh fetch fails`() {
        viewModel.fetchCacheAndRefresh()
        val errorMessage = "Test error"
        fakeRepository.setExceptionForTest(Exception(errorMessage))

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(errorMessage, uiState.errorMessages[0].message)
    }

    @Test
    fun `accumulates error messages when multiple refresh calls fail`() {
        viewModel.fetchCacheAndRefresh()
        val errorMessage1 = "Test error 1"
        val errorMessage2 = "Test error 2"

        fakeRepository.setExceptionForTest(Exception(errorMessage1))
        viewModel.refresh()
        fakeRepository.setExceptionForTest(Exception(errorMessage2))
        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertEquals(2, uiState.errorMessages.size)
        assertEquals(errorMessage1, uiState.errorMessages[0].message)
        assertEquals(errorMessage2, uiState.errorMessages[1].message)
    }

    @Test
    fun `does not accumulate duplicated error messages when multiple refresh calls fail`() {
        viewModel.fetchCacheAndRefresh()
        val errorMessage1 = "Test error 1"

        repeat(times = 2) {
            fakeRepository.setExceptionForTest(Exception(errorMessage1))
            viewModel.refresh()
        }

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(errorMessage1, uiState.errorMessages[0].message)
    }

    @Test
    fun `clears error message when errorShown is called with valid ID`() {
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
    fun `enables scroll to top when requestScrollToTop is called`() {
        val expectedRequestScrollToTop = true
        viewModel.fetchCacheAndRefresh()
        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)

        val uiState = viewModel.uiState.value
        assertEquals(expectedRequestScrollToTop, uiState.requestScrollToTop)
    }

    @Test
    fun `returns correct imageLoader instance when getImageLoader is called`() {
        viewModel.fetchCacheAndRefresh()
        val expectedImageLoader = mockImageLoader
        val imageLoader = viewModel.getImageLoader()
        assertSame(expectedImageLoader, imageLoader)
    }
}
