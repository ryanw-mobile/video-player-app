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
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import java.io.IOException

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
        uiState.isLoading shouldBe false
        uiState.schedules shouldContainExactly listOf(schedule1, schedule2)
    }

    @Test
    fun fetchCacheAndRefresh_ShouldShowError_WhenRepositoryFails() {
        val exceptionMessage = "repository error"
        fakeRepository.setExceptionForTest(IOException(exceptionMessage))
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule1, schedule2))

        viewModel.fetchCacheAndRefresh()

        val uiState = viewModel.uiState.value
        uiState.isLoading shouldBe false
        uiState.schedules shouldBe null
        uiState.errorMessages.size shouldBe 1
        uiState.errorMessages[0].message shouldBe "Error getting data: $exceptionMessage"
    }

    @Test
    fun refresh_ShouldUpdateSchedulesSuccessfully_WhenCalled() {
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule1, schedule2))
        fakeRepository.setLocalSchedulesForTest(listOf(schedule3))
        viewModel.fetchCacheAndRefresh()

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        uiState.isLoading shouldBe false
        uiState.schedules shouldContainExactly listOf(schedule1, schedule2)
    }

    @Test
    fun refresh_ShouldRetainCachedSchedulesAndShowError_OnFailure() {
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule3))
        viewModel.fetchCacheAndRefresh()

        val exceptionMessage = "repository error"
        fakeRepository.setExceptionForTest(IOException(exceptionMessage))
        viewModel.refresh()

        val uiState = viewModel.uiState.value
        uiState.isLoading shouldBe false
        uiState.schedules shouldContainExactly listOf(schedule3)
        uiState.errorMessages.size shouldBe 1
        uiState.errorMessages[0].message shouldBe exceptionMessage
    }

    @Test
    fun refresh_ShouldDisplayErrorMessage_OnFetchFailure() {
        viewModel.fetchCacheAndRefresh()
        val errorMessage = "Test error"
        fakeRepository.setExceptionForTest(Exception(errorMessage))

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        uiState.errorMessages.size shouldBe 1
        uiState.errorMessages.first().message shouldBe errorMessage
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
        uiState.errorMessages.size shouldBe 2
        uiState.errorMessages[0].message shouldBe errorMessage1
        uiState.errorMessages[1].message shouldBe errorMessage2
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
        uiState.errorMessages shouldBe emptyList()
    }

    @Test
    fun requestScrollToTop_ShouldEnableScrollToTop_WhenRequested() {
        viewModel.fetchCacheAndRefresh()
        val expectedRequestScrollToTop = true
        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = viewModel.uiState.value
        uiState.requestScrollToTop shouldBe expectedRequestScrollToTop
    }

    @Test
    fun getImageLoader_ShouldReturnCorrectInstance() {
        viewModel.fetchCacheAndRefresh()
        val expectedImageLoader = mockImageLoader
        val imageLoader = viewModel.getImageLoader()
        imageLoader shouldBeSameInstanceAs expectedImageLoader
    }
}
