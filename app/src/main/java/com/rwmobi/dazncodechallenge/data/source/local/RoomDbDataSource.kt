/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local

import com.rwmobi.dazncodechallenge.data.source.local.dao.EventsDao
import com.rwmobi.dazncodechallenge.data.source.local.dao.ScheduleDao
import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.data.source.local.model.asDatabaseModel
import com.rwmobi.dazncodechallenge.data.source.local.model.asDomainModel
import com.rwmobi.dazncodechallenge.di.DispatcherModule
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class RoomDbDataSource @Inject constructor(
    private val eventsDao: EventsDao,
    private val scheduleDao: ScheduleDao,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : LocalDataSource {
    override suspend fun getEvents(): List<Event> =
        withContext(dispatcher) {
            eventsDao.getEvents().asDomainModel()
        }

    override suspend fun getSchedules(): List<Schedule> =
        withContext(dispatcher) {
            scheduleDao.getSchedules().asDomainModel()
        }

    // Implementation note:
    // Since the provided RestAPIs have no paging design and no synchronization mechanism
    // We assume each time calling to submit*(..) will have a complete dataset supplied, and we overwrite our local DB
    // Afterwards, any cached data no longer in the new dataset will be deleted using the dirty bit approach
    override suspend fun submitEvents(events: List<Event>) =
        withContext(dispatcher) {
            Timber.d("syncEvents() - processed ${events.size} items")
            with(eventsDao) {
                markDirty()
                insertAll(eventDBEntities = events.asDatabaseModel())
                deleteDirty()
            }
        }

    override suspend fun submitSchedule(schedules: List<Schedule>) =
        withContext(dispatcher) {
            Timber.d("syncSchedule() - processed ${schedules.size} items")
            with(scheduleDao) {
                markDirty()
                insertAll(scheduleDBEntities = schedules.asDatabaseModel())
                deleteDirty()
            }
        }
}
