/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import uk.ryanwong.dazn.codechallenge.base.BaseRemoteDataSource
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import uk.ryanwong.dazn.codechallenge.data.source.remote.ApiResult
import java.io.IOException

class FakeRemoteDataSource(
    events: MutableList<Event>? = mutableListOf(),
    schedules: MutableList<Schedule>? = mutableListOf()
) : BaseRemoteDataSource {
    private var _events = events
    private var _schedules = schedules
    private var _shouldReturnError = false
    private var _exceptionMessage = ""

    fun setShouldReturnIOException(state: Boolean, exceptionMessage: String) {
        _shouldReturnError = state
        _exceptionMessage = exceptionMessage
    }

    override suspend fun getEvents(): ApiResult<List<Event>> {
        if (_shouldReturnError) {
            return ApiResult.Error(IOException(_exceptionMessage))
        }
        _events?.let { return ApiResult.Success(ArrayList(it)) }
        return ApiResult.Error(
            Exception("Events not found")
        )
    }

    override suspend fun getSchedules(): ApiResult<List<Schedule>> {
        if (_shouldReturnError) {
            return ApiResult.Error(IOException(_exceptionMessage))
        }
        _schedules?.let { return ApiResult.Success(ArrayList(it)) }
        return ApiResult.Error(
            Exception("Schedules not found")
        )
    }

    fun setEvents(events: List<Event>) {
        _events = events.toMutableList()
    }

    fun setSchedule(schedules: List<Schedule>) {
        _schedules = schedules.toMutableList()
    }
}