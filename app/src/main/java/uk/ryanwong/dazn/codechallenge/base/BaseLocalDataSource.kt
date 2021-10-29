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
abstract class BaseLocalDataSource {

    // Return LiveData
    abstract fun observeEvents(): LiveData<List<Event>>
    abstract fun observeSchedule(): LiveData<List<Schedule>>

    // Return static data
    abstract suspend fun getEvents(): List<Event>
    abstract suspend fun getSchedules(): List<Schedule>

    abstract suspend fun submitEvents(events: List<Event>)
    abstract suspend fun submitSchedule(schedules: List<Schedule>)
}