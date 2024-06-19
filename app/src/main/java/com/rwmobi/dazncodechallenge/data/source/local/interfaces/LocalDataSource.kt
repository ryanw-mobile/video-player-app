/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local.interfaces

import com.rwmobi.dazncodechallenge.data.source.local.model.EventDbEntity
import com.rwmobi.dazncodechallenge.data.source.local.model.ScheduleDbEntity

interface LocalDataSource {
    suspend fun getEvents(): List<EventDbEntity>
    suspend fun getSchedules(): List<ScheduleDbEntity>
    suspend fun submitEvents(events: List<EventDbEntity>)
    suspend fun submitSchedule(schedules: List<ScheduleDbEntity>)
}
