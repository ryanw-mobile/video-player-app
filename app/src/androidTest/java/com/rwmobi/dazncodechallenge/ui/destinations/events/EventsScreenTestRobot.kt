/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.events

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeDown
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.ui.test.DaznCodeChallengeTestRule
import com.rwmobi.dazncodechallenge.ui.test.EventsListSampleData

internal class EventsScreenTestRobot(
    private val composeTestRule: DaznCodeChallengeTestRule,
) {
    // Checks
    fun checkAllListItemsAreDisplayed(events: List<Event>) {
        try {
            assertNoDataScreenIsNotDisplayed()
            assertEventListIsDisplayed()
            for (index in events.indices) {
                scrollToListItem(index)
                assertEventItemIsDisplayed(
                    title = EventsListSampleData.listOfSixteen[index].title,
                )
            }
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("EventsScreenTestRobotError")
            throw AssertionError("Expected event items are not all displayed. ${e.message}", e)
        }
    }

    fun checkNoDataScreenIsDisplayed() {
        try {
            assertNoDataScreenIsDisplayed()
            assertEventListIsNotDisplayed()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("EventsScreenTestRobotError")
            throw AssertionError("Expected no data screen is not displayed. ${e.message}", e)
        }
    }

    // Actions
    fun printSemanticTree() {
        with(composeTestRule) {
            onRoot().printToLog("SemanticTree")
        }
    }

    fun performPullToRefresh() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_pull_to_refresh))
                .performTouchInput {
                    swipeDown(
                        startY = 0f,
                        endY = 500f,
                        durationMillis = 1_000,
                    )
                }
        }
    }

    fun scrollAndTapListItem(index: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_events_list)).performScrollToIndex(index = index).performClick()
        }
    }

    private fun scrollToListItem(index: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_events_list)).performScrollToIndex(index = index)
        }
    }

    // Assertions
    fun assertEventItemIsDisplayed(title: String) {
        with(composeTestRule) {
            onNodeWithText(text = title).assertIsDisplayed()
        }
    }

    private fun assertEventListIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_events_list)).assertIsDisplayed()
        }
    }

    private fun assertEventListIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_events_list)).assertDoesNotExist()
        }
    }

    private fun assertNoDataScreenIsDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.no_data)).assertIsDisplayed()
        }
    }

    private fun assertNoDataScreenIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.no_data)).assertDoesNotExist()
        }
    }
}
