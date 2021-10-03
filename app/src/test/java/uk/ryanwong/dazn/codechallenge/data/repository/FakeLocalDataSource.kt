/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import androidx.lifecycle.LiveData
import uk.ryanwong.dazn.codechallenge.base.BaseLocalDataSource
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule

class FakeLocalDataSource(
    events: MutableList<Event> = mutableListOf(),
    schedules: MutableList<Schedule> = mutableListOf()
) : BaseLocalDataSource {
    private var _events = events
    private var _schedules = schedules
    
    override fun observeEvents(): LiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun observeSchedule(): LiveData<List<Schedule>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEvents(): List<Event> = _events.toList()

    override suspend fun getSchedules(): List<Schedule> = _schedules.toList()

    override suspend fun submitEvents(events: List<Event>) {
        _events = events.toMutableList()
    }

    override suspend fun submitSchedule(schedules: List<Schedule>) {
        _schedules = schedules.toMutableList()
    }
}