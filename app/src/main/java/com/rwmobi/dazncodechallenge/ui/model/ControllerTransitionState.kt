/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.model

import android.view.View

internal enum class ControllerTransitionState {
    GONE,
    APPEARING,
    VISIBLE,
    DISAPPEARING,
    ;

    fun updateState(visibility: Int, isControllerFullyVisible: Boolean): ControllerTransitionState = when {
        visibility == View.INVISIBLE && !isControllerFullyVisible -> GONE
        visibility == View.VISIBLE && isControllerFullyVisible -> VISIBLE
        visibility == View.VISIBLE && !isControllerFullyVisible -> {
            when (this) {
                VISIBLE -> DISAPPEARING
                GONE -> APPEARING
                else -> DISAPPEARING
            }
        }

        else -> GONE
    }
}
