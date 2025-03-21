/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.domain.model

import java.util.Date

data class Schedule(
    val scheduleId: Int,
    val title: String,
    val subtitle: String,
    val date: Date,
    val imageUrl: String,
)
