/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.events

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeDown
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.ui.test.DaznCodeChallengeTestRule
import com.rwmobi.dazncodechallenge.ui.test.withRole

internal class EventsScreenTestRobot(
    private val composeTestRule: DaznCodeChallengeTestRule,
) {
    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
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

    fun tapOK() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Button).and(hasText(text = activity.getString(R.string.ok))),
            ).performClick()
        }
    }

    fun tapListItem(index: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_events_list)).performScrollToIndex(index = index).performClick()
        }
    }

    fun scrollToListItem(index: Int) {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_events_list)).performScrollToIndex(index = index)
        }
    }

    fun assertEventListIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_events_list)).assertIsDisplayed()
        }
    }

    fun assertEventListIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_events_list)).assertDoesNotExist()
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

    fun assertEventItemIsDisplayed(title: String) {
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

    fun assertSnackbarIsNotDisplayed(message: String) {
        with(composeTestRule) {
            onNodeWithText(text = message).assertDoesNotExist()
            onNode(
                matcher = withRole(Role.Button).and(hasText(text = activity.getString(R.string.ok))),
            ).assertDoesNotExist()
        }
    }
}
