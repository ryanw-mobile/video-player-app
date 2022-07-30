/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.schedule

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.ryanwong.dazn.codechallenge.TestData.schedule1
import uk.ryanwong.dazn.codechallenge.TestData.schedule1Modified
import uk.ryanwong.dazn.codechallenge.TestData.schedule2
import uk.ryanwong.dazn.codechallenge.TestData.schedule3
import uk.ryanwong.dazn.codechallenge.data.repository.FakeRepository
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule
import uk.ryanwong.dazn.codechallenge.getOrAwaitValue

/***
 *  Provide testing to the ScheduleViewModel and its live data objects
 */
@ExperimentalCoroutinesApi
class ScheduleViewModelTest {
    // provide testing to the ScheduleViewModel and its live data objects
    // Subject under test
    private lateinit var scheduleViewModel: ScheduleViewModel

    private lateinit var fakeRepository: FakeRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        // We initialise a repository with no data kept inside
        // That is the state before the first API call happens to download something from the server
        fakeRepository = FakeRepository()

        scheduleViewModel = ScheduleViewModel(
            repository = fakeRepository,
            dispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun emptyViewModel_ObserveSchedule_ReturnEmptyList() {
        // GIVEN the viewModel is empty - nothing to do

        // WHEN the ViewModel observes the LiveData list during initialization
        val observedScheduleList = scheduleViewModel.listContents.getOrAwaitValue()

        // THEN LiveData observed should have an initial empty state without error
        assertThat(observedScheduleList).isEmpty()
    }

    @Test
    fun emptyViewModel_RefreshList_ReturnListUpdated() {
        // GIVEN the viewModel is empty
        scheduleViewModel.listContents.getOrAwaitValue()

        // WHEN adding a new task
        val fakedRemoteData = listOf(schedule1, schedule2, schedule3)
        fakeRepository.submitScheduleList(fakedRemoteData)
        scheduleViewModel.refreshList()

        // THEN  The LiveData is updated with the expected list
        val refreshedScheduleList = scheduleViewModel.listContents.getOrAwaitValue()
        assertThat(refreshedScheduleList).containsExactlyElementsIn(fakedRemoteData)
    }

    @Test
    fun nonEmptyViewModel_RefreshList_ReturnListUpdated() {
        // GIVEN the ViewModel holds something
        // The repository has loaded only schedule1
        val fakedInitialRemoteData = listOf(schedule1)
        fakeRepository.submitScheduleList(fakedInitialRemoteData)

        // Refresh the ViewModel and make sure the repository has schedule1 loaded
        scheduleViewModel.refreshList()

        // WHEN the remote data has changed and the ViewModel refresh() is called.
        val fakedChangedRemoteData = listOf(schedule1Modified, schedule3)
        fakeRepository.submitScheduleList(fakedChangedRemoteData)
        scheduleViewModel.refreshList()

        // THEN The LiveData is updated with the expected list
        val refreshedScheduleList = scheduleViewModel.listContents.getOrAwaitValue()
        assertThat(refreshedScheduleList).containsExactlyElementsIn(fakedChangedRemoteData)
    }

    // Show No Data live data value tests
    // This is for fragment to determine if the no data alert should be shown
    @Test
    fun emptyViewModel_RefreshNonEmptyList_ReturnShowNoDataFalse() =
        runTest {
            // GIVEN a ViewModel has nothing in in
            val initialContents = scheduleViewModel.listContents.getOrAwaitValue()

            // WHEN the remote data has changed and the ViewModel refresh() is called.
            val fakedChangedRemoteData = listOf(schedule1Modified, schedule3)
            fakeRepository.submitScheduleList(fakedChangedRemoteData)
            scheduleViewModel.refreshList()

            // THEN The LiveData value for showNoData should be false
            val showNoData = scheduleViewModel.showNoData.getOrAwaitValue()
            assertThat(showNoData).isFalse()
        }

    @Test
    fun nonEmptyViewModel_RefreshNonEmptyList_ReturnShowNoDataFalse() =
        runTest {
            // GIVEN - The ViewModel holds something
            // The repository has loaded only schedule1
            val fakedInitialRemoteData = listOf(schedule1)
            fakeRepository.submitScheduleList(fakedInitialRemoteData)

            // Refresh the ViewModel and make sure the repository has schedule1 loaded
            scheduleViewModel.refreshList()

            // WHEN the remote data has changed and the ViewModel refresh() is called.
            val fakedChangedRemoteData = listOf(schedule1Modified, schedule3)
            fakeRepository.submitScheduleList(fakedChangedRemoteData)
            scheduleViewModel.refreshList()

            // THEN The LiveData value for showNoData should be false
            val showNoData = scheduleViewModel.showNoData.getOrAwaitValue()
            assertThat(showNoData).isFalse()
        }

    @Test
    fun emptyViewModel_RefreshEmptyList_ReturnShowNoDataTrue() =
        runTest {
            // GIVEN a ViewModel has nothing in it - nothing to do

            // WHEN the remote data has changed and the ViewModel refresh() is called.
            val fakedChangedRemoteData = emptyList<Schedule>()
            fakeRepository.submitScheduleList(fakedChangedRemoteData)
            scheduleViewModel.refreshList()

            // THEN The LiveData value for showNoData should be false
            val showNoData = scheduleViewModel.showNoData.getOrAwaitValue()
            assertThat(showNoData).isTrue()
        }

    @Test
    fun nonEmptyViewModel_RefreshEmptyList_ReturnShowNoDataTrue() =
        runTest {
            // GIVEN - The ViewModel holds something
            // The repository has loaded only schedule1
            val fakedInitialRemoteData = listOf(schedule1)
            fakeRepository.submitScheduleList(fakedInitialRemoteData)

            // Refresh the ViewModel and make sure the repository has schedule1 loaded
            scheduleViewModel.refreshList()

            // WHEN the remote data has changed and the ViewModel refresh() is called.
            val fakedChangedRemoteData = emptyList<Schedule>()
            fakeRepository.submitScheduleList(fakedChangedRemoteData)
            scheduleViewModel.refreshList()

            // THEN The LiveData value for showNoData should be false
            val showNoData = scheduleViewModel.showNoData.getOrAwaitValue()
            assertThat(showNoData).isTrue()
        }

    // Negative cases tests - set remote data source to return error
    @Test
    fun repositoryError_RefreshList_ReturnErrorMessage() =
        runTest {
            // GIVEN - The the repository is set to always return error
            val errorMessage = "test error message"
            fakeRepository.setReturnError(true, errorMessage)

            // WHEN ViewModel refreshList() is called
            scheduleViewModel.refreshList()

            // THEN The ViewModel should propagate the error message passed from the repository
            val showErrorMessage = scheduleViewModel.showErrorMessage.getOrAwaitValue()
            assertThat(showErrorMessage).isEqualTo(errorMessage)
        }
}