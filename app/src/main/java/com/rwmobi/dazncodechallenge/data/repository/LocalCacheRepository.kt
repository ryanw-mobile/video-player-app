/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.repository

import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.data.source.remote.ApiResult
import com.rwmobi.dazncodechallenge.data.source.remote.interfaces.RemoteDataSource
import com.rwmobi.dazncodechallenge.di.DispatcherModule
import com.rwmobi.dazncodechallenge.domain.exceptions.except
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule
import com.rwmobi.dazncodechallenge.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class LocalCacheRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : Repository {
    override suspend fun getEvents(): Result<List<Event>> {
        return withContext(dispatcher) {
            Result.runCatching {
                localDataSource.getEvents()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getSchedule(): Result<List<Schedule>> {
        return withContext(dispatcher) {
            Result.runCatching {
                localDataSource.getSchedules()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun refreshEvents(): Result<Unit> {
        return withContext(dispatcher) {
            Result.runCatching {
                val remoteEvents = remoteDataSource.getEvents()

                if (remoteEvents is ApiResult.Success && remoteEvents.data is List<*>) {
                    localDataSource.submitEvents(remoteEvents.data.filterIsInstance<Event>())
                } else if (remoteEvents is ApiResult.Error) {
                    throw remoteEvents.exception
                }
            }.except<CancellationException, _>()
        }
    }

    override suspend fun refreshSchedule(): Result<Unit> {
        return withContext(dispatcher) {
            Result.runCatching {
                val remoteSchedules = remoteDataSource.getSchedules()

                if (remoteSchedules is ApiResult.Success && remoteSchedules.data is List<*>) {
                    localDataSource.submitSchedule(remoteSchedules.data.filterIsInstance<Schedule>())
                } else if (remoteSchedules is ApiResult.Error) {
                    throw remoteSchedules.exception
                }
            }.except<CancellationException, _>()
        }
    }
}
