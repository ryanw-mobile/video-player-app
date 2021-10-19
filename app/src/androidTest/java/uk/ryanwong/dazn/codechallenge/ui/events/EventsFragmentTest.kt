/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.events

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import uk.ryanwong.dazn.codechallenge.MainCoroutineRule
import uk.ryanwong.dazn.codechallenge.R
import uk.ryanwong.dazn.codechallenge.TestData.event1
import uk.ryanwong.dazn.codechallenge.TestData.event2
import uk.ryanwong.dazn.codechallenge.TestData.event3
import uk.ryanwong.dazn.codechallenge.base.BaseRepository
import uk.ryanwong.dazn.codechallenge.data.repository.FakeRepository
import uk.ryanwong.dazn.codechallenge.launchFragmentInHiltContainer
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class EventsFragmentTest {
    @Inject
    lateinit var repository: BaseRepository

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun clickEvent_NavigateToExoplayerActivity() = mainCoroutineRule.runBlockingTest {
        // GIVEN - Load the Events Fragment with three events
        (repository as FakeRepository).submitEventList(listOf(event1, event2, event3))
        repository.refreshEvents()

        // Note: Originally we use launchFragmentInContainer
        // But due to library bugs, we use launchFragmentInHiltContainer
        // See HiltExt.kt for details

        val scenario =
            launchFragmentInHiltContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

        val navController = Mockito.mock(NavController::class.java)
//        scenario.onFragment {
//            Navigation.setViewNavController(it.view!!, navController)
//        }

        // WHEN - Click on the event1
        Espresso.onView(withId(R.id.recyclerview))
            // Using ViewMatchers to locate the item with event1.title on the RecyclerView
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText(event1.title)),
                    ViewActions.click()
                )
            )

        // THEN - Verify that we navigate to the player screen
        Mockito.verify(navController).navigate(
            EventsFragmentDirections.actionNavigationEventsToExoplayerActivity(event1.videoUrl)
        )
    }

    @Test
    fun repositoryEmpty_ShowNoData() = mainCoroutineRule.runBlockingTest {
        // GIVEN - Repository has no events to supply
        (repository as FakeRepository).submitEventList(emptyList())
        repository.refreshEvents()

        // WHEN - Launching the fragment
        launchFragmentInHiltContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

        // THEN - noDataTextView is shown
        Espresso.onView(withId(R.id.textview_nodata))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun repositoryNonEmpty_HiddenNoData() = mainCoroutineRule.runBlockingTest {
        // GIVEN - Repository has 1 event
        (repository as FakeRepository).submitEventList(listOf(event1))
        repository.refreshEvents()

        // WHEN - Launching the fragment
        launchFragmentInHiltContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

        // THEN - noDataTextView is NOT shown
        Espresso.onView(withId(R.id.textview_nodata))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun repositoryError_ShowErrorDialog() = mainCoroutineRule.runBlockingTest {
        // GIVEN - the repository is set to always return error
        val errorMessage = "Instrumentation test error"
        (repository as FakeRepository).setReturnError(true, errorMessage)

        // WHEN - Launching the fragment
        launchFragmentInHiltContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

        // THEN - error dialog is shown
        // Expects reminder to be saved successfully
        Espresso.onView(withText(R.string.something_went_wrong))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}