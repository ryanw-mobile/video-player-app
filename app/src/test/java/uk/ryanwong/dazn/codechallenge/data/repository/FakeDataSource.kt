/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import androidx.lifecycle.LiveData
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import uk.ryanwong.dazn.codechallenge.data.source.DaznApiDataSource

class FakeDataSource(
    events: MutableList<Event>? = mutableListOf(),
    schedules: MutableList<Schedule>? = mutableListOf()
) : DaznApiDataSource {
    private var _events = events
    private var _schedules = schedules

    override fun observeEvents(): LiveData<ApiResult<List<Event>>> {
        TODO("Not yet implemented")
    }

    override fun observeSchedule(): LiveData<ApiResult<List<Schedule>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEvents(): ApiResult<List<Event>> {
        _events?.let { return ApiResult.Success(ArrayList(it)) }
        return ApiResult.Error(
            Exception("Events not found")
        )
    }

    override suspend fun getSchedules(): ApiResult<List<Schedule>> {
        _schedules?.let { return ApiResult.Success(ArrayList(it)) }
        return ApiResult.Error(
            Exception("Schedules not found")
        )
    }

    override suspend fun syncEvents(events: List<Event>) {
        _events = events.toMutableList()
    }

    override suspend fun syncSchedule(schedules: List<Schedule>) {
        _schedules = schedules.toMutableList()
    }
}