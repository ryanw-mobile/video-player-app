/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local.interfaces

import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

interface LocalDataSource {
    suspend fun getEvents(): List<Event>
    suspend fun getSchedules(): List<Schedule>
    suspend fun submitEvents(events: List<Event>)
    suspend fun submitSchedule(schedules: List<Schedule>)
}
