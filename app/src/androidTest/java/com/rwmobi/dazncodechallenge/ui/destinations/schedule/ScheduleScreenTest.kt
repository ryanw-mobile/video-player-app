/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.schedule

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rwmobi.dazncodechallenge.MainActivity
import com.rwmobi.dazncodechallenge.data.repository.FakeUITestRepository
import com.rwmobi.dazncodechallenge.domain.repository.Repository
import com.rwmobi.dazncodechallenge.ui.MainActivityTestRobot
import com.rwmobi.dazncodechallenge.ui.test.ScheduleListSampleData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import javax.inject.Inject

@kotlinx.coroutines.ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class ScheduleScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var repository: Repository

    private lateinit var mainActivityTestRobot: MainActivityTestRobot
    private lateinit var scheduleScreenTestRobot: ScheduleScreenTestRobot
    private lateinit var fakeUITestRepository: FakeUITestRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
        scheduleScreenTestRobot = ScheduleScreenTestRobot(composeTestRule)
        fakeUITestRepository = repository as? FakeUITestRepository
            ?: throw IllegalStateException("Injected repository is not a FakeUITestRepository")
    }

    @Test
    fun scheduleJourneyTest() = runTest {
        with(scheduleScreenTestRobot) {
            // Switch to the Schedule Tab - ensure it is not a second tap
            fakeUITestRepository.setRemoteSchedulesForTest(schedule = emptyList())
            mainActivityTestRobot.tapNavigationEvents()
            mainActivityTestRobot.checkTapScheduleNavigationAndScheduleTabIsSelected()
            checkNoDataScreenIsDisplayed()

            // Remote data source supplies some data, trigger auto refresh
            fakeUITestRepository.setRemoteSchedulesForTest(schedule = ScheduleListSampleData.listOfSixteen)
            composeTestRule.mainClock.advanceTimeBy(milliseconds = 31_000)
            checkAllListItemsAreDisplayed(schedule = ScheduleListSampleData.listOfSixteen)

            // Repository set to return error, trigger auto refresh
            val exceptionMessage = "Testing Exception"
            fakeUITestRepository.setExceptionForTest(IOException(exceptionMessage))
            composeTestRule.mainClock.advanceTimeBy(milliseconds = 31_000)
            mainActivityTestRobot.checkSnackBarExceptionMessageIsDisplayedAndDismissed(exceptionMessage = exceptionMessage)

            // tap on navigation bar again (second tap) to scroll to top
            mainActivityTestRobot.tapNavigationSchedule()
            assertScheduleItemIsDisplayed(
                title = ScheduleListSampleData.listOfSixteen[0].title,
            )
        }
    }
}
