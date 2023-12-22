/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import uk.ryanwong.dazn.codechallenge.TestData
import uk.ryanwong.dazn.codechallenge.TestData.event1
import uk.ryanwong.dazn.codechallenge.TestData.event2
import uk.ryanwong.dazn.codechallenge.TestData.event3
import uk.ryanwong.dazn.codechallenge.data.source.local.FakeLocalDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.FakeRemoteDataSource
import java.io.IOException

@ExperimentalCoroutinesApi
internal class LocalCacheRepositoryTest {

    private val remoteEvents = mutableListOf(event1, event2, event3).apply { sortBy { it.date } }

    private val localEvents = mutableListOf(event1)

    private val remoteSchedules = mutableListOf(
        TestData.schedule1,
        TestData.schedule2,
        TestData.schedule3
    ).apply {
        sortBy { it.date }
    }
    private val localSchedules = mutableListOf(TestData.schedule1)

    private lateinit var remoteDataSource: FakeRemoteDataSource
    private lateinit var localDataSource: FakeLocalDataSource

    // Class under test
    private lateinit var localCacheRepository: LocalCacheRepository

    @Before
    fun createRepository() {
        remoteDataSource =
            FakeRemoteDataSource(remoteEvents, remoteSchedules)
        localDataSource =
            FakeLocalDataSource(localEvents, localSchedules)

        // Get a reference to the class under test
        localCacheRepository = LocalCacheRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun nonEmptyLocal_syncEvents_updatedEvents() = runTest {
        // Given the localDataSource has event1
        // Given the remoteDataSource has event1, event2, event3
        val initEvents = localCacheRepository.getEvents()

        // When repository refreshEvents
        localCacheRepository.refreshEvents()

        // Then repository should now return 3 events
        val refreshedEvents = localCacheRepository.getEvents()
        assertThat(refreshedEvents).isEqualTo(remoteEvents)
    }

    @Test
    fun nonEmptyLocal_syncEmptyRemoteEvents_emptyLocalEvents() = runTest {
        // Given the localDataSource has event1
        // Given the remoteDataSource is empty list
        remoteDataSource.setEvents(emptyList())
        val initEvents = localCacheRepository.getEvents()

        // When repository refreshEvents
        localCacheRepository.refreshEvents()

        // Then repository should now return 0 schedules
        val refreshedEvents = localCacheRepository.getEvents()
        assertThat(refreshedEvents).isEmpty()
    }

    @Test
    fun errorRemote_syncEvents_throwException() = runTest {
        // Given the remoteDataSource set to return error
        val exceptionMessage = "test exception"
        remoteDataSource.setShouldReturnIOException(true, exceptionMessage)

        // When repository refreshEvents
        try {
            localCacheRepository.refreshEvents()
            fail()

            // Then repository should throw the exception
        } catch (ex: IOException) {
            assertThat(ex.message).isEqualTo(exceptionMessage)
        }
    }

    // Tests for schedule

    @Test
    fun nonEmptyLocal_syncSchedules_updatedSchedules() = runTest {
        // Given the localDataSource has schedule1
        // Given the remoteDataSource has schedule1, schedule2, schedule3
        val initSchedules = localCacheRepository.getSchedule()

        // When repository refreshSchedule
        localCacheRepository.refreshSchedule()

        // Then repository should now return 3 schedules
        val refreshedSchedules = localCacheRepository.getSchedule()
        assertThat(refreshedSchedules).isEqualTo(remoteSchedules)
    }

    @Test
    fun nonEmptyLocal_syncEmptyRemoteSchedules_emptyLocalSchedules() =
        runTest {
            // Given the localDataSource has schedule1
            // Given the remoteDataSource has empty list
            remoteDataSource.setSchedule(emptyList())
            val initSchedules = localCacheRepository.getSchedule()

            // When repository refreshSchedule
            localCacheRepository.refreshSchedule()

            // Then repository should now return 0 schedules
            val refreshedSchedules = localCacheRepository.getSchedule()
            assertThat(refreshedSchedules).isEmpty()
        }

    @Test
    fun errorRemote_syncSchedules_throwException() = runTest {
        // Given the remoteDataSource set to return error
        val exceptionMessage = "test exception"
        remoteDataSource.setShouldReturnIOException(true, exceptionMessage)

        // When repository refreshSchedule
        try {
            localCacheRepository.refreshSchedule()
            fail()

            // Then repository should throw the exception
        } catch (ex: IOException) {
            assertThat(ex.message).isEqualTo(exceptionMessage)
        }
    }
}
