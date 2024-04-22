/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.repository

import androidx.lifecycle.LiveData
import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.data.source.remote.ApiResult
import com.rwmobi.dazncodechallenge.data.source.remote.interfaces.RemoteDataSource
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule
import com.rwmobi.dazncodechallenge.domain.repository.Repository
import javax.inject.Inject

/**
 * Default user-facing Repository
 *
 * Behind the scene:
 * - pulls the data from the network
 * - updates the database for caching
 * - always return the data from database
 * So after the first App launch, we are able to always show something,
 * before the new data arrives, or even without an Internet connection.
 */
class LocalCacheRepository
@Inject
constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : Repository {
    override fun observeEvents(): LiveData<List<Event>> {
        return localDataSource.observeEvents()
    }

    override fun observeSchedule(): LiveData<List<Schedule>> {
        return localDataSource.observeSchedule()
    }

    override suspend fun getEvents(): List<Event> =
        localDataSource.getEvents()

    override suspend fun getSchedule(): List<Schedule> {
        return localDataSource.getSchedules()
    }

    /***
     * Fetch data from REST API and store to database
     */
    override suspend fun refreshEvents() {
        val remoteEvents = remoteDataSource.getEvents()

        if (remoteEvents is ApiResult.Success && remoteEvents.data is List<*>) {
            localDataSource.submitEvents(remoteEvents.data.filterIsInstance<Event>())
        } else if (remoteEvents is ApiResult.Error) {
            throw remoteEvents.exception
        }
    }

    override suspend fun refreshSchedule() {
        val remoteSchedules = remoteDataSource.getSchedules()

        if (remoteSchedules is ApiResult.Success && remoteSchedules.data is List<*>) {
            localDataSource.submitSchedule(remoteSchedules.data.filterIsInstance<Schedule>())
        } else if (remoteSchedules is ApiResult.Error) {
            throw remoteSchedules.exception
        }
    }
}
