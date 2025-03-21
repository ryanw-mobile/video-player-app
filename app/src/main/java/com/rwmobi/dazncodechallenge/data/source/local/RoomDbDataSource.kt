/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local

import com.rwmobi.dazncodechallenge.data.source.local.dao.EventsDao
import com.rwmobi.dazncodechallenge.data.source.local.dao.ScheduleDao
import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.data.source.local.model.EventDbEntity
import com.rwmobi.dazncodechallenge.data.source.local.model.ScheduleDbEntity
import com.rwmobi.dazncodechallenge.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class RoomDbDataSource @Inject constructor(
    private val eventsDao: EventsDao,
    private val scheduleDao: ScheduleDao,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : LocalDataSource {
    override suspend fun getEvents(): List<EventDbEntity> =
        withContext(dispatcher) {
            eventsDao.getEvents()
        }

    override suspend fun getSchedules(): List<ScheduleDbEntity> =
        withContext(dispatcher) {
            scheduleDao.getSchedules()
        }

    // Implementation note:
    // Since the provided RestAPIs have no paging design and no synchronization mechanism
    // We assume each time calling to submit*(..) will have a complete dataset supplied, and we overwrite our local DB
    // Afterwards, any cached data no longer in the new dataset will be deleted using the dirty bit approach
    override suspend fun submitEvents(events: List<EventDbEntity>) =
        withContext(dispatcher) {
            Timber.d("syncEvents() - processed ${events.size} items")
            with(eventsDao) {
                markDirty()
                insertAll(eventDBEntities = events)
                deleteDirty()
            }
        }

    override suspend fun submitSchedule(schedules: List<ScheduleDbEntity>) =
        withContext(dispatcher) {
            Timber.d("syncSchedule() - processed ${schedules.size} items")
            with(scheduleDao) {
                markDirty()
                insertAll(scheduleDBEntities = schedules)
                deleteDirty()
            }
        }
}
