/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.repository

import com.rwmobi.dazncodechallenge.data.repository.mapper.toDbEntity
import com.rwmobi.dazncodechallenge.data.repository.mapper.toEvent
import com.rwmobi.dazncodechallenge.data.repository.mapper.toSchedule
import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.data.source.network.interfaces.NetworkDataSource
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
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource,
    @DispatcherModule.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : Repository {
    override suspend fun getEvents(): Result<List<Event>> {
        return withContext(dispatcher) {
            Result.runCatching {
                localDataSource.getEvents().toEvent()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getSchedule(): Result<List<Schedule>> {
        return withContext(dispatcher) {
            Result.runCatching {
                localDataSource.getSchedules().toSchedule()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun refreshEvents(): Result<Unit> {
        return withContext(dispatcher) {
            Result.runCatching {
                val remoteEvents = networkDataSource.getEvents().map { it.toDbEntity() }
                localDataSource.submitEvents(events = remoteEvents)
            }.except<CancellationException, _>()
        }
    }

    override suspend fun refreshSchedule(): Result<Unit> {
        return withContext(dispatcher) {
            Result.runCatching {
                val remoteSchedules = networkDataSource.getSchedules().map { it.toDbEntity() }
                localDataSource.submitSchedule(schedules = remoteSchedules)
            }
        }.except<CancellationException, _>()
    }
}
