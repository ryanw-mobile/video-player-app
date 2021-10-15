/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import androidx.lifecycle.LiveData
import uk.ryanwong.dazn.codechallenge.base.BaseLocalDataSource
import uk.ryanwong.dazn.codechallenge.data.source.local.entities.EventDbEntity
import uk.ryanwong.dazn.codechallenge.data.source.local.entities.ScheduleDbEntity
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule

class FakeLocalDataSource(
    events: MutableList<Event> = mutableListOf(),
    schedules: MutableList<Schedule> = mutableListOf()
) : BaseLocalDataSource {
    private var events = events
    private var schedules = schedules

    override fun observeEvents(): LiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun observeSchedule(): LiveData<List<Schedule>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEvents(): List<Event> = events.toList()

    override suspend fun getSchedules(): List<Schedule> = schedules.toList()

    override suspend fun submitEvents(events: List<Event>) {
        this.events = events.toMutableList()
    }

    override suspend fun submitSchedule(schedules: List<Schedule>) {
        this.schedules = schedules.toMutableList()
    }
}