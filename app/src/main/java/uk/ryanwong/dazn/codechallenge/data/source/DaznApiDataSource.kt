package uk.ryanwong.dazn.codechallenge.data.source

import androidx.lifecycle.LiveData
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule

/**
 * Main entry point for accessing API data
 */
interface DaznApiDataSource {

    fun observeEvents(): LiveData<ApiResult<List<Event>>>

    fun observeSchedule(): LiveData<ApiResult<List<Schedule>>>

    suspend fun getEvents(): ApiResult<List<Event>>

    suspend fun getSchedules(): ApiResult<List<Schedule>>

    suspend fun syncEvents(events: List<Event>)
    suspend fun syncSchedule(schedules: List<Schedule>)
}