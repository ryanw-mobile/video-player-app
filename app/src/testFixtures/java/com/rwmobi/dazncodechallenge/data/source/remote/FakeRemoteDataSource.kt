/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package com.rwmobi.dazncodechallenge.data.source.remote

import com.rwmobi.dazncodechallenge.data.source.network.dto.EventNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.dto.ScheduleNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.interfaces.NetworkDataSource
import com.rwmobi.dazncodechallenge.data.source.network.mapper.asEvent
import com.rwmobi.dazncodechallenge.data.source.network.mapper.asEventNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.mapper.asSchedule
import com.rwmobi.dazncodechallenge.data.source.network.mapper.asScheduleNetworkDto
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule
import java.io.IOException

class FakeRemoteDataSource(
    eventDomain: List<Event> = listOf(),
    scheduleDomain: List<Schedule> = listOf(),
) : NetworkDataSource {
    private val events =
        mutableListOf<EventNetworkDto>().apply {
            addAll(eventDomain.asEventNetworkDto())
        }
    private val schedule =
        mutableListOf<ScheduleNetworkDto>().apply {
            addAll(scheduleDomain.asScheduleNetworkDto())
        }
    private var shouldReturnError = false
    private var exceptionMessage = ""

    fun setShouldReturnIOException(
        shouldReturnError: Boolean,
        exceptionMessage: String,
    ) {
        this.shouldReturnError = shouldReturnError
        this.exceptionMessage = exceptionMessage
    }

    override suspend fun getEvents(): Result<List<Event>> {
        if (shouldReturnError) {
            return Result.failure(IOException(exceptionMessage))
        }
        return Result.success(events.asEvent())
    }

    override suspend fun getSchedules(): Result<List<Schedule>> {
        if (shouldReturnError) {
            return Result.failure(IOException(exceptionMessage))
        }
        return Result.success(schedule.asSchedule())
    }

    fun setEvents(events: List<Event>) {
        this.events.apply {
            clear()
            addAll(events.asEventNetworkDto())
        }
    }

    fun setSchedule(schedule: List<Schedule>) {
        this.schedule.apply {
            clear()
            addAll(schedule.asScheduleNetworkDto())
        }
    }
}
