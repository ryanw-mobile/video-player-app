/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import androidx.lifecycle.LiveData
import uk.ryanwong.dazn.codechallenge.data.source.local.BaseLocalDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.BaseRemoteDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.ApiResult
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule
import uk.ryanwong.dazn.codechallenge.util.wrapEspressoIdlingResource
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
class DefaultCachedRepository @Inject constructor(
    private val remoteDataSource: BaseRemoteDataSource,
    private val localDataSource: BaseLocalDataSource
) : BaseRepository() {

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

            if (remoteEvents is ApiResult.Success && remoteEvents.data is List<*>) {
                localDataSource.submitEvents(remoteEvents.data.filterIsInstance<Event>())
            } else if (remoteEvents is ApiResult.Error) {
                throw remoteEvents.exception
            }
        }
    }

    override suspend fun refreshSchedule() {
        wrapEspressoIdlingResource {
            val remoteSchedules = remoteDataSource.getSchedules()

            if (remoteSchedules is ApiResult.Success && remoteSchedules.data is List<*>) {
                localDataSource.submitSchedule(remoteSchedules.data.filterIsInstance<Schedule>())
            } else if (remoteSchedules is ApiResult.Error) {
                throw remoteSchedules.exception
            }
        }
    }

}