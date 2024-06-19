/*
* Copyright (c) 2024. Ryan Wong
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

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun refreshEvents_ShouldReturnFailure_WhenRemoteThrowsException() = runTest {
        remoteDataSource.setExceptionForTest(exception = IOException())

        val result = localCacheRepository.refreshEvents()

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe IOException()
    }

    @Test
    fun refreshEvents_ShouldReturnSuccess_WhenRemoteReturnsEmptyList() = runTest {
        val remoteEvents = emptyList<EventNetworkDto>()
        remoteDataSource.setEventsForTest(remoteEvents)

        val result = localCacheRepository.refreshEvents()

        result.isSuccess shouldBe true
    }

    @Test
    fun refreshEvents_ShouldReturnSuccess_WhenRemoteReturnsData() = runTest {
        val remoteEvents = listOf(event1, event2, event3)
        remoteDataSource.setEventsForTest(remoteEvents)

        val result = localCacheRepository.refreshEvents()

        result.isSuccess shouldBe true
    }

    @Test
    fun refreshEvents_ShouldCacheRemoteEvents_WhenDataSourceReturnsData() = runTest {
        val remoteEvents = listOf(event1, event2, event3)
        remoteDataSource.setEventsForTest(remoteEvents)

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        refreshedEvents.isSuccess shouldBe true
        refreshedEvents.getOrNull() shouldContainExactlyInAnyOrder remoteEvents.map { it.toEvent() }
    }

    @Test
    fun refreshEvents_ShouldOverwriteCachedEvents_WhenDataSourceReturnsData() = runTest {
        val remoteEvents = listOf(event1, event2)
        remoteDataSource.setEventsForTest(remoteEvents)
        localDataSource.submitEvents(listOf(EventDbEntitySampleData.event3))

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        refreshedEvents.isSuccess shouldBe true
        refreshedEvents.getOrNull() shouldContainExactlyInAnyOrder remoteEvents.map { it.toEvent() }
    }

    @Test
    fun refreshEvents_ShouldClearCachedEvents_WhenRemoteReturnsEmptyList() = runTest {
        val remoteEvents = emptyList<EventNetworkDto>()
        remoteDataSource.setEventsForTest(remoteEvents)
        localDataSource.submitEvents(listOf(EventDbEntitySampleData.event3))

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        refreshedEvents.isSuccess shouldBe true
        refreshedEvents.getOrNull() shouldBe emptyList()
    }

    @Test
    fun refreshEvents_ShouldRetainCachedEvents_WhenRemoteThrowsException() = runTest {
        val localEvents = listOf(EventDbEntitySampleData.event1, EventDbEntitySampleData.event2, EventDbEntitySampleData.event3)
        remoteDataSource.setExceptionForTest(exception = IOException())
        localDataSource.submitEvents(localEvents)

        localCacheRepository.refreshEvents()
        val refreshedEvents = localCacheRepository.getEvents()

        refreshedEvents.isSuccess shouldBe true
        refreshedEvents.getOrNull() shouldContainExactlyInAnyOrder localEvents.map { it.toEvent() }
    }

    @Test
    fun getEvents_ShouldReturnCachedEvents_WithoutRefresh() = runTest {
        val localEvents = listOf(EventDbEntitySampleData.event1, EventDbEntitySampleData.event2, EventDbEntitySampleData.event3)
        localDataSource.submitEvents(localEvents)

        val cachedEvents = localCacheRepository.getEvents()

        cachedEvents.isSuccess shouldBe true
        cachedEvents.getOrNull() shouldContainExactlyInAnyOrder localEvents.map { it.toEvent() }
    }

    // Schedules - the same set of tests but different objects
    @Test
    fun refreshSchedule_ShouldReturnFailure_WhenRemoteThrowsException() = runTest {
        remoteDataSource.setExceptionForTest(exception = IOException())

        val result = localCacheRepository.refreshSchedule()

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe IOException()
    }

    @Test
    fun refreshSchedule_ShouldReturnSuccess_WhenRemoteReturnsEmptyList() = runTest {
        val remoteSchedule = emptyList<ScheduleNetworkDto>()
        remoteDataSource.setScheduleForTest(remoteSchedule)

        val result = localCacheRepository.refreshSchedule()

        result.isSuccess shouldBe true
    }

    @Test
    fun refreshSchedule_ShouldReturnSuccess_WhenRemoteReturnsData() = runTest {
        val remoteSchedule = listOf(schedule1, schedule2, schedule3)
        remoteDataSource.setScheduleForTest(remoteSchedule)

        val result = localCacheRepository.refreshSchedule()

        result.isSuccess shouldBe true
    }

    @Test
    fun refreshSchedule_ShouldCacheRemoteSchedules_WhenDataSourceReturnsData() = runTest {
        val remoteSchedule = listOf(schedule1, schedule2, schedule3)
        remoteDataSource.setScheduleForTest(remoteSchedule)

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        refreshedSchedule.isSuccess shouldBe true
        refreshedSchedule.getOrNull() shouldContainExactlyInAnyOrder remoteSchedule.map { it.toSchedule() }
    }

    @Test
    fun refreshSchedule_ShouldOverwriteCachedSchedules_WhenDataSourceReturnsData() = runTest {
        val remoteSchedule = listOf(schedule1, schedule2)
        remoteDataSource.setScheduleForTest(remoteSchedule)
        localDataSource.submitSchedule(listOf(ScheduleDbEntitySampleData.schedule3))

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        refreshedSchedule.isSuccess shouldBe true
        refreshedSchedule.getOrNull() shouldContainExactlyInAnyOrder remoteSchedule.map { it.toSchedule() }
    }

    @Test
    fun refreshSchedule_ShouldClearCachedSchedules_WhenRemoteReturnsEmptyList() = runTest {
        val remoteSchedule = emptyList<ScheduleNetworkDto>()
        remoteDataSource.setScheduleForTest(remoteSchedule)
        localDataSource.submitSchedule(listOf(ScheduleDbEntitySampleData.schedule3))

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        refreshedSchedule.isSuccess shouldBe true
        refreshedSchedule.getOrNull() shouldBe emptyList()
    }

    @Test
    fun refreshSchedule_ShouldRetainCachedSchedules_WhenRemoteThrowsException() = runTest {
        val localSchedule = listOf(ScheduleDbEntitySampleData.schedule1, ScheduleDbEntitySampleData.schedule2, ScheduleDbEntitySampleData.schedule3)
        remoteDataSource.setExceptionForTest(exception = IOException())
        localDataSource.submitSchedule(localSchedule)

        localCacheRepository.refreshSchedule()
        val refreshedSchedule = localCacheRepository.getSchedule()

        refreshedSchedule.isSuccess shouldBe true
        refreshedSchedule.getOrNull() shouldContainExactlyInAnyOrder localSchedule.map { it.toSchedule() }
    }

    @Test
    fun getSchedule_ShouldReturnCachedSchedules_WithoutRefresh() = runTest {
        val localSchedule = listOf(ScheduleDbEntitySampleData.schedule1, ScheduleDbEntitySampleData.schedule2, ScheduleDbEntitySampleData.schedule3)
        localDataSource.submitSchedule(localSchedule)

        val cachedSchedule = localCacheRepository.getSchedule()

        cachedSchedule.isSuccess shouldBe true
        cachedSchedule.getOrNull() shouldContainExactlyInAnyOrder localSchedule.map { it.toSchedule() }
    }
}
