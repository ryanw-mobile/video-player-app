/*
* Copyright (c) 2024. Ryan Wong
* https://github.com/ryanw-mobile
* Sponsored by RW MobiMedia UK Limited
*
*/

package com.rwmobi.dazncodechallenge.data.repository

import com.rwmobi.dazncodechallenge.data.source.local.FakeLocalDataSource
import com.rwmobi.dazncodechallenge.data.source.remote.FakeRemoteDataSource
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule
import com.rwmobi.dazncodechallenge.test.EventSampleData.event1
import com.rwmobi.dazncodechallenge.test.EventSampleData.event2
import com.rwmobi.dazncodechallenge.test.EventSampleData.event3
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule1
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule2
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData.schedule3
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
internal class LocalCacheRepositoryTest {
    private lateinit var remoteDataSource: FakeRemoteDataSource
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var localCacheRepository: LocalCacheRepository

    @Before
    fun createRepository() {
        remoteDataSource = FakeRemoteDataSource()
        localDataSource = FakeLocalDataSource()

        // subject under test
        localCacheRepository = LocalCacheRepository(
            networkDataSource = remoteDataSource,
            localDataSource = localDataSource,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `Should return failure when refreshEvents and remote returned an exception`() = runTest {
        remoteDataSource.setExceptionForTest(exception = IOException())

        val result = localCacheRepository.refreshEvents()

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe IOException()
    }

    @Test
    fun `Should return success when refreshEvents and remote returned empty list`() = runTest {
        val remoteEvents = emptyList<Event>()
        remoteDataSource.setEventsForTest(remoteEvents)

        val result = localCacheRepository.refreshEvents()

        result.isSuccess shouldBe true
    }

    @Test
    fun `Should return success when refreshEvents and remote returned data`() = runTest {
        val remoteEvents = listOf(event1, event2, event3)
        remoteDataSource.setEventsForTest(remoteEvents)

        val result = localCacheRepository.refreshEvents()

        result.isSuccess shouldBe true
    }

    @Test
    fun `Should cache remote events when refreshEvents and data source returns data`() = runTest {
        val remoteEvents = listOf(event1, event2, event3)
        remoteDataSource.setEventsForTest(remoteEvents)

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        refreshedEvents.isSuccess shouldBe true
        refreshedEvents.getOrNull() shouldContainExactlyInAnyOrder remoteEvents
    }

    @Test
    fun `Should overwrite cached events when refreshEvents and data source returns data`() = runTest {
        val remoteEvents = listOf(event1, event2)
        remoteDataSource.setEventsForTest(remoteEvents)
        localDataSource.submitEvents(listOf(event3))

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        refreshedEvents.isSuccess shouldBe true
        refreshedEvents.getOrNull() shouldContainExactlyInAnyOrder remoteEvents
    }

    @Test
    fun `Should clear cached events when refreshEvents and remote returns empty list`() = runTest {
        val remoteEvents = emptyList<Event>()
        remoteDataSource.setEventsForTest(remoteEvents)
        localDataSource.submitEvents(listOf(event3))

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        refreshedEvents.isSuccess shouldBe true
        refreshedEvents.getOrNull() shouldBe emptyList()
    }

    @Test
    fun `Should retain cached events when refreshEvents and remote returns exception`() = runTest {
        val localEvents = listOf(event1, event2, event3)
        remoteDataSource.setExceptionForTest(exception = IOException())
        localDataSource.submitEvents(localEvents)

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        refreshedEvents.isSuccess shouldBe true
        refreshedEvents.getOrNull() shouldContainExactlyInAnyOrder localEvents
    }

    @Test
    fun `Should return cached events without refreshEvents`() = runTest {
        val localEvents = listOf(event1, event2, event3)
        localDataSource.submitEvents(localEvents)

        val cachedEvents = localCacheRepository.getEvents()

        cachedEvents.isSuccess shouldBe true
        cachedEvents.getOrNull() shouldContainExactlyInAnyOrder localEvents
    }

    // Schedules - the same set of tests but different objects
    @Test
    fun `Should return failure when refreshSchedule and remote returned an exception`() = runTest {
        remoteDataSource.setExceptionForTest(exception = IOException())

        val result = localCacheRepository.refreshSchedule()

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe IOException()
    }

    @Test
    fun `Should return success when refreshSchedule and remote returned empty list`() = runTest {
        val remoteSchedule = emptyList<Schedule>()
        remoteDataSource.setScheduleForTest(remoteSchedule)

        val result = localCacheRepository.refreshSchedule()

        result.isSuccess shouldBe true
    }

    @Test
    fun `Should return success when refreshSchedule and remote returned data`() = runTest {
        val remoteSchedule = listOf(schedule1, schedule2, schedule3)
        remoteDataSource.setScheduleForTest(remoteSchedule)

        val result = localCacheRepository.refreshSchedule()

        result.isSuccess shouldBe true
    }

    @Test
    fun `Should cache remote Schedule when refreshSchedule and data source returns data`() = runTest {
        val remoteSchedule = listOf(schedule1, schedule2, schedule3)
        remoteDataSource.setScheduleForTest(remoteSchedule)

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        refreshedSchedule.isSuccess shouldBe true
        refreshedSchedule.getOrNull() shouldContainExactlyInAnyOrder remoteSchedule
    }

    @Test
    fun `Should overwrite cached Schedule when refreshSchedule and data source returns data`() = runTest {
        val remoteSchedule = listOf(schedule1, schedule2)
        remoteDataSource.setScheduleForTest(remoteSchedule)
        localDataSource.submitSchedule(listOf(schedule3))

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        refreshedSchedule.isSuccess shouldBe true
        refreshedSchedule.getOrNull() shouldContainExactlyInAnyOrder remoteSchedule
    }

    @Test
    fun `Should clear cached Schedule when refreshSchedule and remote returns empty list`() = runTest {
        val remoteSchedule = emptyList<Schedule>()
        remoteDataSource.setScheduleForTest(remoteSchedule)
        localDataSource.submitSchedule(listOf(schedule3))

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        refreshedSchedule.isSuccess shouldBe true
        refreshedSchedule.getOrNull() shouldBe emptyList()
    }

    @Test
    fun `Should retain cached Schedule when refreshSchedule and remote returns exception`() = runTest {
        val localSchedule = listOf(schedule1, schedule2, schedule3)
        remoteDataSource.setExceptionForTest(exception = IOException())
        localDataSource.submitSchedule(localSchedule)

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        refreshedSchedule.isSuccess shouldBe true
        refreshedSchedule.getOrNull() shouldContainExactlyInAnyOrder localSchedule
    }

    @Test
    fun `Should return cached Schedule without refreshSchedule`() = runTest {
        val localSchedule = listOf(schedule1, schedule2, schedule3)
        localDataSource.submitSchedule(localSchedule)

        val cachedSchedule = localCacheRepository.getSchedule()

        cachedSchedule.isSuccess shouldBe true
        cachedSchedule.getOrNull() shouldContainExactlyInAnyOrder localSchedule
    }
}
