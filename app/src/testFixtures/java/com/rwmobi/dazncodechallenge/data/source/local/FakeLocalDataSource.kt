/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local

import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.data.source.local.model.EventDbEntity
import com.rwmobi.dazncodechallenge.data.source.local.model.ScheduleDbEntity

class FakeLocalDataSource : LocalDataSource {
    private var events: List<EventDbEntity> = emptyList()
    private var schedules: List<ScheduleDbEntity> = emptyList()

    override suspend fun getEvents(): List<EventDbEntity> = events
    override suspend fun getSchedules(): List<ScheduleDbEntity> = schedules

    override suspend fun submitEvents(events: List<EventDbEntity>) {
        this.events = events
    }

    override suspend fun submitSchedule(schedules: List<ScheduleDbEntity>) {
        this.schedules = schedules
    }
}
