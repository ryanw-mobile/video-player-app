/*
* Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
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

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun fetchCacheAndRefresh_ShouldDisplayRefreshedSchedules_WhenSuccessful() {
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule1, schedule2))
        fakeRepository.setLocalSchedulesForTest(listOf(schedule3))

        viewModel.fetchCacheAndRefresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(listOf(schedule1, schedule2), uiState.schedules)
    }

    @Test
    fun fetchCacheAndRefresh_ShouldShowError_WhenRepositoryFails() {
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
    fun refresh_ShouldUpdateSchedulesSuccessfully_WhenCalled() {
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule1, schedule2))
        fakeRepository.setLocalSchedulesForTest(listOf(schedule3))
        viewModel.fetchCacheAndRefresh()

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(listOf(schedule1, schedule2), uiState.schedules)
    }

    @Test
    fun refresh_ShouldRetainCachedSchedulesAndShowError_OnFailure() {
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
    fun refresh_ShouldDisplayErrorMessage_OnFetchFailure() {
        viewModel.fetchCacheAndRefresh()
        val errorMessage = "Test error"
        fakeRepository.setExceptionForTest(Exception(errorMessage))

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.errorMessages.size)
        assertEquals(errorMessage, uiState.errorMessages[0].message)
    }

    @Test
    fun refresh_ShouldAccumulateErrorMessages_OnMultipleFailures() {
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
    fun refresh_ShouldNotAccumulateDuplicatedErrorMessages_OnMultipleFailures() {
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
        val expectedImageLoader = mockImageLoader
        val imageLoader = viewModel.getImageLoader()
        assertSame(expectedImageLoader, imageLoader)
    }
}
