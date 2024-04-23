/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.remote

import com.rwmobi.dazncodechallenge.data.source.network.dto.EventNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.dto.ScheduleNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.interfaces.NetworkDataSource
import com.rwmobi.dazncodechallenge.data.source.network.mapper.asEvent
import com.rwmobi.dazncodechallenge.data.source.network.mapper.asSchedule
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

class FakeRemoteDataSource : NetworkDataSource {
    private var events: List<EventNetworkDto> = emptyList()
    private var schedule: List<ScheduleNetworkDto> = emptyList()
    private var exception: Throwable? = null

    override suspend fun getEvents(): Result<List<Event>> {
        return exception?.let {
            Result.failure(it)
        } ?: Result.success(events.asEvent())
    }

    override suspend fun getSchedules(): Result<List<Schedule>> {
        return exception?.let {
            Result.failure(it)
        } ?: Result.success(schedule.asSchedule())
    }

    fun setEventsForTest(events: List<EventNetworkDto>) {
        this.events = events
    }

    fun setScheduleForTest(schedule: List<ScheduleNetworkDto>) {
        this.schedule = schedule
    }

    fun setExceptionForTest(exception: Throwable) {
        this.exception = exception
    }
}
