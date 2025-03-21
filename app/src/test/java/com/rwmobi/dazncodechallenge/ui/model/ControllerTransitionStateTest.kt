/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.model

import android.view.View
import org.junit.Test
import kotlin.test.assertEquals

internal class ControllerTransitionStateTest {

    // Test function names reviewed by Gemini for consistency
    // Simulate when the player controller is invisible, showing full controller, leaving with slider alone, invisible

    @Test
    fun `returns APPEARING when controller is visible but not fully visible`() {
        var controllerTransitionState = ControllerTransitionState.GONE

        controllerTransitionState = controllerTransitionState.updateState(visibility = View.VISIBLE, isControllerFullyVisible = false)
        assertEquals(ControllerTransitionState.APPEARING, controllerTransitionState)
    }

    @Test
    fun `returns VISIBLE when controller is visible and fully visible`() {
        var controllerTransitionState = ControllerTransitionState.APPEARING

        controllerTransitionState = controllerTransitionState.updateState(visibility = View.VISIBLE, isControllerFullyVisible = true)
        assertEquals(ControllerTransitionState.VISIBLE, controllerTransitionState)
    }

    @Test
    fun `returns GONE when controller is gone`() {
        var controllerTransitionState = ControllerTransitionState.DISAPPEARING
        controllerTransitionState = controllerTransitionState.updateState(visibility = View.GONE, isControllerFullyVisible = false)
        assertEquals(ControllerTransitionState.GONE, controllerTransitionState)
    }

    @Test
    fun `returns DISAPPEARING when controller hiding parts of it`() {
        var controllerTransitionState = ControllerTransitionState.VISIBLE
        controllerTransitionState = controllerTransitionState.updateState(visibility = View.VISIBLE, isControllerFullyVisible = false)
        assertEquals(ControllerTransitionState.DISAPPEARING, controllerTransitionState)
    }
}
