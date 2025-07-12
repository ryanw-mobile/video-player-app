/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.repository

import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule
import com.rwmobi.dazncodechallenge.domain.repository.Repository
import javax.inject.Inject

class FakeUITestRepository @Inject constructor() : Repository {
    private var localEvents: List<Event> = emptyList()
    private var localSchedules: List<Schedule> = emptyList()
    private var remoteEvents: List<Event> = emptyList()
    private var remoteSchedules: List<Schedule> = emptyList()
    private var exception: Throwable? = null

    override suspend fun getEvents(): Result<List<Event>> {
        return exception?.let {
            Result.failure(it)
        } ?: Result.success(localEvents) // Note: we do not sort by date
    }

    override suspend fun getSchedule(): Result<List<Schedule>> {
        return exception?.let {
            Result.failure(it)
        } ?: Result.success(localSchedules) // Note: we do not sort by date
    }

    override suspend fun refreshEvents(): Result<Unit> = exception?.let {
        Result.failure(it)
    } ?: run {
        localEvents = remoteEvents
        Result.success(Unit)
    }

    override suspend fun refreshSchedule(): Result<Unit> = exception?.let {
        Result.failure(it)
    } ?: run {
        localSchedules = remoteSchedules
        Result.success(Unit)
    }

    fun setExceptionForTest(exception: Throwable) {
        this.exception = exception
    }

    fun setLocalEventsForTest(events: List<Event>) {
        localEvents = events
    }

    fun setLocalSchedulesForTest(schedule: List<Schedule>) {
        localSchedules = schedule
    }

    fun setRemoteEventsForTest(events: List<Event>) {
        remoteEvents = events
    }

    fun setRemoteSchedulesForTest(schedule: List<Schedule>) {
        remoteSchedules = schedule
    }
}
