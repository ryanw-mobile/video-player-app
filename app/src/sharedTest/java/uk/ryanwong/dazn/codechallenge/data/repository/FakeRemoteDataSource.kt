/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import uk.ryanwong.dazn.codechallenge.base.BaseRemoteDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.ApiResult
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule
import java.io.IOException

class FakeRemoteDataSource(
    events: MutableList<Event>? = mutableListOf(),
    schedules: MutableList<Schedule>? = mutableListOf()
) : BaseRemoteDataSource {
    private var events = events
    private var schedules = schedules
    private var shouldReturnError = false
    private var exceptionMessage = ""

    fun setShouldReturnIOException(state: Boolean, exceptionMessage: String) {
        shouldReturnError = state
        this.exceptionMessage = exceptionMessage
    }

    override suspend fun getEvents(): ApiResult<List<Event>> {
        if (shouldReturnError) {
            return ApiResult.Error(IOException(exceptionMessage))
        }
        events?.let { return ApiResult.Success(ArrayList(it)) }
        return ApiResult.Error(
            Exception("Events not found")
        )
    }

    override suspend fun getSchedules(): ApiResult<List<Schedule>> {
        if (shouldReturnError) {
            return ApiResult.Error(IOException(exceptionMessage))
        }
        schedules?.let { return ApiResult.Success(ArrayList(it)) }
        return ApiResult.Error(
            Exception("Schedules not found")
        )
    }

    fun setEvents(events: List<Event>) {
        this.events = events.toMutableList()
    }

    fun setSchedule(schedules: List<Schedule>) {
        this.schedules = schedules.toMutableList()
    }
}