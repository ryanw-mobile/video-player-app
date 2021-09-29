/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import androidx.lifecycle.LiveData
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule

interface DaznApiRepository {

    // The LiveData designed to be used by ViewModels
    fun observeEvents(): LiveData<ApiResult<List<Event>>>
    fun observeSchedule(): LiveData<ApiResult<List<Schedule>>>

    // The functions exposed to ViewModels
    // They don't have to care about where we pulled the data from
    suspend fun refreshEvents()
    suspend fun refreshSchedule()
}