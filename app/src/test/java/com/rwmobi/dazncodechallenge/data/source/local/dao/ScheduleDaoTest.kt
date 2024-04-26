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
import com.rwmobi.dazncodechallenge.data.source.local.mapper.asSchedule
import com.rwmobi.dazncodechallenge.data.source.local.mapper.asScheduleDbEntity
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule1
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule1Modified
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule2
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule3
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

    // Test function names reviewed by ChatGPT for consistencies

    @Test
    fun getScheduleById_ShouldReturnInsertedSchedule() = runTest {
        // GIVEN - empty database
        database.scheduleDao.insert(schedule1.asScheduleDbEntity())
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        result.asSchedule() shouldBe schedule1
    }

    @Test
    fun getSchedules_ShouldReturnAllInsertedSchedules() = runTest {
        // GIVEN - Empty database
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asScheduleDbEntity())
        val result = database.scheduleDao.getSchedules()
        result.size shouldBe 3
    }

    @Test
    fun getScheduleById_ShouldReturnUpdatedScheduleAfterUpsert() = runTest {
        database.scheduleDao.insert(schedule1.asScheduleDbEntity())
        database.scheduleDao.insert(schedule1Modified.asScheduleDbEntity())
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        result.asSchedule() shouldBe schedule1Modified
    }

    @Test
    fun getScheduleById_ShouldReturnNullAfterScheduleDeletion() = runTest {
        database.scheduleDao.insert(schedule1.asScheduleDbEntity())
        database.scheduleDao.delete(schedule1.scheduleId)
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        result shouldBe null
    }

    @Test
    fun getScheduleById_ShouldReturnNullAfterDeletingSchedule() = runTest {
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asScheduleDbEntity())
        database.scheduleDao.delete(schedule1.scheduleId)
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        result shouldBe null
    }

    @Test
    fun getSchedules_ShouldReturnEmptyListAfterClear() = runTest {
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asScheduleDbEntity())
        database.scheduleDao.clear()
        val result = database.scheduleDao.getSchedules()
        result shouldBe emptyList()
    }

    // Dirty bit testing
    @Test
    fun getScheduleById_ShouldReturnScheduleWithDirtyFlagFalseAfterInsert() = runTest {
        // GIVEN - empty database
        database.scheduleDao.insert(schedule1.asScheduleDbEntity())
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        result.dirty shouldBe false
    }

    @Test
    fun getScheduleById_ShouldReturnScheduleWithDirtyFlagTrueAfterMarkDirty() = runTest {
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asScheduleDbEntity())
        database.scheduleDao.markDirty()
        val result = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        result.dirty shouldBe true
    }

    @Test
    fun getScheduleById_ShouldReturnScheduleWithDirtyFlagFalseAfterUpdate() = runTest {
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asScheduleDbEntity())
        database.scheduleDao.markDirty()

        database.scheduleDao.insert(schedule1Modified.asScheduleDbEntity())

        val result = database.scheduleDao.getScheduleById(schedule1Modified.scheduleId)
        result.dirty shouldBe false
    }

    @Test
    fun getSchedules_ShouldReturnNonDirtySchedulesAfterDeleteDirty() = runTest {
        database.scheduleDao.insertAll(listOf(schedule1, schedule3).asScheduleDbEntity())
        database.scheduleDao.markDirty()

        database.scheduleDao.insert(schedule2.asScheduleDbEntity())
        database.scheduleDao.deleteDirty()

        val result = database.scheduleDao.getSchedules()
        result.asSchedule().shouldContainExactly(schedule2)
    }
}
