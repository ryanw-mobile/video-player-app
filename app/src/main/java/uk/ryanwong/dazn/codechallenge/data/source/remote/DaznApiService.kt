/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.remote

import retrofit2.http.GET
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule

interface DaznApiService {
    @GET("getEvents")
    suspend fun getEvents(): List<Event>

    @GET("getSchedule")
    suspend fun getSchedule(): List<Schedule>
}