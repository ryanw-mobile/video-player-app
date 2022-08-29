/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
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
import uk.ryanwong.dazn.codechallenge.TestData.event1
import uk.ryanwong.dazn.codechallenge.TestData.event1Modified
import uk.ryanwong.dazn.codechallenge.TestData.event2
import uk.ryanwong.dazn.codechallenge.TestData.event3
import uk.ryanwong.dazn.codechallenge.TestData.schedule1
import uk.ryanwong.dazn.codechallenge.TestData.schedule1Modified
import uk.ryanwong.dazn.codechallenge.TestData.schedule2
import uk.ryanwong.dazn.codechallenge.TestData.schedule3
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@MediumTest
internal class RoomDbDataSourceTest {

    // We test the data source, so we are using a REAL data source, not the fake one!
    @Inject
    lateinit var localDataSource: LocalDataSource

    // However we have silently replaced the database with an in-memory database
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

    // runTest has replaced runBlockingTest
    // see: https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-test#runtest
    @Test
    fun emptyList_SyncOneEvent_ReturnOneEvent() = runTest {
        // GIVEN - An empty list -- nothing to do

        // WHEN  - Sync events by supplying one event
        localDataSource.submitEvents(listOf(event1))

        // THEN - One event is returned
        val resultList = localDataSource.getEvents()
        assertThat(resultList).containsExactly(event1)
    }

    @Test
    fun oneEvent_UpsertOneEvent_ReturnEventUpdated() = runTest {
        // GIVEN - An empty list
        localDataSource.submitEvents(listOf(event1))

        // WHEN  - Sync events by supplying another event with same Id but different contents
        localDataSource.submitEvents(listOf(event1Modified))

        // THEN - One event is returned, and the returned item should be the new event
        val resultList = localDataSource.getEvents()
        assertThat(resultList).containsExactly(event1Modified)
    }

    @Test
    fun threeEvents_SyncEmptyList_ReturnNoEvents() = runTest {
        // GIVEN - An empty list
        localDataSource.submitEvents(listOf(event1, event2, event3))
        val initialList = localDataSource.getEvents()

        // WHEN  - Sync events by supplying another event with same Id but different contents
        localDataSource.submitEvents(listOf())

        // THEN - One event is returned
        val resultList = localDataSource.getEvents()
        assertThat(resultList).hasSize(0)
    }

    @Test
    fun emptyList_SyncOneSchedule_ReturnOneSchedule() = runTest {
        // GIVEN - An empty list
        val initialList = localDataSource.getSchedules()

        // WHEN  - Sync schedules by supplying one schedule
        localDataSource.submitSchedule(listOf(schedule1))

        // THEN - One event is returned
        val resultList = localDataSource.getSchedules()
        assertThat(resultList).containsExactly(schedule1)
    }

    @Test
    fun oneSchedule_UpsertOneSchedule_ReturnScheduleUpdated() = runTest {
        // GIVEN - An empty list
        localDataSource.submitSchedule(listOf(schedule1))

        // WHEN - Sync schedules by supplying another schedule with same Id but different contents
        localDataSource.submitSchedule(listOf(schedule1Modified))

        // THEN - One schedule item is returned, and the returned item should be the new schedule
        val resultList = localDataSource.getSchedules()
        assertThat(resultList).containsExactly(schedule1Modified)
    }

    @Test
    fun threeSchedules_SyncEmptyList_ReturnNoSchedules() = runTest {
        // GIVEN - An empty list
        localDataSource.submitSchedule(listOf(schedule1, schedule2, schedule3))
        val initialList = localDataSource.getSchedules()

        // WHEN - Sync schedules by supplying another schedule with same Id but different contents
        localDataSource.submitSchedule(listOf())

        // THEN - schedule list is empty
        val resultList = localDataSource.getSchedules()
        assertThat(resultList).hasSize(0)
    }

    @After
    fun cleanUp() {
        database.close()
    }
}
