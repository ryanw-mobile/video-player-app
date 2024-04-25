/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.events

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rwmobi.dazncodechallenge.MainActivity
import com.rwmobi.dazncodechallenge.ui.MainActivityTestRobot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@kotlinx.coroutines.ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class EventsScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var mainActivityTestRobot: MainActivityTestRobot
    private lateinit var eventsScreenTestRobot: EventsScreenTestRobot

    @Before
    fun setUp() {
        runTest {
            hiltRule.inject()
            mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
            eventsScreenTestRobot = EventsScreenTestRobot(composeTestRule)
        }
    }

    @Test
    fun eventsJourneyTest() = runTest {
        with(mainActivityTestRobot) {
            tapNavigationEvents()
            assertEventsTabIsSelected()
        }

        // initialise both local and remote data source have no data to return

        with(eventsScreenTestRobot) {
            // check no data screen is displayed
            // check list is not displayed
            // set remote data source has data
            // pull to refresh (how?)
            // check no data screen is not displayed
            // check list is displayed
            // click on an event
            // check if exoplayer is opened
            // back
            // check the list is displayed
        }
    }

//    @Test
//    fun repositoryError_ShowErrorDialog() =
//        runTest {
//            // GIVEN - the repository is set to always return error
//            val errorMessage = "Instrumentation test error"
//            (repository as FakeRepository).setReturnError(true, errorMessage)
//
//            // WHEN - Launching the fragment
//            launchFragmentInHiltContainer<ScheduleFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)
//
//            // THEN - error dialog is shown
//            // Expects reminder to be saved successfully
//            Espresso.onView(withText(R.string.something_went_wrong))
//                .check(matches(isDisplayed()))
//        }
}
