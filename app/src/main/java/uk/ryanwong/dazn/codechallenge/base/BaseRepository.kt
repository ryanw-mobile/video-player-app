/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.base

import androidx.lifecycle.LiveData
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule

interface BaseRepository {

    // The LiveData implementations are designed to be used by ViewModels
    fun observeEvents(): LiveData<List<Event>>
    fun observeSchedule(): LiveData<List<Schedule>>

    // The static versions are designed to be used for tests
    suspend fun getEvents(): List<Event>
    suspend fun getSchedule(): List<Schedule>

    // The functions exposed to ViewModels
    // They don't have to care about where we pulled the data from
    suspend fun refreshEvents()
    suspend fun refreshSchedule()
}