/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.schedule

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.printToLog
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.domain.model.Schedule
import com.rwmobi.dazncodechallenge.ui.test.DaznCodeChallengeTestRule
import com.rwmobi.dazncodechallenge.ui.test.EventsListSampleData

internal class ScheduleScreenTestRobot(
    private val composeTestRule: DaznCodeChallengeTestRule,
) {
    // Checks
    fun checkNoDataScreenIsDisplayed() {
        try {
            assertNoDataScreenIsDisplayed()
            assertScheduleListIsNotDisplayed()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("ScheduleScreenTestRobotError")
            throw AssertionError("Expected no data screen is not displayed. ${e.message}", e)
        }
    }

    fun checkAllListItemsAreDisplayed(schedule: List<Schedule>) {
        try {
            assertNoDataScreenIsNotDisplayed()
            assertScheduleListIsDisplayed()
            for (index in schedule.indices) {
                scrollToListItem(index)
                assertScheduleItemIsDisplayed(
                    title = EventsListSampleData.listOfSixteen[index].title,
                )
            }
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("ScheduleScreenTestRobotError")
            throw AssertionError("Expected schedule items are not all displayed. ${e.message}", e)
        }
    }

    // Actions
    fun printSemanticTree() {
        with(composeTestRule) {
            onRoot().printToLog("SemanticTree")
        }
    }

    private fun scrollToListItem(index: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_schedule_list)).performScrollToIndex(index = index)
        }
    }

    // Assertions
    fun assertScheduleItemIsDisplayed(title: String) {
        with(composeTestRule) {
            onNodeWithText(text = title).assertIsDisplayed()
        }
    }

    private fun assertScheduleListIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_schedule_list)).assertIsDisplayed()
        }
    }

    private fun assertScheduleListIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_schedule_list)).assertDoesNotExist()
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
