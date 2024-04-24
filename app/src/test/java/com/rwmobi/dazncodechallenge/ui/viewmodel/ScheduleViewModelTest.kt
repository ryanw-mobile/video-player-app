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
    }

    private fun setupViewModel() {
        viewModel = ScheduleViewModel(
            repository = fakeRepository,
            imageLoader = mockImageLoader,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `Should return empty list on first initialisation`() {
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule1, schedule2))
        fakeRepository.setLocalSchedulesForTest(emptyList())

        setupViewModel()

        val uiState = viewModel.uiState.value
        uiState.isLoading shouldBe false
        uiState.schedules shouldBe emptyList()
        uiState.errorMessages.size shouldBe 0
    }

    @Test
    fun `Should fetch cached schedules on initialisation`() {
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule1, schedule2))
        fakeRepository.setLocalSchedulesForTest(listOf(schedule3))

        setupViewModel()

        val uiState = viewModel.uiState.value
        uiState.isLoading shouldBe false
        uiState.schedules shouldContainExactly listOf(schedule3)
    }

    @Test
    fun `Should return error on first initialisation when repository returned failure`() {
        val exceptionMessage = "repository error"
        fakeRepository.setExceptionForTest(IOException(exceptionMessage))

        setupViewModel()

        val uiState = viewModel.uiState.value
        uiState.isLoading shouldBe false
        uiState.schedules shouldBe null
        uiState.errorMessages.size shouldBe 1
        uiState.errorMessages[0].message shouldBe "Error getting data: $exceptionMessage"
    }

    @Test
    fun `Should fetch schedules successfully on refresh`() {
        fakeRepository.setRemoteSchedulesForTest(listOf(schedule1, schedule2))
        fakeRepository.setLocalSchedulesForTest(listOf(schedule3))
        setupViewModel()

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        uiState.isLoading shouldBe false
        uiState.schedules shouldContainExactly listOf(schedule1, schedule2)
    }

    @Test
    fun `Should return errors and keep previous cached schedules on failed refresh`() {
        fakeRepository.setLocalSchedulesForTest(listOf(schedule3))
        setupViewModel()

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
    fun `Should update UI with error message on fetch failure`() {
        setupViewModel()
        val errorMessage = "Test error"
        fakeRepository.setExceptionForTest(Exception(errorMessage))

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        uiState.errorMessages.size shouldBe 1
        uiState.errorMessages.first().message shouldBe errorMessage
    }

    @Test
    fun `Should accumulate error messages in UIState upon multiple errors`() {
        setupViewModel()
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
    fun `Should remove error message when errorShown is called with valid ID`() {
        setupViewModel()
        val errorMessage = "Test error"
        fakeRepository.setExceptionForTest(Exception(errorMessage))

        viewModel.refresh()
        val errorId = viewModel.uiState.value.errorMessages.first().id
        viewModel.errorShown(errorId)

        val uiState = viewModel.uiState.value
        uiState.errorMessages shouldBe emptyList()
    }

    @Test
    fun `Should enable scroll to top when requested`() {
        setupViewModel()
        val expectedRequestScrollToTop = true
        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = viewModel.uiState.value
        uiState.requestScrollToTop shouldBe expectedRequestScrollToTop
    }

    @Test
    fun `Should return the correct ImageLoader instance`() {
        setupViewModel()
        val expectedImageLoader = mockImageLoader
        val imageLoader = viewModel.getImageLoader()
        imageLoader shouldBeSameInstanceAs expectedImageLoader
    }
}
