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

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `returns event when get event by id after insert`() = runTest {
        // GIVEN - empty database when insert an event
        database.eventsDao.insert(event1)
        val result = database.eventsDao.getEventById(event1.eventId)
        assertEquals(event1, result)
    }

    @Test
    fun `returns all events when get all events after inserting multiple events`() = runTest {
        // GIVEN - Empty database
        database.eventsDao.insertAll(listOf(event1, event2, event3))
        val result = database.eventsDao.getEvents()
        assertEquals(3, result.size)
    }

    @Test
    fun `returns updated event when upserting an existing event`() = runTest {
        database.eventsDao.insert(event1)
        database.eventsDao.insert(event1Modified)
        val result = database.eventsDao.getEventById(event1.eventId)
        assertEquals(event1Modified, result)
    }

    @Test
    fun `returns null when get event by id after deleting it`() = runTest {
        database.eventsDao.insert(event1)
        database.eventsDao.delete(event1.eventId)
        val result = database.eventsDao.getEventById(event1.eventId)
        assertNull(result)
    }

    @Test
    fun `returns null when get event by id after deleting one of the multiple events`() = runTest {
        database.eventsDao.insertAll(
            listOf(event1, event2, event3).map { it },
        )
        database.eventsDao.delete(event1.eventId)
        val result = database.eventsDao.getEventById(event1.eventId)
        assertNull(result)
    }

    @Test
    fun `returns empty list when get all events after clearing the database`() = runTest {
        database.eventsDao.insertAll(listOf(event1, event2, event3).map { it })
        database.eventsDao.clear()
        val result = database.eventsDao.getEvents()
        assertTrue(result.isEmpty())
    }

    // Dirty bit testing
    @Test
    fun `returns event with dirty flag false after insert`() = runTest {
        // GIVEN - empty database
        database.eventsDao.insert(event1)
        val result = database.eventsDao.getEventById(event1.eventId)
        assertEquals(event1, result)
    }

    @Test
    fun `returns event with dirty flag true when marked dirty`() = runTest {
        database.eventsDao.insertAll(listOf(event1, event2, event3))
        database.eventsDao.markDirty()
        val result = database.eventsDao.getEventById(event1.eventId)
        assertTrue(result.dirty)
    }

    @Test
    fun `returns event with dirty flag false when upserting an existing event after marked dirty`() = runTest {
        database.eventsDao.insertAll(listOf(event1, event2, event3))
        database.eventsDao.markDirty()

        database.eventsDao.insert(event1Modified)

        val result = database.eventsDao.getEventById(event1Modified.eventId)
        assertFalse(result.dirty)
    }

    @Test
    fun `returns only non-dirty events when getting all events after deleting dirty`() = runTest {
        database.eventsDao.insertAll(listOf(event1, event3))
        database.eventsDao.markDirty()

        database.eventsDao.insert(event2)
        database.eventsDao.deleteDirty()

        val result = database.eventsDao.getEvents()
        assertContentEquals(listOf(event2), result)
    }
}
