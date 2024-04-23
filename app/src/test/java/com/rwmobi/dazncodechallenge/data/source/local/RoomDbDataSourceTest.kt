/*
* Copyright (c) 2024. Ryan Wong
* https://github.com/ryanw-mobile
* Sponsored by RW MobiMedia UK Limited
*
*/

package com.rwmobi.dazncodechallenge.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.test.EventSampleData.event1
import com.rwmobi.dazncodechallenge.test.EventSampleData.event1Modified
import com.rwmobi.dazncodechallenge.test.EventSampleData.event2
import com.rwmobi.dazncodechallenge.test.EventSampleData.event3
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule1
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule1Modified
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule2
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule3
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

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

    // Test coverage: basic CRUD on both events and schedule

    @Test
    fun emptyList_SyncOneEvent_ReturnOneEvent() = runTest {
        localDataSource.submitEvents(listOf(event1))
        val resultList = localDataSource.getEvents()
        resultList.shouldContainExactlyInAnyOrder(event1)
    }

    @Test
    fun oneEvent_UpsertOneEvent_ReturnEventUpdated() = runTest {
        localDataSource.submitEvents(listOf(event1))
        localDataSource.submitEvents(listOf(event1Modified))
        val resultList = localDataSource.getEvents()
        resultList.shouldContainExactlyInAnyOrder(event1Modified)
    }

    @Test
    fun threeEvents_SyncEmptyList_ReturnNoEvents() = runTest {
        localDataSource.submitEvents(listOf(event1, event2, event3))
        localDataSource.submitEvents(listOf())
        val resultList = localDataSource.getEvents()
        resultList.size shouldBe 0
    }

    @Test
    fun emptyList_SyncOneSchedule_ReturnOneSchedule() = runTest {
        localDataSource.submitSchedule(listOf(schedule1))
        val resultList = localDataSource.getSchedules()
        resultList.shouldContainExactlyInAnyOrder(schedule1)
    }

    @Test
    fun oneSchedule_UpsertOneSchedule_ReturnScheduleUpdated() = runTest {
        localDataSource.submitSchedule(listOf(schedule1))
        localDataSource.submitSchedule(listOf(schedule1Modified))
        val resultList = localDataSource.getSchedules()
        resultList.shouldContainExactlyInAnyOrder(schedule1Modified)
    }

    @Test
    fun threeSchedules_SyncEmptyList_ReturnNoSchedules() = runTest {
        localDataSource.submitSchedule(listOf(schedule1, schedule2, schedule3))
        localDataSource.submitSchedule(listOf())
        val resultList = localDataSource.getSchedules()
        resultList.size shouldBe 0
    }
}
