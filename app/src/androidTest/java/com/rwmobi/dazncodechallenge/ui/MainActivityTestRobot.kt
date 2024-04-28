/*
 * Copyright (c) 2024. Ryan Wong
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
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
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

    // Actions
    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
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

    fun waitUntilPlayerDismissed() {
        with(composeTestRule) {
            waitUntil(timeoutMillis = 2_000) {
                !onNodeWithContentDescription(label = activity.getString(R.string.content_description_video_player)).isDisplayed()
            }
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
    fun assertNavigationBarIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_navigation_bar)).assertIsDisplayed()
        }
    }

    fun assertNavigationBarIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_navigation_bar)).assertDoesNotExist()
        }
    }

    fun assertNavigationRailIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_navigation_rail)).assertIsDisplayed()
        }
    }

    fun assertNavigationItemsAreDisplayed() {
        with(composeTestRule) {
            for (navigationItem in AppNavItem.navBarItems) {
                onNode(
                    matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(navigationItem.titleResId))),
                ).assertIsDisplayed()
            }
        }
    }

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

    fun assertScheduleTabIsSelected() {
        with(composeTestRule) {
            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.events))),
            ).assertIsNotSelected()

            onNode(
                matcher = withRole(Role.Tab).and(hasContentDescription(value = activity.getString(R.string.schedule))),
            ).assertIsSelected()
        }
    }

    fun assertExoPlayerIsDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_video_player)).assertIsDisplayed()
        }
    }

    fun assertExoPlayerIsNotDisplayed() {
        with(composeTestRule) {
            onNodeWithContentDescription(label = activity.getString(R.string.content_description_video_player)).assertDoesNotExist()
        }
    }
}
