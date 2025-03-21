/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.repository

import com.rwmobi.dazncodechallenge.data.repository.mapper.toEvent
import com.rwmobi.dazncodechallenge.data.repository.mapper.toSchedule
import com.rwmobi.dazncodechallenge.data.source.local.FakeLocalDataSource
import com.rwmobi.dazncodechallenge.data.source.network.dto.EventNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.dto.ScheduleNetworkDto
import com.rwmobi.dazncodechallenge.data.source.remote.FakeRemoteDataSource
import com.rwmobi.dazncodechallenge.test.EventDbEntitySampleData
import com.rwmobi.dazncodechallenge.test.EventNetworkDtoSampleData.event1
import com.rwmobi.dazncodechallenge.test.EventNetworkDtoSampleData.event2
import com.rwmobi.dazncodechallenge.test.EventNetworkDtoSampleData.event3
import com.rwmobi.dazncodechallenge.test.ScheduleDbEntitySampleData
import com.rwmobi.dazncodechallenge.test.ScheduleNetworkDtoSampleData.schedule1
import com.rwmobi.dazncodechallenge.test.ScheduleNetworkDtoSampleData.schedule2
import com.rwmobi.dazncodechallenge.test.ScheduleNetworkDtoSampleData.schedule3
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

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

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `returns failure when refreshEvents encounters remote exception`() = runTest {
        remoteDataSource.setExceptionForTest(exception = IOException())

        val result = localCacheRepository.refreshEvents()

        assertTrue(result.isFailure)
        assertFailsWith<IOException> {
            throw result.exceptionOrNull()!!
        }
    }

    @Test
    fun `returns success when refreshEvents receives empty list from remote`() = runTest {
        val remoteEvents = emptyList<EventNetworkDto>()
        remoteDataSource.setEventsForTest(remoteEvents)

        val result = localCacheRepository.refreshEvents()

        assertTrue(result.isSuccess)
    }

    @Test
    fun `returns success when refreshEvents receives data from remote`() = runTest {
        val remoteEvents = listOf(event1, event2, event3)
        remoteDataSource.setEventsForTest(remoteEvents)

        val result = localCacheRepository.refreshEvents()

        assertTrue(result.isSuccess)
    }

    @Test
    fun `caches remote events when refreshEvents receives data`() = runTest {
        val remoteEvents = listOf(event1, event2, event3)
        remoteDataSource.setEventsForTest(remoteEvents)

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        assertTrue(refreshedEvents.isSuccess)
        assertEquals(remoteEvents.map { it.toEvent() }, refreshedEvents.getOrNull())
    }

    @Test
    fun `overwrites cached events when refreshEvents receives data`() = runTest {
        val remoteEvents = listOf(event1, event2)
        remoteDataSource.setEventsForTest(remoteEvents)
        localDataSource.submitEvents(listOf(EventDbEntitySampleData.event3))

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        assertTrue(refreshedEvents.isSuccess)
        assertEquals(remoteEvents.map { it.toEvent() }, refreshedEvents.getOrNull())
    }

    @Test
    fun `clears cached events when refreshEvents receives empty list from remote`() = runTest {
        val remoteEvents = emptyList<EventNetworkDto>()
        remoteDataSource.setEventsForTest(remoteEvents)
        localDataSource.submitEvents(listOf(EventDbEntitySampleData.event3))

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        assertTrue(refreshedEvents.isSuccess)
        assertEquals(emptyList(), refreshedEvents.getOrNull())
    }

    @Test
    fun `retains cached events when refreshEvents encounters remote exception`() = runTest {
        val localEvents = listOf(EventDbEntitySampleData.event1, EventDbEntitySampleData.event2, EventDbEntitySampleData.event3)
        remoteDataSource.setExceptionForTest(exception = IOException())
        localDataSource.submitEvents(localEvents)

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        assertTrue(refreshedEvents.isSuccess)
        assertEquals(localEvents.map { it.toEvent() }, refreshedEvents.getOrNull())
    }

    @Test
    fun `returns cached events when getEvents is called without refresh`() = runTest {
        val localEvents = listOf(EventDbEntitySampleData.event1, EventDbEntitySampleData.event2, EventDbEntitySampleData.event3)
        localDataSource.submitEvents(localEvents)

        val cachedEvents = localCacheRepository.getEvents()

        assertTrue(cachedEvents.isSuccess)
        assertEquals(localEvents.map { it.toEvent() }, cachedEvents.getOrNull())
    }

    // Schedules - the same set of tests but different objects
    @Test
    fun `returns failure when refreshSchedule encounters remote exception`() = runTest {
        remoteDataSource.setExceptionForTest(exception = IOException())

        val result = localCacheRepository.refreshSchedule()

        assertTrue(result.isFailure)
        assertFailsWith<IOException> {
            throw result.exceptionOrNull()!!
        }
    }

    @Test
    fun `returns success when refreshSchedule receives empty list from remote`() = runTest {
        val remoteSchedule = emptyList<ScheduleNetworkDto>()
        remoteDataSource.setScheduleForTest(remoteSchedule)

        val result = localCacheRepository.refreshSchedule()

        assertTrue(result.isSuccess)
    }

    @Test
    fun `returns success when refreshSchedule receives data from remote`() = runTest {
        val remoteSchedule = listOf(schedule1, schedule2, schedule3)
        remoteDataSource.setScheduleForTest(remoteSchedule)

        val result = localCacheRepository.refreshSchedule()

        assertTrue(result.isSuccess)
    }

    @Test
    fun `caches remote schedules when refreshSchedule receives data`() = runTest {
        val remoteSchedule = listOf(schedule1, schedule2, schedule3)
        remoteDataSource.setScheduleForTest(remoteSchedule)

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        assertTrue(refreshedSchedule.isSuccess)
        assertEquals(remoteSchedule.map { it.toSchedule() }, refreshedSchedule.getOrNull())
    }

    @Test
    fun `overwrites cached schedules when refreshSchedule receives data`() = runTest {
        val remoteSchedule = listOf(schedule1, schedule2)
        remoteDataSource.setScheduleForTest(remoteSchedule)
        localDataSource.submitSchedule(listOf(ScheduleDbEntitySampleData.schedule3))

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        assertTrue(refreshedSchedule.isSuccess)
        assertEquals(remoteSchedule.map { it.toSchedule() }, refreshedSchedule.getOrNull())
    }

    @Test
    fun `clears cached schedules when refreshSchedule receives empty list from remote`() = runTest {
        val remoteSchedule = emptyList<ScheduleNetworkDto>()
        remoteDataSource.setScheduleForTest(remoteSchedule)
        localDataSource.submitSchedule(listOf(ScheduleDbEntitySampleData.schedule3))

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        assertTrue(refreshedSchedule.isSuccess)
        assertEquals(emptyList(), refreshedSchedule.getOrNull())
    }

    @Test
    fun `retains cached schedules when refreshSchedule encounters remote exception`() = runTest {
        val localSchedule = listOf(ScheduleDbEntitySampleData.schedule1, ScheduleDbEntitySampleData.schedule2, ScheduleDbEntitySampleData.schedule3)
        remoteDataSource.setExceptionForTest(exception = IOException())
        localDataSource.submitSchedule(localSchedule)

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        assertTrue(refreshedSchedule.isSuccess)
        assertEquals(localSchedule.map { it.toSchedule() }, refreshedSchedule.getOrNull())
    }

    @Test
    fun `returns cached schedules when getSchedule is called without refresh`() = runTest {
        val localSchedule = listOf(ScheduleDbEntitySampleData.schedule1, ScheduleDbEntitySampleData.schedule2, ScheduleDbEntitySampleData.schedule3)
        localDataSource.submitSchedule(localSchedule)

        val cachedSchedule = localCacheRepository.getSchedule()

        assertTrue(cachedSchedule.isSuccess)
        assertEquals(localSchedule.map { it.toSchedule() }, cachedSchedule.getOrNull())
    }
}
