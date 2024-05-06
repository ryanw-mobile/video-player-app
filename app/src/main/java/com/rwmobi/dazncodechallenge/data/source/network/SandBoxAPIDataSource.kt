/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.network

import com.rwmobi.dazncodechallenge.data.source.network.interfaces.NetworkDataSource
import com.rwmobi.dazncodechallenge.data.source.network.mapper.toEvent
import com.rwmobi.dazncodechallenge.data.source.network.mapper.toSchedule
import com.rwmobi.dazncodechallenge.di.DispatcherModule
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SandBoxAPIDataSource @Inject constructor(
    private val retrofitService: DaznApiService,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : NetworkDataSource {

    override suspend fun getEvents(): Result<List<Event>> {
        return withContext(dispatcher) {
            Result.runCatching {
                retrofitService.getEvents().toEvent()
            }
        }
    }

    override suspend fun getSchedules(): Result<List<Schedule>> {
        return withContext(dispatcher) {
            Result.runCatching {
                retrofitService.getSchedule().toSchedule()
            }
        }
    }
}
