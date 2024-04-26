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
import com.rwmobi.dazncodechallenge.data.repository.FakeRepository
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
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
        scheduleScreenTestRobot = ScheduleScreenTestRobot(composeTestRule)
        fakeRepository = repository as FakeRepository // workaround
    }

    @Test
    fun scheduleJourneyTest() = runTest {
        fakeRepository.setRemoteSchedulesForTest(schedule = emptyList())

        with(mainActivityTestRobot) {
            // Switch to the Schedule Tab
            tapNavigationSchedule()
            assertScheduleTabIsSelected()
        }

        with(scheduleScreenTestRobot) {
            // Repository returns no data - expect no data screen
            assertNoDataScreenIsDisplayed()
            assertScheduleListIsNotDisplayed()

            // Remote data source supplies some data, trigger auto refresh
            fakeRepository.setRemoteSchedulesForTest(schedule = ScheduleListSampleData.listOfSixteen)
            composeTestRule.mainClock.advanceTimeBy(milliseconds = 31_000)

            // expect full list to be shown
            assertNoDataScreenIsNotDisplayed()
            assertScheduleListIsDisplayed()
            assertScheduleItemIsDisplayed(
                title = ScheduleListSampleData.listOfSixteen[0].title,
            )

            val lastListItemIndex = ScheduleListSampleData.listOfSixteen.lastIndex
            scrollToListItem(lastListItemIndex)
            assertScheduleItemIsDisplayed(
                title = ScheduleListSampleData.listOfSixteen[lastListItemIndex].title,
            )

            // Repository set to return error, trigger auto refresh
            val exceptionMessage = "Testing Exception"
            fakeRepository.setExceptionForTest(IOException(exceptionMessage))
            composeTestRule.mainClock.advanceTimeBy(milliseconds = 31_000)

            // expect snackbar with error message
            assertSnackbarIsDisplayed(message = exceptionMessage)
        }
    }
}
