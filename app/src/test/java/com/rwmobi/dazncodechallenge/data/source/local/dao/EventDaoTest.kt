/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rwmobi.dazncodechallenge.data.source.local.DaznApiDatabase
import com.rwmobi.dazncodechallenge.test.EventDbEntitySampleData.event1
import com.rwmobi.dazncodechallenge.test.EventDbEntitySampleData.event1Modified
import com.rwmobi.dazncodechallenge.test.EventDbEntitySampleData.event2
import com.rwmobi.dazncodechallenge.test.EventDbEntitySampleData.event3
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class EventDaoTest {

    private lateinit var database: DaznApiDatabase

    @Before
    fun setupDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = DaznApiDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun getEventById_ShouldReturnInsertedEvent() = runTest {
        // GIVEN - empty database
        database.eventsDao.insert(event1)
        val result = database.eventsDao.getEventById(event1.eventId)
        assertEquals(event1, result)
    }

    @Test
    fun getEvents_ShouldReturnAllInsertedEvents() = runTest {
        // GIVEN - Empty database
        database.eventsDao.insertAll(listOf(event1, event2, event3))
        val result = database.eventsDao.getEvents()
        assertEquals(3, result.size)
    }

    @Test
    fun getEventById_ShouldReturnUpdatedEventAfterUpsert() = runTest {
        database.eventsDao.insert(event1)
        database.eventsDao.insert(event1Modified)
        val result = database.eventsDao.getEventById(event1.eventId)
        assertEquals(event1Modified, result)
    }

    @Test
    fun getEventById_ShouldReturnNullAfterEventDeletion() = runTest {
        database.eventsDao.insert(event1)
        database.eventsDao.delete(event1.eventId)
        val result = database.eventsDao.getEventById(event1.eventId)
        assertNull(result)
    }

    @Test
    fun getEventById_ShouldReturnNullAfterDeletingEvent() = runTest {
        database.eventsDao.insertAll(
            listOf(event1, event2, event3).map { it },
        )
        database.eventsDao.delete(event1.eventId)
        val result = database.eventsDao.getEventById(event1.eventId)
        assertNull(result)
    }

    @Test
    fun getEvents_ShouldReturnEmptyListAfterClear() = runTest {
        database.eventsDao.insertAll(listOf(event1, event2, event3).map { it })
        database.eventsDao.clear()
        val result = database.eventsDao.getEvents()
        assertTrue(result.isEmpty())
    }

    // Dirty bit testing
    @Test
    fun getEventById_ShouldReturnEventWithDirtyFlagFalseAfterInsert() = runTest {
        // GIVEN - empty database
        database.eventsDao.insert(event1)
        val result = database.eventsDao.getEventById(event1.eventId)
        assertEquals(event1, result)
    }

    @Test
    fun getEventById_ShouldReturnEventWithDirtyFlagTrueAfterMarkDirty() = runTest {
        database.eventsDao.insertAll(listOf(event1, event2, event3))
        database.eventsDao.markDirty()
        val result = database.eventsDao.getEventById(event1.eventId)
        assertTrue(result.dirty)
    }

    @Test
    fun getEventById_ShouldReturnEventWithDirtyFlagFalseAfterUpdate() = runTest {
        database.eventsDao.insertAll(listOf(event1, event2, event3))
        database.eventsDao.markDirty()

        database.eventsDao.insert(event1Modified)

        val result = database.eventsDao.getEventById(event1Modified.eventId)
        assertFalse(result.dirty)
    }

    @Test
    fun getEvents_ShouldReturnNonDirtyEventsAfterDeleteDirty() = runTest {
        database.eventsDao.insertAll(listOf(event1, event3))
        database.eventsDao.markDirty()

        database.eventsDao.insert(event2)
        database.eventsDao.deleteDirty()

        val result = database.eventsDao.getEvents()
        assertContentEquals(listOf(event2), result)
    }
}
