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

    // Simulate when the player controller is invisible, showing full controller, leaving with slider alone, invisible

    @Test
    fun updateState_shouldReturnCorrectState_whenControllerAnimates() {
        var controllerTransitionState = ControllerTransitionState.GONE

        controllerTransitionState = controllerTransitionState.updateState(visibility = View.VISIBLE, isControllerFullyVisible = false)
        assertEquals(ControllerTransitionState.APPEARING, controllerTransitionState)

        controllerTransitionState = controllerTransitionState.updateState(visibility = View.VISIBLE, isControllerFullyVisible = true)
        assertEquals(ControllerTransitionState.VISIBLE, controllerTransitionState)

        repeat(times = 3) {
            // This is when the controller hiding parts of it in phases. It triggers the listener every time.
            controllerTransitionState = controllerTransitionState.updateState(visibility = View.VISIBLE, isControllerFullyVisible = false)
            assertEquals(ControllerTransitionState.DISAPPEARING, controllerTransitionState)
        }

        controllerTransitionState = controllerTransitionState.updateState(visibility = View.GONE, isControllerFullyVisible = false)
        assertEquals(ControllerTransitionState.GONE, controllerTransitionState)
    }
}
