/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import uk.ryanwong.dazn.codechallenge.base.BaseLocalDataSource
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import uk.ryanwong.dazn.codechallenge.data.source.DaznApiDaos

/**
 * Concrete implementation of a data source as a db.
 */
class RoomDbDataSource internal constructor(
    private val daznApiDaos: DaznApiDaos,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseLocalDataSource {

    override fun observeEvents(): LiveData<List<Event>> = daznApiDaos.eventsDao.observeEvents()
    override fun observeSchedule(): LiveData<List<Schedule>> =
        daznApiDaos.scheduleDao.observeSchedules()

    /**
     * Returns the events cached in the local database.
     * Data can be cached using syncEvents(...)
     */
    override suspend fun getEvents(): List<Event> = withContext(ioDispatcher) {
        daznApiDaos.eventsDao.getEvents()
    }

    /**
     * Returns the schedules cached in the local database.
     * Data can be cached using syncSchedules(...)
     */
    override suspend fun getSchedules(): List<Schedule> = withContext(ioDispatcher) {
        daznApiDaos.scheduleDao.getSchedules()
    }

    // Implementation note:
    // Since the provided RestAPIs have no paging design and no synchronization mechanism
    // We assume each time calling to submit*(..) will have a complete dataset supplied, and we overwrite our local DB
    // Afterwards, any cached data no longer in the new dataset will be deleted using the dirty bit approach
    override suspend fun submitEvents(events: List<Event>) = withContext(ioDispatcher) {
        Timber.d("syncEvents() - processed ${events.size} items")
        // Kotlin usage note
        // apply: Object configuration. Returns Context object
        // run: Object configuration and computing the result. Returns lambda result
        daznApiDaos.eventsDao.run {
            markDirty()
            insertAll(events)
            deleteDirty()
        }
    }

    override suspend fun submitSchedule(schedules: List<Schedule>) = withContext(ioDispatcher) {
        Timber.d("syncSchedule() - processed ${schedules.size} items")
        daznApiDaos.scheduleDao.run {
            markDirty()
            insertAll(schedules)
            deleteDirty()
        }
    }
}