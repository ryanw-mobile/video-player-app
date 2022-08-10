/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ryanwong.dazn.codechallenge.TestData.schedule1
import uk.ryanwong.dazn.codechallenge.TestData.schedule1Modified
import uk.ryanwong.dazn.codechallenge.TestData.schedule2
import uk.ryanwong.dazn.codechallenge.TestData.schedule3
import uk.ryanwong.dazn.codechallenge.data.source.local.DaznApiDatabase
import uk.ryanwong.dazn.codechallenge.data.source.local.entities.asDatabaseModel
import uk.ryanwong.dazn.codechallenge.data.source.local.entities.asDomainModel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@SmallTest
internal class ScheduleDaoTest {
    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // Use in-memory database for testing
    @Inject
    lateinit var database: DaznApiDatabase

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun insertSchedule_GetById_ReturnSameSchedule() = runTest {
        // GIVEN - empty database
        // Do Nothing

        // WHEN - Insert an schedule
        database.scheduleDao.insert(schedule1.asDatabaseModel())

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertThat(loaded.asDomainModel()).isEqualTo(schedule1)
    }

    @Test
    fun insertAll_GetSchedules_ReturnCorrectListSize() = runTest {
        // GIVEN - Empty database
        // do nothing

        // WHEN - use insertAll to insert three schedules
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asDatabaseModel())

        // THEN - When get the schedules, the list size should be 3
        val loaded = database.scheduleDao.getSchedules()
        assertThat(loaded).hasSize(3)
    }

    @Test
    fun upsertSchedule_GetById_ReturnUpdatedSchedule() = runTest {
        // GIVEN - Insert an schedule
        database.scheduleDao.insert(schedule1.asDatabaseModel())

        // WHEN - Update the schedule with a same Id, but with different data
        // get the schedule by id from the database
        database.scheduleDao.insert(schedule1Modified.asDatabaseModel())

        // THEN - When get the schedule by Id again, it should contain the new values
        val loaded = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertThat(loaded.asDomainModel()).isEqualTo(schedule1Modified)
    }

    @Test
    fun deleteSchedule_GetById_ReturnNull() = runTest {
        // GIVEN - Insert an schedule
        database.scheduleDao.insert(schedule1.asDatabaseModel())

        // WHEN - delete the schedule by Id
        database.scheduleDao.delete(schedule1.scheduleId)

        // THEN - When get the schedule by Id again, it should return null
        val loaded = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertThat(loaded).isNull()
    }

    @Test
    fun insertAll_DeleteOneAndGetById_ReturnNull() = runTest {
        // GIVEN - Insert 3 schedules in a list
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asDatabaseModel())

        // WHEN - clear the database
        database.scheduleDao.delete(schedule1.scheduleId)

        // THEN - When get the schedules, the list should be empty
        val loaded = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertThat(loaded).isNull()
    }

    @Test
    fun insertAll_Clear_ReturnEmptyList() = runTest {
        // GIVEN - Insert 3 schedules in a list
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asDatabaseModel())

        // WHEN - clear the database
        database.scheduleDao.clear()

        // THEN - When get the schedules, the list should be empty
        val loaded = database.scheduleDao.getSchedules()
        assertThat(loaded).isEmpty()
    }

    // Dirty bit testing

    @Test
    fun insertNewSchedule_GetById_ReturnDirtyFalse() = runTest {
        // GIVEN - empty database
        // Do Nothing

        // WHEN - Insert an schedule
        database.scheduleDao.insert(schedule1.asDatabaseModel())

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertThat(loaded.dirty).isFalse()
    }

    @Test
    fun markDirty_GetById_ReturnDirtyTrue() = runTest {
        // GIVEN - multiple schedules
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asDatabaseModel())

        // WHEN - Insert an schedule and mark it as dirty
        database.scheduleDao.markDirty()

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        assertThat(loaded.dirty).isTrue()
    }

    @Test
    fun dirtySchedules_UpdateSchedule_ReturnDirtyFalse() = runTest {
        // GIVEN - multiple schedules and marked dirty
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asDatabaseModel())
        database.scheduleDao.markDirty()

        // WHEN - Upsert schedule 1
        database.scheduleDao.insert(schedule1Modified.asDatabaseModel())

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1Modified.scheduleId)
        assertThat(loaded.dirty).isFalse()
    }

    @Test
    fun dirtySchedules_InsertOneAndDeleteDirty_ReturnOneSchedule() = runTest {
        // GIVEN - multiple schedules and marked dirty
        database.scheduleDao.insertAll(listOf(schedule1, schedule3).asDatabaseModel())
        database.scheduleDao.markDirty()

        // WHEN - Insert schedule 2
        database.scheduleDao.insert(schedule2.asDatabaseModel())
        database.scheduleDao.deleteDirty()

        // THEN - Get all schedules. Expect only schedule 2 is returned
        val loaded = database.scheduleDao.getSchedules()
        assertThat(loaded.asDomainModel()).containsExactly(schedule2)
    }

    @After
    fun closeDb() = database.close()
}