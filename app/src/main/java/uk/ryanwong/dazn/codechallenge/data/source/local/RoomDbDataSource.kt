/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import uk.ryanwong.dazn.codechallenge.data.source.local.daos.DaznApiDaos
import uk.ryanwong.dazn.codechallenge.data.source.local.entities.asDatabaseModel
import uk.ryanwong.dazn.codechallenge.data.source.local.entities.asDomainModel
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule
import javax.inject.Inject

/**
 * Concrete implementation of a data source as a db.
 */
class RoomDbDataSource @Inject constructor(
    private val daznApiDaos: DaznApiDaos,
    private val ioDispatcher: CoroutineDispatcher
) : LocalDataSource {

    override fun observeEvents(): LiveData<List<Event>> {
        return Transformations.map(daznApiDaos.eventsDao.observeEvents()) {
            it.map { item ->
                Event(
                    item.eventId,
                    item.title,
                    item.subtitle,
                    item.date,
                    item.imageUrl,
                    item.videoUrl
                )
            }
        }
    }

    override fun observeSchedule(): LiveData<List<Schedule>> {
        return Transformations.map(daznApiDaos.scheduleDao.observeSchedules()) {
            it.map { item ->
                Schedule(
                    item.scheduleId,
                    item.title,
                    item.subtitle,
                    item.date,
                    item.imageUrl
                )
            }
        }
    }

    /**
     * Returns the events cached in the local database.
     * Data can be cached using syncEvents(...)
     */
    override suspend fun getEvents(): List<Event> = withContext(ioDispatcher) {
        daznApiDaos.eventsDao.getEvents().asDomainModel()
    }

    /**
     * Returns the schedules cached in the local database.
     * Data can be cached using syncSchedules(...)
     */
    override suspend fun getSchedules(): List<Schedule> = withContext(ioDispatcher) {
        daznApiDaos.scheduleDao.getSchedules().asDomainModel()
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
            insertAll(events.asDatabaseModel())
            deleteDirty()
        }
    }

    override suspend fun submitSchedule(schedules: List<Schedule>) = withContext(ioDispatcher) {
        Timber.d("syncSchedule() - processed ${schedules.size} items")
        daznApiDaos.scheduleDao.run {
            markDirty()
            insertAll(schedules.asDatabaseModel())
            deleteDirty()
        }
    }
}
