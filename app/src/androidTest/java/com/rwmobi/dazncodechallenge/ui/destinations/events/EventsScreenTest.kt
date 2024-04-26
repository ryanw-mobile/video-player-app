/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.events

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rwmobi.dazncodechallenge.MainActivity
import com.rwmobi.dazncodechallenge.data.repository.FakeUITestRepository
import com.rwmobi.dazncodechallenge.domain.repository.Repository
import com.rwmobi.dazncodechallenge.ui.MainActivityTestRobot
import com.rwmobi.dazncodechallenge.ui.test.EventsListSampleData
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
internal class EventsScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var repository: Repository

    private lateinit var mainActivityTestRobot: MainActivityTestRobot
    private lateinit var eventsScreenTestRobot: EventsScreenTestRobot
    private lateinit var fakeUITestRepository: FakeUITestRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
        eventsScreenTestRobot = EventsScreenTestRobot(composeTestRule)
        fakeUITestRepository = repository as FakeUITestRepository // workaround
    }

    @Test
    fun eventJourneyTest() = runTest {
        fakeUITestRepository.setRemoteEventsForTest(events = emptyList())

        with(mainActivityTestRobot) {
            tapNavigationEvents()
            assertEventsTabIsSelected()
        }

        with(eventsScreenTestRobot) {
            // Repository returns no data - expect no data screen
            assertNoDataScreenIsDisplayed()
            assertEventListIsNotDisplayed()

            // Remote data source supplies some data, pull to refresh
            fakeUITestRepository.setRemoteEventsForTest(events = EventsListSampleData.listOfSixteen)
            performPullToRefresh()

            // expect full list to be shown
            assertNoDataScreenIsNotDisplayed()
            assertEventListIsDisplayed()
            assertEventItemIsDisplayed(
                title = EventsListSampleData.listOfSixteen[0].title,
            )

            val lastListItemIndex = EventsListSampleData.listOfSixteen.lastIndex
            scrollToListItem(lastListItemIndex)
            assertEventItemIsDisplayed(
                title = EventsListSampleData.listOfSixteen[lastListItemIndex].title,
            )

            tapListItem(lastListItemIndex)
        }

        with(mainActivityTestRobot) {
            assertExoPlayerIsDisplayed()
            assertNavigationBarIsNotDisplayed()

            pressBack()
            waitUntilPlayerDismissed()

            assertExoPlayerIsNotDisplayed()
            assertNavigationBarIsDisplayed()
            assertEventsTabIsSelected()

            // tap on navigation bar again to scroll to top
            tapNavigationEvents()
        }

        with(eventsScreenTestRobot) {
            assertEventItemIsDisplayed(
                title = EventsListSampleData.listOfSixteen[0].title,
            )

            // Repository set to return error, trigger auto refresh
            val exceptionMessage = "Testing Exception"
            fakeUITestRepository.setExceptionForTest(IOException(exceptionMessage))
            performPullToRefresh2()

            // expect snackbar with error message
            assertSnackbarIsDisplayed(message = exceptionMessage)

            tapOK()
            assertSnackbarIsNotDisplayed(message = exceptionMessage)
        }
    }
}
