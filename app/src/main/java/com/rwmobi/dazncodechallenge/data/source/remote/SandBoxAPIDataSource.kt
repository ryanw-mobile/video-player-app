/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.remote

import com.rwmobi.dazncodechallenge.data.source.remote.entities.asDomainModel
import com.rwmobi.dazncodechallenge.data.source.remote.interfaces.RemoteDataSource
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SandBoxAPIDataSource
@Inject
constructor(
    private val retrofitService: DaznApiService,
    private val ioDispatcher: CoroutineDispatcher,
) :
    RemoteDataSource {
    /***
     * Retrieve events from network
     */
    override suspend fun getEvents(): ApiResult<List<Event>> =
        withContext(ioDispatcher) {
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
    override suspend fun getSchedules(): ApiResult<List<Schedule>> =
        withContext(ioDispatcher) {
            return@withContext try {
                // We could add sorting here, but it is redundant as our local data source will handle that
                ApiResult.Success(
                    retrofitService.getSchedule().asDomainModel(),
                ) // .sortedBy { it.date }
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
}
