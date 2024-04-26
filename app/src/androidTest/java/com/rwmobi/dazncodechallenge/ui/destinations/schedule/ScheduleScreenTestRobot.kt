/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.schedule

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.printToLog
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.ui.test.DaznCodeChallengeTestRule
import com.rwmobi.dazncodechallenge.ui.test.withRole

internal class ScheduleScreenTestRobot(
    private val composeTestRule: DaznCodeChallengeTestRule,
) {
    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
    }

    fun scrollToListItem(index: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_schedule_list)).performScrollToIndex(index = index)
        }
    }

    fun assertScheduleListIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_schedule_list)).assertIsDisplayed()
        }
    }

    fun assertScheduleListIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_schedule_list)).assertDoesNotExist()
        }
    }

    fun assertNoDataScreenIsDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.no_data)).assertIsDisplayed()
        }
    }

    fun assertNoDataScreenIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithText(text = activity.getString(R.string.no_data)).assertDoesNotExist()
        }
    }

    fun assertScheduleItemIsDisplayed(title: String) {
        with(composeTestRule) {
            onNodeWithText(text = title).assertIsDisplayed()
        }
    }

    fun assertSnackbarIsDisplayed(message: String) {
        with(composeTestRule) {
            onNodeWithText(text = message).assertIsDisplayed()
            onNode(
                matcher = withRole(Role.Button).and(hasText(text = activity.getString(R.string.ok))),
            ).assertIsDisplayed()
        }
    }
}
