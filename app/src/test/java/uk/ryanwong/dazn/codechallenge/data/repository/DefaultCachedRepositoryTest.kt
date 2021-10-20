/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.ryanwong.dazn.codechallenge.MainCoroutineRule
import uk.ryanwong.dazn.codechallenge.TestData
import uk.ryanwong.dazn.codechallenge.TestData.event1
import uk.ryanwong.dazn.codechallenge.TestData.event2
import uk.ryanwong.dazn.codechallenge.TestData.event3
import java.io.IOException


@ExperimentalCoroutinesApi
class DefaultCachedRepositoryTest {

    private val remoteEvents = mutableListOf(event1, event2, event3).apply { sortBy { it.date } }

    private val localEvents = mutableListOf(event1)

    private val remoteSchedules = mutableListOf(
        TestData.schedule1, TestData.schedule2, TestData.schedule3
    ).apply {
        sortBy { it.date }
    }
    private val localSchedules = mutableListOf(TestData.schedule1)

    private lateinit var remoteDataSource: FakeRemoteDataSource
    private lateinit var localDataSource: FakeLocalDataSource

    // Class under test
    private lateinit var defaultCachedRepository: DefaultCachedRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        remoteDataSource =
            FakeRemoteDataSource(remoteEvents, remoteSchedules)
        localDataSource =
            FakeLocalDataSource(localEvents, localSchedules)

        // Get a reference to the class under test
        defaultCachedRepository = DefaultCachedRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun nonEmptyLocal_syncEvents_updatedEvents() = mainCoroutineRule.runBlockingTest {
        // Given the localDataSource has event1
        // Given the remoteDataSource has event1, event2, event3
        val initEvents = defaultCachedRepository.getEvents()
        MatcherAssert.assertThat(initEvents, `is`(localEvents))

        // When repository refreshEvents
        defaultCachedRepository.refreshEvents()

        // Then repository should now return 3 events
        val refreshedEvents = defaultCachedRepository.getEvents()
        MatcherAssert.assertThat(refreshedEvents, `is`(remoteEvents))
    }

    @Test
    fun nonEmptyLocal_syncEmptyRemoteEvents_emptyLocalEvents() = mainCoroutineRule.runBlockingTest {
        // Given the localDataSource has event1
        // Given the remoteDataSource is empty list
        remoteDataSource.setEvents(emptyList())
        val initEvents = defaultCachedRepository.getEvents()
        MatcherAssert.assertThat(initEvents, `is`(localEvents))

        // When repository refreshEvents
        defaultCachedRepository.refreshEvents()

        // Then repository should now return 0 schedules
        val refreshedEvents = defaultCachedRepository.getEvents()
        MatcherAssert.assertThat(refreshedEvents.size, `is`(0))
    }

    @Test
    fun errorRemote_syncEvents_throwException() = mainCoroutineRule.runBlockingTest {
        // Given the remoteDataSource set to return error
        val exceptionMessage = "test exception"
        remoteDataSource.setShouldReturnIOException(true, exceptionMessage)

        // When repository refreshEvents
        try {
            defaultCachedRepository.refreshEvents()
            fail();

            // Then repository should throw the exception
        } catch (ex: IOException) {
            MatcherAssert.assertThat(exceptionMessage, `is`(exceptionMessage))
        }
    }

    // Tests for schedule

    @Test
    fun nonEmptyLocal_syncSchedules_updatedSchedules() = mainCoroutineRule.runBlockingTest {
        // Given the localDataSource has schedule1
        // Given the remoteDataSource has schedule1, schedule2, schedule3
        val initSchedules = defaultCachedRepository.getSchedule()
        MatcherAssert.assertThat(initSchedules, `is`(localSchedules))

        // When repository refreshSchedule
        defaultCachedRepository.refreshSchedule()

        // Then repository should now return 3 schedules
        val refreshedSchedules = defaultCachedRepository.getSchedule()
        MatcherAssert.assertThat(
            refreshedSchedules, `is`(remoteSchedules)
        )
    }

    @Test
    fun nonEmptyLocal_syncEmptyRemoteSchedules_emptyLocalSchedules() =
        mainCoroutineRule.runBlockingTest {
            // Given the localDataSource has schedule1
            // Given the remoteDataSource has empty list
            remoteDataSource.setSchedule(emptyList())
            val initSchedules = defaultCachedRepository.getSchedule()
            MatcherAssert.assertThat(initSchedules, `is`(localSchedules))

            // When repository refreshSchedule
            defaultCachedRepository.refreshSchedule()

            // Then repository should now return 0 schedules
            val refreshedSchedules = defaultCachedRepository.getSchedule()
            MatcherAssert.assertThat(refreshedSchedules.size, `is`(0))
        }

    @Test
    fun errorRemote_syncSchedules_throwException() = mainCoroutineRule.runBlockingTest {
        // Given the remoteDataSource set to return error
        val exceptionMessage = "test exception"
        remoteDataSource.setShouldReturnIOException(true, exceptionMessage)

        // When repository refreshSchedule
        try {
            defaultCachedRepository.refreshSchedule()
            fail();

            // Then repository should throw the exception
        } catch (ex: IOException) {
            MatcherAssert.assertThat(exceptionMessage, `is`(exceptionMessage))
        }
    }
}
