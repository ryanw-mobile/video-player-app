/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.base

import androidx.lifecycle.LiveData
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule

abstract class BaseRepository {

    // The LiveData implementations are designed to be used by ViewModels
    abstract fun observeEvents(): LiveData<List<Event>>
    abstract fun observeSchedule(): LiveData<List<Schedule>>

    // The static versions are designed to be used for tests
    abstract suspend fun getEvents(): List<Event>
    abstract suspend fun getSchedule(): List<Schedule>

    // The functions exposed to ViewModels
    // They don't have to care about where we pulled the data from
    abstract suspend fun refreshEvents()
    abstract suspend fun refreshSchedule()
}