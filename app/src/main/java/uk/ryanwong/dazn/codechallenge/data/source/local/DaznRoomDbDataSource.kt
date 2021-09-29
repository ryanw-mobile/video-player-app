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
import uk.ryanwong.dazn.codechallenge.data.source.DaznApiDataSource

/**
 * Concrete implementation of a data source as a db.
 */
class DaznRoomDbDataSource internal constructor(
    private val eventsDao: EventsDao,
    private val scheduleDao: ScheduleDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DaznApiDataSource {

    override fun observeEvents(): LiveData<ApiResult<List<Event>>> {
        return eventsDao.observeEvents().map {
            Success(it)
        }
    }

    override fun observeSchedule(): LiveData<ApiResult<List<Schedule>>> {
        return scheduleDao.observeSchedules().map {
            Success(it)
        }
    }

    /**
     * Returns the events cached in the local database.
     * Data can be cached using syncEvents(...)
     */
    override suspend fun getEvents(): ApiResult<List<Event>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(eventsDao.getEvents())
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
            Success(scheduleDao.getSchedules())
        } catch (e: Exception) {
            Error(e)
        }
    }

    // Implementation note:
    // Since the provided RestAPIs have no paging design and no synchronization mechanism
    // We assume each dataset provided is complete, and we overwrite our local DB
    // Afterwards any cached data no longer in the new dataset will be deleted using the dirty bit approach

    override suspend fun syncEvents(events: List<Event>) = withContext(ioDispatcher) {
        Timber.d("syncEvents() - received ${events.size}")
        eventsDao.markDirty()
        eventsDao.insertAll(events)
        eventsDao.deleteDirty()
    }

    override suspend fun syncSchedule(schedules: List<Schedule>) = withContext(ioDispatcher) {
        Timber.d("syncSchedule() - received ${schedules.size}")
        scheduleDao.markDirty()
        scheduleDao.insertAll(schedules)
        scheduleDao.deleteDirty()
    }
}