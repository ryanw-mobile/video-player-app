/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import uk.ryanwong.dazn.codechallenge.data.source.DaznApiDataSource
import util.wrapEspressoIdlingResource

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
class DefaultCachedRepository(
    private val remoteDataSource: DaznApiDataSource,
    private val localDataSource: DaznApiDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DaznApiRepository {

    override fun observeEvents(): LiveData<ApiResult<List<Event>>> {
        wrapEspressoIdlingResource {
            return localDataSource.observeEvents()
        }
    }

    override fun observeSchedule(): LiveData<ApiResult<List<Schedule>>> {
        wrapEspressoIdlingResource {
            return localDataSource.observeSchedule()
        }
    }

    /***
     * Fetch data from REST API and store to database
     */
    override suspend fun refreshEvents() {
        wrapEspressoIdlingResource {
            updateEventsFromRemoteDataSource()
        }
    }

    private suspend fun updateEventsFromRemoteDataSource() {
        wrapEspressoIdlingResource {
            val remoteEvents = remoteDataSource.getEvents()

            if (remoteEvents is ApiResult.Success) {
                localDataSource.syncEvents(remoteEvents.data)
            } else if (remoteEvents is ApiResult.Error) {
                throw remoteEvents.exception
            }
        }
    }

    override suspend fun refreshSchedule() {
        wrapEspressoIdlingResource {
            updateScheduleFromRemoteDataSource()
        }
    }

    private suspend fun updateScheduleFromRemoteDataSource() {
        wrapEspressoIdlingResource {
            val remoteSchedules = remoteDataSource.getSchedules()

            if (remoteSchedules is ApiResult.Success) {
                localDataSource.syncSchedule(remoteSchedules.data)
            } else if (remoteSchedules is ApiResult.Error) {
                throw remoteSchedules.exception
            }
        }
    }
}