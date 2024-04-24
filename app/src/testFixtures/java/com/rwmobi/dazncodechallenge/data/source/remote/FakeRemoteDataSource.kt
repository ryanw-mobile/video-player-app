/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.remote

import com.rwmobi.dazncodechallenge.data.source.network.interfaces.NetworkDataSource
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

class FakeRemoteDataSource : NetworkDataSource {
    private var events: List<Event> = emptyList()
    private var schedule: List<Schedule> = emptyList()
    private var exception: Throwable? = null

    override suspend fun getEvents(): Result<List<Event>> {
        return exception?.let {
            Result.failure(it)
        } ?: Result.success(events)
    }

    override suspend fun getSchedules(): Result<List<Schedule>> {
        return exception?.let {
            Result.failure(it)
        } ?: Result.success(schedule)
    }

    fun setEventsForTest(events: List<Event>) {
        this.events = events
    }

    fun setScheduleForTest(schedule: List<Schedule>) {
        this.schedule = schedule
    }

    fun setExceptionForTest(exception: Throwable) {
        this.exception = exception
    }
}
