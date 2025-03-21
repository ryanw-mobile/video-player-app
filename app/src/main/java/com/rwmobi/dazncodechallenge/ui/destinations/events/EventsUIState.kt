/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.events

import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.ui.model.ErrorMessage

data class EventsUIState(
    val events: List<Event>? = null,
    val isLoading: Boolean = true,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
