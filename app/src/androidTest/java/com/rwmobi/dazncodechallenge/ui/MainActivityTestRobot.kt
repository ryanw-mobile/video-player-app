/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.espresso.Espresso
import androidx.test.platform.app.InstrumentationRegistry
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.ui.navigation.AppNavItem
import com.rwmobi.dazncodechallenge.ui.test.DaznCodeChallengeTestRule
import com.rwmobi.dazncodechallenge.ui.test.withRole

internal class MainActivityTestRobot(
    private val composeTestRule: DaznCodeChallengeTestRule,
) {
    init {
        assertNavigationItemsAreDisplayed()
    }

    // Checks
    fun checkTapEventsNavigationAndEventsTabIsSelected() {
        try {
            tapNavigationEvents()
            assertEventsTabIsSelected()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected events tab is not selected. ${e.message}", e)
        }
    }

    fun checkTapScheduleNavigationAndScheduleTabIsSelected() {
        try {
            tapNavigationSchedule()
            assertScheduleTabIsSelected()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected schedule tab is not selected. ${e.message}", e)
        }
    }

    fun checkNavigationLayoutIsCorrect() {
        try {
            val windowWidthSizeClass = getWindowSizeClass().widthSizeClass
            if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
                assertNavigationBarIsDisplayed()
            } else {
                assertNavigationRailIsDisplayed()
            }
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected navigation layout is not observed. ${e.message}", e)
        }
    }

    fun checkExoPlayerIsDisplayedAndClosed() {
        try {
            assertExoPlayerIsDisplayed()
            assertNavigationBarIsNotDisplayed()

            Espresso.pressBack()
            waitUntilPlayerDismissed()

            assertExoPlayerIsNotDisplayed()
            assertNavigationBarIsDisplayed()
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected ExoPlayer behaviour is not observed. ${e.message}", e)
        }
    }

    fun checkSnackBarExceptionMessageIsDisplayedAndDismissed(exceptionMessage: String) {
        try {
            assertSnackbarIsDisplayed(message = exceptionMessage)
            tapOK()
            assertSnackbarIsNotDisplayed(message = exceptionMessage)
        } catch (e: AssertionError) {
            composeTestRule.onRoot().printToLog("MainActivityTestRobotError")
            throw AssertionError("Expected snackbar behaviour is not observed. ${e.message}", e)
        }
    }

    // Actions
    fun printSemanticTree() {
        with(composeTestRule) {
            onRoot().printToLog("SemanticTree")
        }
    }

    fun tapNavigationEvents() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.events))),
            ).performClick()
        }
    }

    fun tapNavigationSchedule() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.schedule))),
            ).performClick()
        }
    }

    private fun waitUntilPlayerDismissed() {
        with(composeTestRule) {
            waitUntil(timeoutMillis = 2_000) {
                !onNodeWithContentDescription(label = activity.getString(R.string.content_description_video_player)).isDisplayed()
            }
        }
    }

    private fun tapOK() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Button).and(hasText(text = activity.getString(R.string.ok))),
            ).performClick()
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun getWindowSizeClass(): WindowSizeClass {
        val metrics = InstrumentationRegistry.getInstrumentation().targetContext.resources.displayMetrics
        val widthPx = metrics.widthPixels
        val heightPx = metrics.heightPixels
        val density = metrics.density

        val widthDp = widthPx / density
        val heightDp = heightPx / density

        return WindowSizeClass.calculateFromSize(size = DpSize(width = widthDp.dp, height = heightDp.dp))
    }

    // Assertions
    fun assertEventsTabIsSelected() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.events))),
            ).assertIsSelected()

            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.schedule))),
            ).assertIsNotSelected()
        }
    }

    private fun assertScheduleTabIsSelected() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.events))),
            ).assertIsNotSelected()

            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.schedule))),
            ).assertIsSelected()
        }
    }

    private fun assertNavigationBarIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_navigation_bar)).assertIsDisplayed()
        }
    }

    private fun assertNavigationBarIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_navigation_bar)).assertDoesNotExist()
        }
    }

    private fun assertNavigationRailIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_navigation_rail)).assertIsDisplayed()
        }
    }

    private fun assertNavigationItemsAreDisplayed() {
        with(composeTestRule) {
            for (navigationItem in AppNavItem.navBarItems) {
                onNode(
                    matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(navigationItem.titleResId))),
                ).assertIsDisplayed()
            }
        }
    }

    private fun assertExoPlayerIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_video_player)).assertIsDisplayed()
        }
    }

    private fun assertExoPlayerIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_video_player)).assertDoesNotExist()
        }
    }

    private fun assertSnackbarIsDisplayed(message: String) {
        with(composeTestRule) {
            onNodeWithText(text = message).assertIsDisplayed()
            onNode(
                matcher = withRole(Role.Button).and(hasText(text = activity.getString(R.string.ok))),
            ).assertIsDisplayed()
        }
    }

    private fun assertSnackbarIsNotDisplayed(message: String) {
        with(composeTestRule) {
            onNodeWithText(text = message).assertDoesNotExist()
            onNode(
                matcher = withRole(Role.Button).and(hasText(text = activity.getString(R.string.ok))),
            ).assertDoesNotExist()
        }
    }
}
