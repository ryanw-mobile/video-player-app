/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ryanwong.dazn.codechallenge.TestData.event1
import uk.ryanwong.dazn.codechallenge.TestData.event1Modified
import uk.ryanwong.dazn.codechallenge.TestData.event2
import uk.ryanwong.dazn.codechallenge.TestData.event3
import uk.ryanwong.dazn.codechallenge.TestData.schedule1
import uk.ryanwong.dazn.codechallenge.TestData.schedule1Modified
import uk.ryanwong.dazn.codechallenge.TestData.schedule2
import uk.ryanwong.dazn.codechallenge.TestData.schedule3
import uk.ryanwong.dazn.codechallenge.base.BaseLocalDataSource
import uk.ryanwong.dazn.codechallenge.di.ProvideRoomDbDataSource
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@MediumTest
class RoomDbDataSourceTest {

    // Use the real data source
    @Inject
    @ProvideRoomDbDataSource
    lateinit var localDataSource: BaseLocalDataSource

    @Inject
    lateinit var database: DaznApiDatabase

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    // Test coverage: basic CRUD on both events and schedule

    // runBlocking is used here because of https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    // TODO: Replace with runBlockingTest once issue is resolved
    @Test
    fun emptyList_SyncOneEvent_ExpectsOneEvent() = runBlocking {
        // GIVEN - An empty list
        val initialList = localDataSource.getEvents()
        MatcherAssert.assertThat(initialList.size, `is`(0))

        // WHEN  - Sync events by supplying one event
        localDataSource.submitEvents(listOf(event1))

        // THEN - One event is returned
        val resultList = localDataSource.getEvents()
        MatcherAssert.assertThat(resultList.size, `is`(1))

        val event = resultList[0]
        MatcherAssert.assertThat(event.eventId, `is`(event1.eventId))
        MatcherAssert.assertThat(event.title, `is`(event1.title))
        MatcherAssert.assertThat(event.subtitle, `is`(event1.subtitle))
        MatcherAssert.assertThat(event.date, `is`(event1.date))
        MatcherAssert.assertThat(event.imageUrl, `is`(event1.imageUrl))
        MatcherAssert.assertThat(event.videoUrl, `is`(event1.videoUrl))
    }

    @Test
    fun oneEvent_UpsertOneEvent_ExpectsEventUpdated() = runBlocking {
        // GIVEN - An empty list
        localDataSource.submitEvents(listOf(event1))

        // WHEN  - Sync events by supplying another event with same Id but different contents
        localDataSource.submitEvents(listOf(event1Modified))

        // THEN - One event is returned
        val resultList = localDataSource.getEvents()
        MatcherAssert.assertThat(resultList.size, `is`(1))

        // and the returned item should be the new event
        val event = resultList[0]
        MatcherAssert.assertThat(event.eventId, `is`(event1Modified.eventId))
        MatcherAssert.assertThat(event.title, `is`(event1Modified.title))
        MatcherAssert.assertThat(event.subtitle, `is`(event1Modified.subtitle))
        MatcherAssert.assertThat(event.date, `is`(event1Modified.date))
        MatcherAssert.assertThat(event.imageUrl, `is`(event1Modified.imageUrl))
        MatcherAssert.assertThat(event.videoUrl, `is`(event1Modified.videoUrl))
    }

    @Test
    fun threeEvents_SyncEmptyList_ExpectsNoEvents() = runBlocking {
        // GIVEN - An empty list
        localDataSource.submitEvents(listOf(event1, event2, event3))
        val initialList = localDataSource.getEvents()
        MatcherAssert.assertThat(initialList.size, `is`(3))

        // WHEN  - Sync events by supplying another event with same Id but different contents
        localDataSource.submitEvents(listOf())

        // THEN - One event is returned
        val resultList = localDataSource.getEvents()
        MatcherAssert.assertThat(resultList.size, `is`(0))
    }

    @Test
    fun emptyList_SyncOneSchedule_ExpectsOneSchedule() = runBlocking {
        // GIVEN - An empty list
        val initialList = localDataSource.getSchedules()
        MatcherAssert.assertThat(initialList.size, `is`(0))

        // WHEN  - Sync schedules by supplying one schedule
        localDataSource.submitSchedule(listOf(schedule1))

        // THEN - One event is returned
        val resultList = localDataSource.getSchedules()
        MatcherAssert.assertThat(resultList.size, `is`(1))

        val schedule = resultList[0]
        MatcherAssert.assertThat(schedule.scheduleId, `is`(schedule1.scheduleId))
        MatcherAssert.assertThat(schedule.title, `is`(schedule1.title))
        MatcherAssert.assertThat(schedule.subtitle, `is`(schedule1.subtitle))
        MatcherAssert.assertThat(schedule.date, `is`(schedule1.date))
        MatcherAssert.assertThat(schedule.imageUrl, `is`(schedule1.imageUrl))
    }

    @Test
    fun oneSchedule_UpsertOneSchedule_ExpectsScheduleUpdated() = runBlocking {
        // GIVEN - An empty list
        localDataSource.submitSchedule(listOf(schedule1))

        // WHEN - Sync schedules by supplying another schedule with same Id but different contents
        localDataSource.submitSchedule(listOf(schedule1Modified))

        // THEN - One schedule item is returned
        val resultList = localDataSource.getSchedules()
        MatcherAssert.assertThat(resultList.size, `is`(1))

        // and the returned item should be the new schedule
        val schedule = resultList[0]
        MatcherAssert.assertThat(schedule.scheduleId, `is`(schedule1Modified.scheduleId))
        MatcherAssert.assertThat(schedule.title, `is`(schedule1Modified.title))
        MatcherAssert.assertThat(schedule.subtitle, `is`(schedule1Modified.subtitle))
        MatcherAssert.assertThat(schedule.date, `is`(schedule1Modified.date))
        MatcherAssert.assertThat(schedule.imageUrl, `is`(schedule1Modified.imageUrl))
    }

    @Test
    fun threeSchedules_SyncEmptyList_ExpectsNoSchedules() = runBlocking {
        // GIVEN - An empty list
        localDataSource.submitSchedule(listOf(schedule1, schedule2, schedule3))
        val initialList = localDataSource.getSchedules()
        MatcherAssert.assertThat(initialList.size, `is`(3))

        // WHEN  - Sync schedules by supplying another schedule with same Id but different contents
        localDataSource.submitSchedule(listOf())

        // THEN - One schedule is returned
        val resultList = localDataSource.getSchedules()
        MatcherAssert.assertThat(resultList.size, `is`(0))
    }

    @After
    fun cleanUp() {
        database.close()
    }
}