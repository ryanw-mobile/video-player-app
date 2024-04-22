/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local.interfaces

import androidx.lifecycle.LiveData
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

/**
 * Local Data Source interface
 *
 * A Local Data Source:
 * - Passively relies on external source to supply data using submit*(...) functions
 * - Has its own infrastructure to store data (can be in-memory or disk based)
 * - Can provide live data or a data snapshot
 */
interface LocalDataSource {
    // Return LiveData
    fun observeEvents(): LiveData<List<Event>>

    fun observeSchedule(): LiveData<List<Schedule>>

    // Return static data
    suspend fun getEvents(): List<Event>

    suspend fun getSchedules(): List<Schedule>

    suspend fun submitEvents(events: List<Event>)

    suspend fun submitSchedule(schedules: List<Schedule>)
}
