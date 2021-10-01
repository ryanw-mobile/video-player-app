/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.EventDaoTest.TestData.event1
import uk.ryanwong.dazn.codechallenge.data.model.EventDaoTest.TestData.event1Modified
import uk.ryanwong.dazn.codechallenge.data.model.EventDaoTest.TestData.event2
import uk.ryanwong.dazn.codechallenge.data.model.EventDaoTest.TestData.event3
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import uk.ryanwong.dazn.codechallenge.data.model.ScheduleDaoTest.TestData.schedule1
import uk.ryanwong.dazn.codechallenge.data.model.ScheduleDaoTest.TestData.schedule1Modified
import uk.ryanwong.dazn.codechallenge.data.model.ScheduleDaoTest.TestData.schedule2
import uk.ryanwong.dazn.codechallenge.data.model.ScheduleDaoTest.TestData.schedule3
import uk.ryanwong.dazn.codechallenge.data.source.DaznApiDaos
import uk.ryanwong.dazn.codechallenge.util.parseTimeStamp

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class DaznRoomDbDataSourceTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localDataSource: DaznRoomDbDataSource
    private lateinit var database: DaznApiDatabase

    // Test data set
    object TestData {

        val event1 = Event(
            1,
            "Liverpool v Porto",
            "UEFA Champions League",
            "2021-09-28T01:55:56.925Z".parseTimeStamp(),
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310176837169_image-header_pDach_1554579780000.jpeg?alt=media&token=1777d26b-d051-4b5f-87a8-7633d3d6dd20",
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
        )

        val event1Modified = Event(
            1,
            "Nîmes v Rennes",
            "Ligue 1",
            "2021-09-28T02:55:56.925Z".parseTimeStamp(),
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310381637057_image-header_pDach_1554664873000.jpeg?alt=media&token=53616931-55a8-476e-b1b7-d18fc22a2bf0",
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
        )

        val event2 = Event(
            2,
            "Nîmes v Rennes",
            "Ligue 1",
            "2021-09-28T02:55:56.925Z".parseTimeStamp(),
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310381637057_image-header_pDach_1554664873000.jpeg?alt=media&token=53616931-55a8-476e-b1b7-d18fc22a2bf0",
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
        )

        val event3 = Event(
            3,
            "Tottenham v Man City",
            "UEFA Champions League",
            "2021-09-28T03:55:56.925Z".parseTimeStamp(),
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310511685198_image-header_pDach_1554872450000.jpeg?alt=media&token=5524d719-261e-49e6-abf3-a74c30df3e27",
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
        )

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
    fun setup() {
        // Using an in-memory database for testing, because it doesn't survive killing the process.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DaznApiDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        val daznApiDaos = DaznApiDaos(database.eventsDao, database.scheduleDao)
        localDataSource = DaznRoomDbDataSource(daznApiDaos, Dispatchers.Main)
    }

    // Test coverage: basic CRUD on both events and schedule

    // runBlocking is used here because of https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    // TODO: Replace with runBlockingTest once issue is resolved
    @Test
    fun emptyList_SyncOneEvent_ExpectsOneEvent() = runBlocking {
        // GIVEN - An empty list
        val initialList = localDataSource.getEvents() as ApiResult.Success
        MatcherAssert.assertThat(initialList.data.size, Is.`is`(0))

        // WHEN  - Sync events by supplying one event
        localDataSource.syncEvents(listOf(event1))

        // THEN - One event is returned
        val resultList = localDataSource.getEvents() as ApiResult.Success
        MatcherAssert.assertThat(resultList.data.size, Is.`is`(1))

        val event = resultList.data[0]
        MatcherAssert.assertThat(event.id, Is.`is`(event1.id))
        MatcherAssert.assertThat(event.title, Is.`is`(event1.title))
        MatcherAssert.assertThat(event.subtitle, Is.`is`(event1.subtitle))
        MatcherAssert.assertThat(event.date, Is.`is`(event1.date))
        MatcherAssert.assertThat(event.imageUrl, Is.`is`(event1.imageUrl))
        MatcherAssert.assertThat(event.videoUrl, Is.`is`(event1.videoUrl))
    }

    @Test
    fun oneEvent_UpsertOneEvent_ExpectsEventUpdated() = runBlocking {
        // GIVEN - An empty list
        localDataSource.syncEvents(listOf(event1))

        // WHEN  - Sync events by supplying another event with same Id but different contents
        localDataSource.syncEvents(listOf(event1Modified))

        // THEN - One event is returned
        val resultList = localDataSource.getEvents() as ApiResult.Success
        MatcherAssert.assertThat(resultList.data.size, Is.`is`(1))

        // and the returned item should be the new event
        val event = resultList.data[0]
        MatcherAssert.assertThat(event.id, Is.`is`(event1Modified.id))
        MatcherAssert.assertThat(event.title, Is.`is`(event1Modified.title))
        MatcherAssert.assertThat(event.subtitle, Is.`is`(event1Modified.subtitle))
        MatcherAssert.assertThat(event.date, Is.`is`(event1Modified.date))
        MatcherAssert.assertThat(event.imageUrl, Is.`is`(event1Modified.imageUrl))
        MatcherAssert.assertThat(event.videoUrl, Is.`is`(event1Modified.videoUrl))
    }

    @Test
    fun threeEvents_SyncEmptyList_ExpectsNoEvents() = runBlocking {
        // GIVEN - An empty list
        localDataSource.syncEvents(listOf(event1, event2, event3))
        val initialList = localDataSource.getEvents() as ApiResult.Success
        MatcherAssert.assertThat(initialList.data.size, Is.`is`(3))

        // WHEN  - Sync events by supplying another event with same Id but different contents
        localDataSource.syncEvents(listOf())

        // THEN - One event is returned
        val resultList = localDataSource.getEvents() as ApiResult.Success
        MatcherAssert.assertThat(resultList.data.size, Is.`is`(0))
    }

    @Test
    fun emptyList_SyncOneSchedule_ExpectsOneSchedule() = runBlocking {
        // GIVEN - An empty list
        val initialList = localDataSource.getSchedules() as ApiResult.Success
        MatcherAssert.assertThat(initialList.data.size, Is.`is`(0))

        // WHEN  - Sync schedules by supplying one schedule
        localDataSource.syncSchedule(listOf(schedule1))

        // THEN - One event is returned
        val resultList = localDataSource.getSchedules() as ApiResult.Success
        MatcherAssert.assertThat(resultList.data.size, Is.`is`(1))

        val schedule = resultList.data[0]
        MatcherAssert.assertThat(schedule.id, Is.`is`(schedule1.id))
        MatcherAssert.assertThat(schedule.title, Is.`is`(schedule1.title))
        MatcherAssert.assertThat(schedule.subtitle, Is.`is`(schedule1.subtitle))
        MatcherAssert.assertThat(schedule.date, Is.`is`(schedule1.date))
        MatcherAssert.assertThat(schedule.imageUrl, Is.`is`(schedule1.imageUrl))
    }

    @Test
    fun oneSchedule_UpsertOneSchedule_ExpectsScheduleUpdated() = runBlocking {
        // GIVEN - An empty list
        localDataSource.syncSchedule(listOf(schedule1))

        // WHEN - Sync schedules by supplying another schedule with same Id but different contents
        localDataSource.syncSchedule(listOf(schedule1Modified))

        // THEN - One schedule item is returned
        val resultList = localDataSource.getSchedules() as ApiResult.Success
        MatcherAssert.assertThat(resultList.data.size, Is.`is`(1))

        // and the returned item should be the new schedule
        val schedule = resultList.data[0]
        MatcherAssert.assertThat(schedule.id, Is.`is`(schedule1Modified.id))
        MatcherAssert.assertThat(schedule.title, Is.`is`(schedule1Modified.title))
        MatcherAssert.assertThat(schedule.subtitle, Is.`is`(schedule1Modified.subtitle))
        MatcherAssert.assertThat(schedule.date, Is.`is`(schedule1Modified.date))
        MatcherAssert.assertThat(schedule.imageUrl, Is.`is`(schedule1Modified.imageUrl))
    }

    @Test
    fun threeSchedules_SyncEmptyList_ExpectsNoSchedules() = runBlocking {
        // GIVEN - An empty list
        localDataSource.syncSchedule(listOf(schedule1, schedule2, schedule3))
        val initialList = localDataSource.getSchedules() as ApiResult.Success
        MatcherAssert.assertThat(initialList.data.size, Is.`is`(3))

        // WHEN  - Sync schedules by supplying another schedule with same Id but different contents
        localDataSource.syncSchedule(listOf())

        // THEN - One schedule is returned
        val resultList = localDataSource.getSchedules() as ApiResult.Success
        MatcherAssert.assertThat(resultList.data.size, Is.`is`(0))
    }

    @After
    fun cleanUp() {
        database.close()
    }
}