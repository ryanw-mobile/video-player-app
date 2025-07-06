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
import com.rwmobi.dazncodechallenge.test.ScheduleDbEntitySampleData.schedule1
import com.rwmobi.dazncodechallenge.test.ScheduleDbEntitySampleData.schedule1Modified
import com.rwmobi.dazncodechallenge.test.ScheduleDbEntitySampleData.schedule2
import com.rwmobi.dazncodechallenge.test.ScheduleDbEntitySampleData.schedule3
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@Config(sdk = [35])
@RunWith(RobolectricTestRunner::class)
internal class ScheduleDaoTest {

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
    fun `returns inserted schedule when schedule is inserted`() = runTest {
        // GIVEN - empty database
        database.scheduleDao.insert(schedule1)
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertEquals(schedule1, result)
    }

    @Test
    fun `returns all inserted schedules when schedules are inserted`() = runTest {
        // GIVEN - Empty database
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3))
        val result = database.scheduleDao.getSchedules()
        assertEquals(3, result.size)
    }

    @Test
    fun `returns updated schedule when upserting the schedule`() = runTest {
        database.scheduleDao.insert(schedule1)
        database.scheduleDao.insert(schedule1Modified)
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertEquals(schedule1Modified, result)
    }

    @Test
    fun `returns null when schedule is deleted`() = runTest {
        database.scheduleDao.insert(schedule1)
        database.scheduleDao.delete(schedule1.scheduleId)
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertNull(result)
    }

    @Test
    fun `returns null when deleting schedule from multiple`() = runTest {
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3))
        database.scheduleDao.delete(schedule1.scheduleId)
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertNull(result)
    }

    @Test
    fun `returns empty list when clearing all schedules`() = runTest {
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3))
        database.scheduleDao.clear()
        val result = database.scheduleDao.getSchedules()
        assertEquals(emptyList(), result)
    }

    // Dirty bit testing
    @Test
    fun `returns schedule with dirty flag false when schedule is inserted`() = runTest {
        database.scheduleDao.insert(schedule1)
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertFalse(result.dirty)
    }

    @Test
    fun `returns schedule with dirty flag true when marking dirty`() = runTest {
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3))
        database.scheduleDao.markDirty()
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertTrue(result.dirty)
    }

    @Test
    fun `returns schedule with dirty flag false when updating schedule after mark dirty`() = runTest {
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3))
        database.scheduleDao.markDirty()

        database.scheduleDao.insert(schedule1Modified)

        val result = database.scheduleDao.getScheduleById(schedule1Modified.scheduleId)
        assertFalse(result.dirty)
    }

    @Test
    fun `returns non dirty schedules when deleting dirty schedules`() = runTest {
        database.scheduleDao.insertAll(listOf(schedule1, schedule3))
        database.scheduleDao.markDirty()

        database.scheduleDao.insert(schedule2)
        database.scheduleDao.deleteDirty()

        val result = database.scheduleDao.getSchedules()
        assertContentEquals(listOf(schedule2), result)
    }
}
