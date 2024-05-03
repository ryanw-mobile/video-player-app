/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.exoplayer

import com.rwmobi.dazncodechallenge.ui.model.ErrorMessage

data class ExoPlayerUIState(
    val videoWidth: Int = 0,
    val videoHeight: Int = 0,
    val hasVideoLoaded: Boolean = false,
    val isFullScreenMode: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
