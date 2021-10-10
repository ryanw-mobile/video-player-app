/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.events

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import uk.ryanwong.dazn.codechallenge.R
import uk.ryanwong.dazn.codechallenge.ServiceLocator
import uk.ryanwong.dazn.codechallenge.TestData.event1
import uk.ryanwong.dazn.codechallenge.TestData.event2
import uk.ryanwong.dazn.codechallenge.TestData.event3
import uk.ryanwong.dazn.codechallenge.data.repository.FakeRepository

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class EventsFragmentTest {
    private lateinit var repository: FakeRepository

    @Before
    fun initRepository() {
        // For testing we inject a fake repository at the ServiceLocator for test doubles
        repository = FakeRepository()
        ServiceLocator.baseRepository = repository
    }

    @Test
    fun clickEvent_NavigateToExoplayerActivity() = runBlockingTest {
        // GIVEN - Load the Events Fragment with three events
        repository.submitEventList(listOf(event1, event2, event3))
        repository.refreshEvents()
        val scenario =
            launchFragmentInContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

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
    fun repositoryEmpty_ShowNoData() = runBlockingTest {
        // GIVEN - Repository has no events to supply
        repository.submitEventList(emptyList())
        repository.refreshEvents()

        // WHEN - Launching the fragment
        launchFragmentInContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

        // THEN - noDataTextView is shown
        Espresso.onView(withId(R.id.textview_nodata))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun repositoryNonEmpty_HiddenNoData() = runBlockingTest {
        // GIVEN - Repository has 1 event
        repository.submitEventList(listOf(event1))
        repository.refreshEvents()

        // WHEN - Launching the fragment
        launchFragmentInContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

        // THEN - noDataTextView is NOT shown
        Espresso.onView(withId(R.id.textview_nodata))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun repositoryError_ShowErrorDialog() = runBlockingTest {
        // GIVEN - the repository is set to always return error
        val errorMessage = "Instrumentation test error"
        repository.setReturnError(true, errorMessage)

        // WHEN - Launching the fragment
        launchFragmentInContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

        // THEN - error dialog is shown
        // Expects reminder to be saved successfully
        Espresso.onView(withText(R.string.something_went_wrong))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }
}