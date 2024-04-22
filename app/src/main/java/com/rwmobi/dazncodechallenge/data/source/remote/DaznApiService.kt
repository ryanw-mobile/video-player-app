/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.remote

import com.rwmobi.dazncodechallenge.data.source.remote.entities.EventNetworkEntity
import com.rwmobi.dazncodechallenge.data.source.remote.entities.ScheduleNetworkEntity
import retrofit2.http.GET

sealed interface DaznApiService {
    @GET("getEvents")
    suspend fun getEvents(): List<EventNetworkEntity>

    @GET("getSchedule")
    suspend fun getSchedule(): List<ScheduleNetworkEntity>
}
