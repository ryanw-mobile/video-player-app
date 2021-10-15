/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
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
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@SmallTest
class ScheduleDaoTest {
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
    fun insertSchedule_GetById_ExpectsSameSchedule() = runBlockingTest {
        // GIVEN - empty database
        // Do Nothing

        // WHEN - Insert an schedule
        database.scheduleDao.insert(schedule1.asDatabaseModel())

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        MatcherAssert.assertThat(loaded, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.scheduleId, `is`(schedule1.scheduleId))
        MatcherAssert.assertThat(loaded.title, `is`(schedule1.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(schedule1.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(schedule1.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(schedule1.imageUrl))
    }

    @Test
    fun insertAll_GetSchedules_ExpectsCorrectListSize() = runBlockingTest {
        // GIVEN - Empty database
        // do nothing

        // WHEN - use insertAll to insert three schedules
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asDatabaseModel())

        // THEN - When get the schedules, the list size should be 3
        val loaded = database.scheduleDao.getSchedules()
        MatcherAssert.assertThat(loaded.size, `is`(3))
    }

    @Test
    fun upsertSchedule_GetById_ExpectsUpdatedSchedule() = runBlockingTest {
        // GIVEN - Insert an schedule
        database.scheduleDao.insert(schedule1.asDatabaseModel())

        // WHEN - Update the schedule with a same Id, but with different data
        // get the schedule by id from the database
        database.scheduleDao.insert(schedule1Modified.asDatabaseModel())

        // THEN - When get the schedule by Id again, it should contain the new values
        val loaded = database.scheduleDao.getScheduleById(schedule1Modified.scheduleId)
        MatcherAssert.assertThat(loaded, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.scheduleId, `is`(schedule1Modified.scheduleId))
        MatcherAssert.assertThat(loaded.title, `is`(schedule1Modified.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(schedule1Modified.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(schedule1Modified.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(schedule1Modified.imageUrl))
    }

    @Test
    fun deleteSchedule_GetById_ExpectsNull() = runBlockingTest {
        // GIVEN - Insert an schedule
        database.scheduleDao.insert(schedule1.asDatabaseModel())

        // WHEN - delete the schedule by Id
        database.scheduleDao.delete(schedule1.scheduleId)

        // THEN - When get the schedule by Id again, it should return null
        val loaded = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        MatcherAssert.assertThat(loaded, CoreMatchers.nullValue())
    }

    @Test
    fun insertAll_DeleteOneAndGetById_ExpectsNull() = runBlockingTest {
        // GIVEN - Insert 3 schedules in a list
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asDatabaseModel())

        // WHEN - clear the database
        database.scheduleDao.delete(schedule1.scheduleId)

        // THEN - When get the schedules, the list should be empty
        val loaded = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        MatcherAssert.assertThat(loaded, CoreMatchers.nullValue())
    }

    @Test
    fun insertAll_Clear_ExpectsEmptyList() = runBlockingTest {
        // GIVEN - Insert 3 schedules in a list
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asDatabaseModel())

        // WHEN - clear the database
        database.scheduleDao.clear()

        // THEN - When get the schedules, the list should be empty
        val loaded = database.scheduleDao.getSchedules()
        MatcherAssert.assertThat(loaded.size, `is`(0))
    }

    // Dirty bit testing

    @Test
    fun insertNewSchedule_GetById_ExpectsDirtyFalse() = runBlockingTest {
        // GIVEN - empty database
        // Do Nothing

        // WHEN - Insert an schedule
        database.scheduleDao.insert(schedule1.asDatabaseModel())

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        MatcherAssert.assertThat(loaded, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.scheduleId, `is`(schedule1.scheduleId))
        MatcherAssert.assertThat(loaded.title, `is`(schedule1.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(schedule1.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(schedule1.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(schedule1.imageUrl))
        MatcherAssert.assertThat(loaded.dirty, `is`(false))
    }

    @Test
    fun markDirty_GetById_ExpectsDirtyTrue() = runBlockingTest {
        // GIVEN - multiple schedules
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asDatabaseModel())

        // WHEN - Insert an schedule and mark it as dirty
        database.scheduleDao.markDirty()

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1.scheduleId)
        MatcherAssert.assertThat(loaded, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.scheduleId, `is`(schedule1.scheduleId))
        MatcherAssert.assertThat(loaded.title, `is`(schedule1.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(schedule1.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(schedule1.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(schedule1.imageUrl))
        MatcherAssert.assertThat(loaded.dirty, `is`(true))
    }

    @Test
    fun dirtySchedules_UpdateSchedule_ExpectsDirtyFalse() = runBlockingTest {
        // GIVEN - multiple schedules and marked dirty
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3).asDatabaseModel())
        database.scheduleDao.markDirty()

        // WHEN - Upsert schedule 1
        database.scheduleDao.insert(schedule1Modified.asDatabaseModel())

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1Modified.scheduleId)
        MatcherAssert.assertThat(loaded, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.scheduleId, `is`(schedule1Modified.scheduleId))
        MatcherAssert.assertThat(loaded.title, `is`(schedule1Modified.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(schedule1Modified.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(schedule1Modified.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(schedule1Modified.imageUrl))
        MatcherAssert.assertThat(loaded.dirty, `is`(false))
    }

    @Test
    fun dirtySchedules_InsertOneAndDeleteDirty_ExpectsOneSchedule() = runBlockingTest {
        // GIVEN - multiple schedules and marked dirty
        database.scheduleDao.insertAll(listOf(schedule1, schedule3).asDatabaseModel())
        database.scheduleDao.markDirty()

        // WHEN - Insert schedule 2
        database.scheduleDao.insert(schedule2.asDatabaseModel())
        database.scheduleDao.deleteDirty()

        // THEN - Get all schedules. Expect only schedule 2 is returned
        val loaded = database.scheduleDao.getSchedules()
        MatcherAssert.assertThat(loaded.size, `is`(1))
        val loadedSchedule = loaded[0]
        MatcherAssert.assertThat(loadedSchedule, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loadedSchedule.scheduleId, `is`(schedule2.scheduleId))
        MatcherAssert.assertThat(loadedSchedule.title, `is`(schedule2.title))
        MatcherAssert.assertThat(loadedSchedule.subtitle, `is`(schedule2.subtitle))
        MatcherAssert.assertThat(loadedSchedule.date, `is`(schedule2.date))
        MatcherAssert.assertThat(loadedSchedule.imageUrl, `is`(schedule2.imageUrl))
        MatcherAssert.assertThat(loadedSchedule.dirty, `is`(false))
    }

    @After
    fun closeDb() = database.close()
}