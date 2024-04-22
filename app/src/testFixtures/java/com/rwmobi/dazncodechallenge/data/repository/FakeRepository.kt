/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package com.rwmobi.dazncodechallenge.data.repository

import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule
import com.rwmobi.dazncodechallenge.domain.repository.Repository

class FakeRepository : Repository {
    private var shouldReturnError = false
    private var exceptionMessage = ""

    private var localEvents: List<Event> = emptyList()
    private var localSchedules: List<Schedule> = emptyList()
    private var remoteEvents: List<Event> = emptyList()
    private var remoteSchedules: List<Schedule> = emptyList()

    override suspend fun getEvents(): Result<List<Event>> {
        return if (shouldReturnError) {
            Result.failure(Exception(exceptionMessage))
        } else {
            Result.success(localEvents)
        }
    }

    override suspend fun getSchedule(): Result<List<Schedule>> {
        return if (shouldReturnError) {
            Result.failure(Exception(exceptionMessage))
        } else {
            Result.success(localSchedules)
        }
    }

    override suspend fun refreshEvents(): Result<Unit> {
        return if (shouldReturnError) {
            Result.failure(Exception(exceptionMessage))
        } else {
            localEvents = remoteEvents
            Result.success(Unit)
        }
    }

    override suspend fun refreshSchedule(): Result<Unit> {
        return if (shouldReturnError) {
            Result.failure(Exception(exceptionMessage))
        } else {
            localSchedules = remoteSchedules
            Result.success(Unit)
        }
    }

    fun setReturnError(
        shouldReturnError: Boolean,
        exceptionMessage: String,
    ) {
        this.shouldReturnError = shouldReturnError
        this.exceptionMessage = exceptionMessage
    }

    /***
     * Submit a list of events as a faked remote data source.
     * When refreshEvents() is called, the list will be loaded to the ViewModel
     */
    fun submitEventList(events: List<Event>) {
        remoteEvents = events
    }

    /***
     * Submit a list of events as a faked remote data source.
     * When refreshEvents() is called, the list will be loaded to the ViewModel
     */
    fun submitScheduleList(schedule: List<Schedule>) {
        remoteSchedules = schedule
    }
}
