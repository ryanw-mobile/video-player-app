/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
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
import uk.ryanwong.dazn.codechallenge.data.model.ScheduleDaoTest.TestData.schedule1
import uk.ryanwong.dazn.codechallenge.data.model.ScheduleDaoTest.TestData.schedule1Modified
import uk.ryanwong.dazn.codechallenge.data.model.ScheduleDaoTest.TestData.schedule2
import uk.ryanwong.dazn.codechallenge.data.model.ScheduleDaoTest.TestData.schedule3
import uk.ryanwong.dazn.codechallenge.data.source.local.DaznApiDatabase
import uk.ryanwong.dazn.codechallenge.util.parseTimeStamp

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ScheduleDaoTest {
    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: DaznApiDatabase

    object TestData {
        val schedule1 = Schedule(
            10,
            "Pre-Match ITV: Jürgen Klopp",
            "",
            "2021-09-29T14:56:29.101Z".parseTimeStamp(),
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311354437259_image-header_pDach_1554838977000.jpeg?alt=media&token=8135fc30-3340-4449-9b45-daa9adc1bbc9"
        )

        val schedule1Modified = Schedule(
            10,
            "CSKA Moskow v St Petersburg",
            "KHL Ice Hockey",
            "2021-09-29T14:56:29.101Z".parseTimeStamp(),
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311428677455_image-header_pDach_1554829417000.jpeg?alt=media&token=ea122c47-2a50-4cf2-a901-2be2ff94f3c4"
        )

        val schedule2 = Schedule(
            12,
            "Rockets @ Thunder",
            "NBA",
            "2021-09-29T14:56:29.101Z".parseTimeStamp(),
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311471173073_image-header_pDach_1554571998000.jpeg?alt=media&token=a69da8e4-d2d1-45f0-a005-977311981d66"
        )

        val schedule3 = Schedule(
            13,
            "PSG v Strasbourg",
            "Ligue 1",
            "2021-09-29T14:56:29.101Z".parseTimeStamp(),
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311953989300_image-header_pDach_1554750608000.jpeg?alt=media&token=56f3a7a8-2f10-436c-8069-c762b37594cd"
        )
    }

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DaznApiDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun insertSchedule_GetById_ExpectsSameSchedule() = runBlockingTest {
        // GIVEN - empty database
        // Do Nothing

        // WHEN - Insert an schedule
        database.scheduleDao.insert(schedule1)

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1.id)
        MatcherAssert.assertThat<Schedule>(loaded as Schedule, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, `is`(schedule1.id))
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
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3))

        // THEN - When get the schedules, the list size should be 3
        val loaded = database.scheduleDao.getSchedules()
        MatcherAssert.assertThat(loaded.size, `is`(3))
    }

    @Test
    fun upsertSchedule_GetById_ExpectsUpdatedSchedule() = runBlockingTest {
        // GIVEN - Insert an schedule
        database.scheduleDao.insert(schedule1)

        // WHEN - Update the schedule with a same Id, but with different data
        // get the schedule by id from the database
        database.scheduleDao.insert(schedule1Modified)

        // THEN - When get the schedule by Id again, it should contain the new values
        val loaded = database.scheduleDao.getScheduleById(schedule1Modified.id)
        MatcherAssert.assertThat<Schedule>(loaded as Schedule, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, `is`(schedule1Modified.id))
        MatcherAssert.assertThat(loaded.title, `is`(schedule1Modified.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(schedule1Modified.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(schedule1Modified.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(schedule1Modified.imageUrl))
    }

    @Test
    fun deleteSchedule_GetById_ExpectsNull() = runBlockingTest {
        // GIVEN - Insert an schedule
        database.scheduleDao.insert(schedule1)

        // WHEN - delete the schedule by Id
        database.scheduleDao.delete(schedule1.id)

        // THEN - When get the schedule by Id again, it should return null
        val loaded = database.scheduleDao.getScheduleById(schedule1.id)
        MatcherAssert.assertThat<Schedule>(loaded as Schedule, CoreMatchers.nullValue())
    }

    @Test
    fun insertAll_DeleteOneAndGetById_ExpectsNull() = runBlockingTest {
        // GIVEN - Insert 3 schedules in a list
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3))

        // WHEN - clear the database
        database.scheduleDao.delete(schedule1.id)

        // THEN - When get the schedules, the list should be empty
        val loaded = database.scheduleDao.getScheduleById(schedule1.id)
        MatcherAssert.assertThat<Schedule>(loaded as Schedule, CoreMatchers.nullValue())
    }

    @Test
    fun insertAll_Clear_ExpectsEmptyList() = runBlockingTest {
        // GIVEN - Insert 3 schedules in a list
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3))

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
        database.scheduleDao.insert(schedule1)

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1.id)
        MatcherAssert.assertThat<Schedule>(loaded as Schedule, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, `is`(schedule1.id))
        MatcherAssert.assertThat(loaded.title, `is`(schedule1.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(schedule1.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(schedule1.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(schedule1.imageUrl))
        MatcherAssert.assertThat(loaded.dirty, `is`(false))
    }

    @Test
    fun markDirty_GetById_ExpectsDirtyTrue() = runBlockingTest {
        // GIVEN - multiple schedules
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3))

        // WHEN - Insert an schedule and mark it as dirty
        database.scheduleDao.markDirty()

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1.id)
        MatcherAssert.assertThat<Schedule>(loaded as Schedule, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, `is`(schedule1.id))
        MatcherAssert.assertThat(loaded.title, `is`(schedule1.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(schedule1.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(schedule1.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(schedule1.imageUrl))
        MatcherAssert.assertThat(loaded.dirty, `is`(true))
    }

    @Test
    fun dirtySchedules_UpdateSchedule_ExpectsDirtyFalse() = runBlockingTest {
        // GIVEN - multiple schedules and marked dirty
        database.scheduleDao.insertAll(listOf(schedule1, schedule2, schedule3))
        database.scheduleDao.markDirty()

        // WHEN - Upsert schedule 1
        database.scheduleDao.insert(schedule1Modified)

        // THEN - Get the schedule by id from the database. The loaded schedule contains the expected values
        val loaded = database.scheduleDao.getScheduleById(schedule1Modified.id)
        MatcherAssert.assertThat<Schedule>(loaded as Schedule, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, `is`(schedule1Modified.id))
        MatcherAssert.assertThat(loaded.title, `is`(schedule1Modified.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(schedule1Modified.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(schedule1Modified.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(schedule1Modified.imageUrl))
        MatcherAssert.assertThat(loaded.dirty, `is`(false))
    }

    @Test
    fun dirtySchedules_InsertOneAndDeleteDirty_ExpectsOneSchedule() = runBlockingTest {
        // GIVEN - multiple schedules and marked dirty
        database.scheduleDao.insertAll(listOf(schedule1, schedule3))
        database.scheduleDao.markDirty()

        // WHEN - Insert schedule 2
        database.scheduleDao.insert(schedule2)
        database.scheduleDao.deleteDirty()

        // THEN - Get all schedules. Expect only schedule 2 is returned
        val loaded = database.scheduleDao.getSchedules()
        MatcherAssert.assertThat(loaded.size, `is`(1))
        val loadedSchedule = loaded[0]
        MatcherAssert.assertThat<Schedule>(loadedSchedule as Schedule, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loadedSchedule.id, `is`(schedule2.id))
        MatcherAssert.assertThat(loadedSchedule.title, `is`(schedule2.title))
        MatcherAssert.assertThat(loadedSchedule.subtitle, `is`(schedule2.subtitle))
        MatcherAssert.assertThat(loadedSchedule.date, `is`(schedule2.date))
        MatcherAssert.assertThat(loadedSchedule.imageUrl, `is`(schedule2.imageUrl))
        MatcherAssert.assertThat(loadedSchedule.dirty, `is`(false))
    }

    @After
    fun closeDb() = database.close()
}