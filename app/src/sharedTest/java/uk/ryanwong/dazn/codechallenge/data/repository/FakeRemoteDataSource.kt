/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import uk.ryanwong.dazn.codechallenge.base.BaseRemoteDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.ApiResult
import uk.ryanwong.dazn.codechallenge.data.source.remote.entities.EventNetworkEntity
import uk.ryanwong.dazn.codechallenge.data.source.remote.entities.ScheduleNetworkEntity
import uk.ryanwong.dazn.codechallenge.data.source.remote.entities.asDomainModel
import uk.ryanwong.dazn.codechallenge.data.source.remote.entities.asNetworkModel
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule
import java.io.IOException

class FakeRemoteDataSource(
    eventDomain: List<Event> = listOf(),
    scheduleDomain: List<Schedule> = listOf()
) : BaseRemoteDataSource() {
    private val events = mutableListOf<EventNetworkEntity>().apply {
        addAll(eventDomain.asNetworkModel())
    }
    private val schedule = mutableListOf<ScheduleNetworkEntity>().apply {
        addAll(scheduleDomain.asNetworkModel())
    }
    private var shouldReturnError = false
    private var exceptionMessage = ""

    fun setShouldReturnIOException(shouldReturnError: Boolean, exceptionMessage: String) {
        this.shouldReturnError = shouldReturnError
        this.exceptionMessage = exceptionMessage
    }

    override suspend fun getEvents(): ApiResult<List<Event>> {
        if (shouldReturnError) {
            return ApiResult.Error(IOException(exceptionMessage))
        }
        return ApiResult.Success(events.asDomainModel())
    }

    override suspend fun getSchedules(): ApiResult<List<Schedule>> {
        if (shouldReturnError) {
            return ApiResult.Error(IOException(exceptionMessage))
        }
        return ApiResult.Success(schedule.asDomainModel())
    }

    fun setEvents(events: List<Event>) {
        this.events.apply {
            clear()
            addAll(events.asNetworkModel())
        }
    }

    fun setSchedule(schedule: List<Schedule>) {
        this.schedule.apply {
            clear()
            addAll(schedule.asNetworkModel())
        }
    }
}

