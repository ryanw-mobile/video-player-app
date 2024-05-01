/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.exoplayer

import com.rwmobi.dazncodechallenge.ui.model.ErrorMessage

data class ExoPlayerUIState(
    val hasVideoLoaded: Boolean = false,
    val shouldPlayOnResume: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
