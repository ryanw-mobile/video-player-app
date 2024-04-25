/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.schedule

import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.rwmobi.dazncodechallenge.ui.test.DaznCodeChallengeTestRule

internal class ScheduleScreenTestRobot(
    private val composeTestRule: DaznCodeChallengeTestRule,
) {
    init {
        printSemanticTree()
    }

    fun printSemanticTree() {
        composeTestRule.onRoot().printToLog("SemanticTree")
    }
}
