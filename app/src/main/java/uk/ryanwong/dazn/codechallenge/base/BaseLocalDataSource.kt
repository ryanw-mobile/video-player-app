/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.base

import androidx.lifecycle.LiveData
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule

/**
 * Local Data Source interface
 *
 * A Local Data Source:
 * - Passively relies on external source to supply data using submit*(...) functions
 * - Has its own infrastructure to store data (can be in-memory or disk based)
 * - Can provide live data or a data snapshot
 */
interface BaseLocalDataSource {

    // Return LiveData
    fun observeEvents(): LiveData<List<Event>>
    fun observeSchedule(): LiveData<List<Schedule>>

    // Return static data
    suspend fun getEvents(): List<Event>
    suspend fun getSchedules(): List<Schedule>

    suspend fun submitEvents(events: List<Event>)
    suspend fun submitSchedule(schedules: List<Schedule>)
}