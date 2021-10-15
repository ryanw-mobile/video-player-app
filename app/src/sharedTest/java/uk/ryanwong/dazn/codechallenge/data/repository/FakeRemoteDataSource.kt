/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import uk.ryanwong.dazn.codechallenge.base.BaseRemoteDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.ApiResult
import uk.ryanwong.dazn.codechallenge.data.source.remote.entities.asDomainModel
import uk.ryanwong.dazn.codechallenge.data.source.remote.entities.asNetworkModel
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule
import java.io.IOException

class FakeRemoteDataSource(
    eventDomain: MutableList<Event> = mutableListOf(),
    scheduleDomain: MutableList<Schedule> = mutableListOf()
) : BaseRemoteDataSource {
    private var events = eventDomain.asNetworkModel()
    private var schedules = scheduleDomain.asNetworkModel()
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
        events?.let { return ApiResult.Success(ArrayList(it.asDomainModel())) }
        return ApiResult.Error(
            Exception("Events not found")
        )
    }

    override suspend fun getSchedules(): ApiResult<List<Schedule>> {
        if (shouldReturnError) {
            return ApiResult.Error(IOException(exceptionMessage))
        }
        schedules?.let { return ApiResult.Success(ArrayList(it.asDomainModel())) }
        return ApiResult.Error(
            Exception("Schedules not found")
        )
    }

    fun setEvents(events: List<Event>) {
        this.events = events.toMutableList().asNetworkModel()
    }

    fun setSchedule(schedules: List<Schedule>) {
        this.schedules = schedules.toMutableList().asNetworkModel()
    }
}