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

class FakeRemoteDataSource : NetworkDataSource {
    private var events: List<EventNetworkDto> = emptyList()
    private var schedule: List<ScheduleNetworkDto> = emptyList()
    private var exception: Throwable? = null

    override suspend fun getEvents(): List<EventNetworkDto> {
        exception?.let {
            throw it
        }
        return events
    }

    override suspend fun getSchedules(): List<ScheduleNetworkDto> {
        exception?.let {
            throw it
        }
        return schedule
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
