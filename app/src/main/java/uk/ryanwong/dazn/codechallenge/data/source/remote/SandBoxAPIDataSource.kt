/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uk.ryanwong.dazn.codechallenge.data.source.remote.entities.asDomainModel
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule
import javax.inject.Inject

class SandBoxAPIDataSource @Inject constructor(
    private val retrofitService: DaznApiService,
    private val ioDispatcher: CoroutineDispatcher
) :
    RemoteDataSource {

    /***
     * Retrieve events from network
     */
    override suspend fun getEvents(): ApiResult<List<Event>> = withContext(ioDispatcher) {
        return@withContext try {
            // We could add sorting here, but it is redundant as our local data source will handle that
            ApiResult.Success(retrofitService.getEvents().asDomainModel()) // .sortedBy { it.date }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    /***
     * Retrieve events from network
     */
    override suspend fun getSchedules(): ApiResult<List<Schedule>> = withContext(ioDispatcher) {
        return@withContext try {
            // We could add sorting here, but it is redundant as our local data source will handle that
            ApiResult.Success(
                retrofitService.getSchedule().asDomainModel()
            ) // .sortedBy { it.date }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}
