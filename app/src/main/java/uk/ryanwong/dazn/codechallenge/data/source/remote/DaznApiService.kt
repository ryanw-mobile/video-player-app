/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.remote

import retrofit2.http.GET
import uk.ryanwong.dazn.codechallenge.data.source.remote.entities.EventNetworkEntity
import uk.ryanwong.dazn.codechallenge.data.source.remote.entities.ScheduleNetworkEntity

sealed interface DaznApiService {
    @GET("getEvents")
    suspend fun getEvents(): List<EventNetworkEntity>

    @GET("getSchedule")
    suspend fun getSchedule(): List<ScheduleNetworkEntity>
}
