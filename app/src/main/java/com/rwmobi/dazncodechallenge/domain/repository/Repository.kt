/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.domain.repository

import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

interface Repository {
    suspend fun getEvents(): Result<List<Event>>
    suspend fun getSchedule(): Result<List<Schedule>>

    suspend fun refreshEvents(): Result<Unit>
    suspend fun refreshSchedule(): Result<Unit>
}
