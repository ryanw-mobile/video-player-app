/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import uk.ryanwong.dazn.codechallenge.base.BaseLocalDataSource
import uk.ryanwong.dazn.codechallenge.base.BaseRemoteDataSource
import uk.ryanwong.dazn.codechallenge.base.BaseRepository
import uk.ryanwong.dazn.codechallenge.data.source.remote.ApiResult
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule
import uk.ryanwong.dazn.codechallenge.util.wrapEspressoIdlingResource

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
    private val remoteDataSource: BaseRemoteDataSource,
    private val localDataSource: BaseLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository {

    override fun observeEvents(): LiveData<List<Event>> {
        wrapEspressoIdlingResource {
            return localDataSource.observeEvents()
        }
    }

    override fun observeSchedule(): LiveData<List<Schedule>> {
        wrapEspressoIdlingResource {
            return localDataSource.observeSchedule()
        }
    }

    override suspend fun getEvents(): List<Event> =
        wrapEspressoIdlingResource {
            return localDataSource.getEvents()
        }


    override suspend fun getSchedule(): List<Schedule> {
        wrapEspressoIdlingResource {
            return localDataSource.getSchedules()
        }
    }

    /***
     * Fetch data from REST API and store to database
     */
    override suspend fun refreshEvents() {
        wrapEspressoIdlingResource {
            val remoteEvents = remoteDataSource.getEvents()

            if (remoteEvents is ApiResult.Success) {
                localDataSource.submitEvents(remoteEvents.data as List<Event>)
            } else if (remoteEvents is ApiResult.Error) {
                throw remoteEvents.exception
            }
        }
    }

    override suspend fun refreshSchedule() {
        wrapEspressoIdlingResource {
            val remoteSchedules = remoteDataSource.getSchedules()

            if (remoteSchedules is ApiResult.Success) {
                localDataSource.submitSchedule(remoteSchedules.data as List<Schedule>)
            } else if (remoteSchedules is ApiResult.Error) {
                throw remoteSchedules.exception
            }
        }
    }

}