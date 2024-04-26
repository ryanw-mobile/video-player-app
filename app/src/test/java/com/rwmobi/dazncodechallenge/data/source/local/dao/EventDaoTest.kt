/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rwmobi.dazncodechallenge.data.source.local.DaznApiDatabase
import com.rwmobi.dazncodechallenge.data.source.local.mapper.asEvent
import com.rwmobi.dazncodechallenge.data.source.local.mapper.asEventDbEntity
import com.rwmobi.dazncodechallenge.test.EventSampleData.event1
import com.rwmobi.dazncodechallenge.test.EventSampleData.event1Modified
import com.rwmobi.dazncodechallenge.test.EventSampleData.event2
import com.rwmobi.dazncodechallenge.test.EventSampleData.event3
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

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
        database.eventsDao.insert(event1.asEventDbEntity())
        val result = database.eventsDao.getEventById(event1.eventId)
        result.asEvent() shouldBe event1
    }

    @Test
    fun getEvents_ShouldReturnAllInsertedEvents() = runTest {
        // GIVEN - Empty database
        database.eventsDao.insertAll(listOf(event1, event2, event3).asEventDbEntity())
        val result = database.eventsDao.getEvents()
        result.size shouldBe 3
    }

    @Test
    fun getEventById_ShouldReturnUpdatedEventAfterUpsert() = runTest {
        database.eventsDao.insert(event1.asEventDbEntity())
        database.eventsDao.insert(event1Modified.asEventDbEntity())
        val result = database.eventsDao.getEventById(event1.eventId)
        result.asEvent() shouldBe event1Modified
    }

    @Test
    fun getEventById_ShouldReturnNullAfterEventDeletion() = runTest {
        database.eventsDao.insert(event1.asEventDbEntity())
        database.eventsDao.delete(event1.eventId)
        val result = database.eventsDao.getEventById(event1.eventId)
        result shouldBe null
    }

    @Test
    fun getEventById_ShouldReturnNullAfterDeletingEvent() = runTest {
        database.eventsDao.insertAll(
            listOf(event1, event2, event3).asEventDbEntity(),
        )
        database.eventsDao.delete(event1.eventId)
        val result = database.eventsDao.getEventById(event1.eventId)
        result shouldBe null
    }

    @Test
    fun getEvents_ShouldReturnEmptyListAfterClear() = runTest {
        database.eventsDao.insertAll(listOf(event1, event2, event3).asEventDbEntity())
        database.eventsDao.clear()
        val result = database.eventsDao.getEvents()
        result shouldBe emptyList()
    }

    // Dirty bit testing
    @Test
    fun getEventById_ShouldReturnEventWithDirtyFlagFalseAfterInsert() = runTest {
        // GIVEN - empty database
        database.eventsDao.insert(event1.asEventDbEntity())
        val result = database.eventsDao.getEventById(event1.eventId)
        result.asEvent() shouldBe event1
    }

    @Test
    fun getEventById_ShouldReturnEventWithDirtyFlagTrueAfterMarkDirty() = runTest {
        database.eventsDao.insertAll(listOf(event1, event2, event3).asEventDbEntity())
        database.eventsDao.markDirty()
        val result = database.eventsDao.getEventById(event1.eventId)
        result.dirty shouldBe true
    }

    @Test
    fun getEventById_ShouldReturnEventWithDirtyFlagFalseAfterUpdate() = runTest {
        database.eventsDao.insertAll(listOf(event1, event2, event3).asEventDbEntity())
        database.eventsDao.markDirty()

        database.eventsDao.insert(event1Modified.asEventDbEntity())

        val result = database.eventsDao.getEventById(event1Modified.eventId)
        result.dirty shouldBe false
    }

    @Test
    fun getEvents_ShouldReturnNonDirtyEventsAfterDeleteDirty() = runTest {
        database.eventsDao.insertAll(listOf(event1, event3).asEventDbEntity())
        database.eventsDao.markDirty()

        database.eventsDao.insert(event2.asEventDbEntity())
        database.eventsDao.deleteDirty()

        val result = database.eventsDao.getEvents()
        result.asEvent().shouldContainExactly(event2)
    }
}
