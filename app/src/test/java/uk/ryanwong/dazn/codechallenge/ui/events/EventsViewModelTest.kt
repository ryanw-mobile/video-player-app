/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.events

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.ryanwong.dazn.codechallenge.TestData.event1
import uk.ryanwong.dazn.codechallenge.TestData.event1Modified
import uk.ryanwong.dazn.codechallenge.TestData.event2
import uk.ryanwong.dazn.codechallenge.TestData.event3
import uk.ryanwong.dazn.codechallenge.data.repository.FakeRepository
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.getOrAwaitValue

/***
 *  Provide testing to the EventsViewModel and its live data objects
 */
@ExperimentalCoroutinesApi
class EventsViewModelTest {

    // Subject under test
    private lateinit var eventsViewModel: EventsViewModel

    private lateinit var fakeRepository: FakeRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        // We initialise a repository with no data kept inside
        // That is the state before the first API call happens to download something from the server
        fakeRepository = FakeRepository()

        // Given a fresh ViewModel
        eventsViewModel = EventsViewModel(
            repository = fakeRepository,
            dispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun emptyViewModel_ObserveEvents_ReturnEmptyList() {
        // GIVEN the viewModel is empty -- nothing to do

        // WHEN the ViewModel observes the LiveData list during initialization
        val observedEventsList = eventsViewModel.listContents.getOrAwaitValue()

        // THEN LiveData observed should have an initial empty state without error
        assertThat(observedEventsList).isEmpty()
    }

    @Test
    fun emptyViewModel_RefreshList_ReturnListUpdated() {
        // GIVEN the viewModel is empty -- nothing to do

        // WHEN adding a new task
        val fakedRemoteData = listOf(event1, event2, event3)
        fakeRepository.submitEventList(fakedRemoteData)
        eventsViewModel.refreshList()

        // THEN  The LiveData is updated with the expected list
        val refreshedEventsList = eventsViewModel.listContents.getOrAwaitValue()
        assertThat(refreshedEventsList).containsExactlyElementsIn(fakedRemoteData)
    }

    @Test
    fun nonEmptyViewModel_RefreshList_ReturnListUpdated() {
        // GIVEN the ViewModel holds something
        // The repository has loaded only event1
        val fakedInitialRemoteData = listOf(event1)
        fakeRepository.submitEventList(fakedInitialRemoteData)

        // Refresh the ViewModel and make sure the repository has event1 loaded
        eventsViewModel.refreshList()

        // WHEN the remote data has changed and the ViewModel refresh() is called.
        val fakedChangedRemoteData = listOf(event1Modified, event3)
        fakeRepository.submitEventList(fakedChangedRemoteData)
        eventsViewModel.refreshList()

        // THEN The LiveData is updated with the expected list
        val refreshedEventsList = eventsViewModel.listContents.getOrAwaitValue()
        assertThat(refreshedEventsList).containsExactlyElementsIn(fakedChangedRemoteData)
    }

    // Show No Data live data value tests
    // This is for fragment to determine if the no data alert should be shown
    @Test
    fun emptyViewModel_RefreshNonEmptyList_ReturnShowNoDataFalse() {
        // GIVEN a ViewModel has nothing in it -- nothing to do

        // WHEN the remote data has changed and the ViewModel refresh() is called.
        val fakedChangedRemoteData = listOf(event1Modified, event3)
        fakeRepository.submitEventList(fakedChangedRemoteData)
        eventsViewModel.refreshList()

        // THEN The LiveData value for showNoData should be false
        val showNoData = eventsViewModel.showNoData.getOrAwaitValue()
        assertThat(showNoData).isFalse()
    }

    @Test
    fun nonEmptyViewModel_RefreshNonEmptyList_ReturnShowNoDataFalse() {
        // GIVEN - The ViewModel holds something
        // The repository has loaded only event1
        val fakedInitialRemoteData = listOf(event1)
        fakeRepository.submitEventList(fakedInitialRemoteData)

        // Refresh the ViewModel and make sure the repository has event1 loaded
        eventsViewModel.refreshList()

        // WHEN the remote data has changed and the ViewModel refresh() is called.
        val fakedChangedRemoteData = listOf(event1Modified, event3)
        fakeRepository.submitEventList(fakedChangedRemoteData)
        eventsViewModel.refreshList()

        // THEN The LiveData value for showNoData should be false
        val showNoData = eventsViewModel.showNoData.getOrAwaitValue()
        assertThat(showNoData).isFalse()
    }

    @Test
    fun emptyViewModel_RefreshEmptyList_ReturnShowNoDataTrue() {
        // GIVEN a ViewModel has nothing in it - nothing to do

        // WHEN the remote data has changed and the ViewModel refresh() is called.
        val fakedChangedRemoteData = emptyList<Event>()
        fakeRepository.submitEventList(fakedChangedRemoteData)
        eventsViewModel.refreshList()

        // THEN The LiveData value for showNoData should be false
        val showNoData = eventsViewModel.showNoData.getOrAwaitValue()
        assertThat(showNoData).isTrue()
    }

    @Test
    fun nonEmptyViewModel_RefreshEmptyList_ReturnShowNoDataTrue() {
        // GIVEN - The ViewModel holds something
        // The repository has loaded only event1
        val fakedInitialRemoteData = listOf(event1)
        fakeRepository.submitEventList(fakedInitialRemoteData)

        // Refresh the ViewModel and make sure the repository has event1 loaded
        eventsViewModel.refreshList()

        // WHEN the remote data has changed and the ViewModel refresh() is called.
        val fakedChangedRemoteData = emptyList<Event>()
        fakeRepository.submitEventList(fakedChangedRemoteData)
        eventsViewModel.refreshList()

        // THEN The LiveData value for showNoData should be false
        val showNoData = eventsViewModel.showNoData.getOrAwaitValue()
        assertThat(showNoData).isTrue()
    }

    // Negative cases tests - set remote data source to return error
    @Test
    fun repositoryError_RefreshList_ReturnErrorMessage() {
        // GIVEN - The the repository is set to always return error
        val errorMessage = "test error message"
        fakeRepository.setReturnError(true, errorMessage)

        // WHEN ViewModel refreshList() is called
        eventsViewModel.refreshList()

        // THEN The ViewModel should propagate the error message passed from the repository
        val showErrorMessage = eventsViewModel.showErrorMessage.getOrAwaitValue()
        assertThat(showErrorMessage).isEqualTo(errorMessage)
    }
}