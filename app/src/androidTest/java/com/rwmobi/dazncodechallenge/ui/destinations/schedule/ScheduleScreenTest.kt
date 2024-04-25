/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.schedule

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
internal class ScheduleScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var mainActivityTestRobot: MainActivityTestRobot
    private lateinit var scheduleScreenTestRobot: ScheduleScreenTestRobot

    @Before
    fun setUp() {
        runTest {
            hiltRule.inject()
            mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
            scheduleScreenTestRobot = ScheduleScreenTestRobot(composeTestRule)
        }
    }

    @Test
    fun scheduleJourneyTest() = runTest {
        with(mainActivityTestRobot) {
            tapNavigationSchedule()
            assertScheduleTabIsSelected()
        }

        // initialise both local and remote data source have no data to return

        with(scheduleScreenTestRobot) {
            // check no data screen is displayed
            // check list is not displayed
            // set remote data source has data
            // wait 31 seconds, check if schedule is refreshed
            // check no data screen is not displayed
            // check list is displayed
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
