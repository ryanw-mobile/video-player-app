/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local

import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

class FakeLocalDataSource(
    private val events: MutableList<Event> = mutableListOf(),
    private val schedules: MutableList<Schedule> = mutableListOf(),
) : LocalDataSource {
    override suspend fun getEvents(): List<Event> = events
    override suspend fun getSchedules(): List<Schedule> = schedules

    override suspend fun submitEvents(events: List<Event>) {
        this.events.apply {
            clear()
            addAll(events)
        }
    }

    override suspend fun submitSchedule(schedules: List<Schedule>) {
        this.schedules.apply {
            clear()
            addAll(schedules)
        }
    }
}
