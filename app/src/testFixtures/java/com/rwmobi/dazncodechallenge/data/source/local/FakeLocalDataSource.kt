/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local

import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

class FakeLocalDataSource : LocalDataSource {
    private var events: List<Event> = emptyList()
    private var schedules: List<Schedule> = emptyList()

    override suspend fun getEvents(): List<Event> = events
    override suspend fun getSchedules(): List<Schedule> = schedules

    override suspend fun submitEvents(events: List<Event>) {
        this.events = events
    }

    override suspend fun submitSchedule(schedules: List<Schedule>) {
        this.schedules = schedules
    }
}
