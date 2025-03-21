/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.network

import com.rwmobi.dazncodechallenge.data.source.network.dto.EventNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.dto.ScheduleNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.interfaces.NetworkDataSource
import com.rwmobi.dazncodechallenge.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SandBoxAPIDataSource @Inject constructor(
    private val retrofitService: DaznApiService,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : NetworkDataSource {

    override suspend fun getEvents(): List<EventNetworkDto> {
        return withContext(dispatcher) {
            retrofitService.getEvents()
        }
    }

    override suspend fun getSchedules(): List<ScheduleNetworkDto> {
        return withContext(dispatcher) {
            retrofitService.getSchedule()
        }
    }
}
