/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.lifecycle.LiveData
import uk.ryanwong.dazn.codechallenge.data.source.local.BaseLocalDataSource
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule

class FakeLocalDataSource(
    private val events: MutableList<Event> = mutableListOf(),
    private val schedules: MutableList<Schedule> = mutableListOf()
) : BaseLocalDataSource() {

    override fun observeEvents(): LiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun observeSchedule(): LiveData<List<Schedule>> {
        TODO("Not yet implemented")
    }

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