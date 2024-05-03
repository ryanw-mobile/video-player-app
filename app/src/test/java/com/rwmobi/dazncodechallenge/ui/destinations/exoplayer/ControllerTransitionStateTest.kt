package com.rwmobi.dazncodechallenge.ui.destinations.exoplayer

import android.view.View
import io.kotest.matchers.shouldBe
import org.junit.Test

class ControllerTransitionStateTest {

    // Simulate when the player controller is invisible, showing full controller, leaving with slider alone, invisible

    @Test
    fun updateState_shouldReturnCorrectState_whenControllerAnimates() {
        var controllerTransitionState = ControllerTransitionState.GONE

        controllerTransitionState = controllerTransitionState.updateState(visibility = View.VISIBLE, isControllerFullyVisible = false)
        controllerTransitionState shouldBe ControllerTransitionState.APPEARING

        controllerTransitionState = controllerTransitionState.updateState(visibility = View.VISIBLE, isControllerFullyVisible = true)
        controllerTransitionState shouldBe ControllerTransitionState.VISIBLE

        repeat(times = 3) {
            // This is when the controller hiding parts of it in phases. It triggers the listener every time.
            controllerTransitionState = controllerTransitionState.updateState(visibility = View.VISIBLE, isControllerFullyVisible = false)
            controllerTransitionState shouldBe ControllerTransitionState.DISAPPEARING
        }

        controllerTransitionState = controllerTransitionState.updateState(visibility = View.GONE, isControllerFullyVisible = false)
        controllerTransitionState shouldBe ControllerTransitionState.GONE
    }
}
