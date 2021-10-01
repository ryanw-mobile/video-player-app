/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.ApiResult.Error
import uk.ryanwong.dazn.codechallenge.data.ApiResult.Success
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import uk.ryanwong.dazn.codechallenge.data.source.DaznApiDaos
import uk.ryanwong.dazn.codechallenge.data.source.DaznApiDataSource

/**
 * Concrete implementation of a data source as a db.
 */
class DaznRoomDbDataSource internal constructor(
    private val daznApiDaos: DaznApiDaos,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DaznApiDataSource {

    override fun observeEvents(): LiveData<ApiResult<List<Event>>> {
        return daznApiDaos.eventsDao.observeEvents().map {
            Success(it)
        }
    }

    override fun observeSchedule(): LiveData<ApiResult<List<Schedule>>> {
        return daznApiDaos.scheduleDao.observeSchedules().map {
            Success(it)
        }
    }

    /**
     * Returns the events cached in the local database.
     * Data can be cached using syncEvents(...)
     */
    override suspend fun getEvents(): ApiResult<List<Event>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(daznApiDaos.eventsDao.getEvents())
        } catch (e: Exception) {
            Error(e)
        }
    }

    /**
     * Returns the schedules cached in the local database.
     * Data can be cached using syncSchedules(...)
     */
    override suspend fun getSchedules(): ApiResult<List<Schedule>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(daznApiDaos.scheduleDao.getSchedules())
        } catch (e: Exception) {
            Error(e)
        }
    }

    // Implementation note:
    // Since the provided RestAPIs have no paging design and no synchronization mechanism
    // We assume each dataset provided is complete, and we overwrite our local DB
    // Afterwards any cached data no longer in the new dataset will be deleted using the dirty bit approach
    override suspend fun syncEvents(events: List<Event>) = withContext(ioDispatcher) {
        daznApiDaos.eventsDao.apply {
            markDirty()
            insertAll(events)
            deleteDirty()
        }
        Timber.d("syncEvents() - processed ${events.size} items")
    }

    override suspend fun syncSchedule(schedules: List<Schedule>) = withContext(ioDispatcher) {
        daznApiDaos.scheduleDao.apply {
            markDirty()
            insertAll(schedules)
            deleteDirty()
        }
        Timber.d("syncSchedule() - processed ${schedules.size} items")
    }
}