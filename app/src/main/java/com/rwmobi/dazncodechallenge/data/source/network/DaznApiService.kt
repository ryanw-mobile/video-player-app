/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.network

import com.rwmobi.dazncodechallenge.data.source.network.dto.EventNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.dto.ScheduleNetworkDto
import retrofit2.http.GET

sealed interface DaznApiService {
    @GET("getEvents")
    suspend fun getEvents(): List<EventNetworkDto>

    @GET("getSchedule")
    suspend fun getSchedule(): List<ScheduleNetworkDto>
}
