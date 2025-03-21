/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.test.EventDbEntitySampleData.event1
import com.rwmobi.dazncodechallenge.test.EventDbEntitySampleData.event1Modified
import com.rwmobi.dazncodechallenge.test.EventDbEntitySampleData.event2
import com.rwmobi.dazncodechallenge.test.EventDbEntitySampleData.event3
import com.rwmobi.dazncodechallenge.test.ScheduleDbEntitySampleData.schedule1
import com.rwmobi.dazncodechallenge.test.ScheduleDbEntitySampleData.schedule1Modified
import com.rwmobi.dazncodechallenge.test.ScheduleDbEntitySampleData.schedule2
import com.rwmobi.dazncodechallenge.test.ScheduleDbEntitySampleData.schedule3
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class RoomDbDataSourceTest {

    private lateinit var database: DaznApiDatabase
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setupDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = DaznApiDatabase::class.java,
        ).allowMainThreadQueries().build()

        localDataSource = RoomDbDataSource(
            eventsDao = database.eventsDao,
            scheduleDao = database.scheduleDao,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    // Test function names reviewed by Gemini for consistency
    // Test coverage: basic CRUD on both events and schedule

    @Test
    fun `returns one event when one event submitted to empty list`() = runTest {
        localDataSource.submitEvents(listOf(event1))
        val resultList = localDataSource.getEvents()
        assertContentEquals(listOf(event1), resultList)
    }

    @Test
    fun `updates event when event upserted`() = runTest {
        localDataSource.submitEvents(listOf(event1))
        localDataSource.submitEvents(listOf(event1Modified))
        val resultList = localDataSource.getEvents()
        assertContentEquals(listOf(event1Modified), resultList)
    }

    @Test
    fun `clears events when empty list submitted`() = runTest {
        localDataSource.submitEvents(listOf(event1, event2, event3))
        localDataSource.submitEvents(listOf())
        val resultList = localDataSource.getEvents()
        assertTrue(resultList.isEmpty())
    }

    @Test
    fun `returns one schedule when one schedule submitted to empty list`() = runTest {
        localDataSource.submitSchedule(listOf(schedule1))
        val resultList = localDataSource.getSchedules()
        assertContentEquals(listOf(schedule1), resultList)
    }

    @Test
    fun `updates schedule when schedule upserted`() = runTest {
        localDataSource.submitSchedule(listOf(schedule1))
        localDataSource.submitSchedule(listOf(schedule1Modified))
        val resultList = localDataSource.getSchedules()
        assertContentEquals(listOf(schedule1Modified), resultList)
    }

    @Test
    fun `clears schedules when empty list submitted`() = runTest {
        localDataSource.submitSchedule(listOf(schedule1, schedule2, schedule3))
        localDataSource.submitSchedule(listOf())
        val resultList = localDataSource.getSchedules()
        assertTrue(resultList.isEmpty())
    }
}
