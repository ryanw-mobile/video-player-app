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

    @Test
    fun `Should return refreshed events on fetchCacheAndRefresh`() {
        fakeRepository.setRemoteEventsForTest(listOf(event1, event2))
        fakeRepository.setLocalEventsForTest(listOf(event3))

        viewModel.fetchCacheAndRefresh()

        val uiState = viewModel.uiState.value
        uiState.isLoading shouldBe false
        uiState.events shouldContainExactly listOf(event1, event2)
    }

    @Test
    fun `Should return error without refresh on fetchCacheAndReload when fetching cache and repository returned failure`() {
        val exceptionMessage = "repository error"
        fakeRepository.setExceptionForTest(IOException(exceptionMessage))
        fakeRepository.setRemoteEventsForTest(listOf(event1, event2))

        viewModel.fetchCacheAndRefresh()

        val uiState = viewModel.uiState.value
        uiState.isLoading shouldBe false
        uiState.events shouldBe null
        uiState.errorMessages.size shouldBe 1
        uiState.errorMessages[0].message shouldBe "Error getting data: $exceptionMessage"
    }

    @Test
    fun `Should fetch events successfully on refresh`() {
        fakeRepository.setRemoteEventsForTest(listOf(event1, event2))
        fakeRepository.setLocalEventsForTest(listOf(event3))
        viewModel.fetchCacheAndRefresh()

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        uiState.isLoading shouldBe false
        uiState.events shouldContainExactly listOf(event1, event2)
    }

    @Test
    fun `Should return errors and keep previous cached events on failed refresh`() {
        fakeRepository.setRemoteEventsForTest(listOf(event3))
        viewModel.fetchCacheAndRefresh()

        val exceptionMessage = "repository error"
        fakeRepository.setExceptionForTest(IOException(exceptionMessage))
        viewModel.refresh()

        val uiState = viewModel.uiState.value
        uiState.isLoading shouldBe false
        uiState.events shouldContainExactly listOf(event3)
        uiState.errorMessages.size shouldBe 1
        uiState.errorMessages[0].message shouldBe exceptionMessage
    }

    @Test
    fun `Should update UI with error message on fetch failure`() {
        viewModel.fetchCacheAndRefresh()
        val errorMessage = "Test error"
        fakeRepository.setExceptionForTest(Exception(errorMessage))

        viewModel.refresh()

        val uiState = viewModel.uiState.value
        uiState.errorMessages.size shouldBe 1
        uiState.errorMessages.first().message shouldBe errorMessage
    }

    @Test
    fun `Should accumulate error messages in UIState upon multiple errors`() {
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
    fun `Should remove error message when errorShown is called with valid ID`() {
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
    fun `Should enable scroll to top when requested`() {
        viewModel.fetchCacheAndRefresh()
        val expectedRequestScrollToTop = true
        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = viewModel.uiState.value
        uiState.requestScrollToTop shouldBe expectedRequestScrollToTop
    }

    @Test
    fun `Should return the correct ImageLoader instance`() {
        viewModel.fetchCacheAndRefresh()
        val expectedImageLoader = mockImageLoader
        val imageLoader = viewModel.getImageLoader()
        imageLoader shouldBeSameInstanceAs expectedImageLoader
    }
}
