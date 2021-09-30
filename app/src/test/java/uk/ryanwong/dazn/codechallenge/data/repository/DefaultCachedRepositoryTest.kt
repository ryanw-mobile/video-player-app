/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.ryanwong.dazn.codechallenge.MainCoroutineRule
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import uk.ryanwong.dazn.codechallenge.data.repository.DefaultCachedRepositoryTest.TestData.event1
import uk.ryanwong.dazn.codechallenge.data.repository.DefaultCachedRepositoryTest.TestData.event2
import uk.ryanwong.dazn.codechallenge.data.repository.DefaultCachedRepositoryTest.TestData.event3
import uk.ryanwong.dazn.codechallenge.util.parseTimeStamp

@ExperimentalCoroutinesApi
class DefaultCachedRepositoryTest {

    object TestData {
        val event1 = Event(
            1,
            "Liverpool v Porto",
            "UEFA Champions League",
            "2021-09-28T01:55:56.925Z".parseTimeStamp(),
            "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310176837169_image-header_pDach_1554579780000.jpeg?alt=media&token=1777d26b-d051-4b5f-87a8-7633d3d6dd20",
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

    private val remoteEvents = listOf(event1, event2, event3).sortedBy { it.date }
    private val localEvents = listOf(event1)

    private val remoteSchedules =
        listOf(TestData.schedule1, TestData.schedule2, TestData.schedule3).sortedBy { it.date }
    private val localSchedules = listOf(TestData.schedule1)

    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var localDataSource: FakeDataSource

    // Class under test
    private lateinit var defaultCachedRepository: DefaultCachedRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        remoteDataSource =
            FakeDataSource(remoteEvents.toMutableList(), remoteSchedules.toMutableList())
        localDataSource =
            FakeDataSource(localEvents.toMutableList(), localSchedules.toMutableList())

        // Get a reference to the class under test
        defaultCachedRepository = DefaultCachedRepository(
            remoteDataSource, localDataSource, Dispatchers.Main
        )
    }

    @Test
    fun nonEmptyLocal_syncEvents_updatedEvents() = mainCoroutineRule.runBlockingTest {
        // Given the localDataSource has event1
        // Given the remoteDataSource has event1, event2, event3
        val initEvents = defaultCachedRepository.getEvents() as ApiResult.Success
        MatcherAssert.assertThat(initEvents.data, IsEqual(localEvents))

        // When repository refreshEvents
        defaultCachedRepository.refreshEvents()
        val refreshedEvents = defaultCachedRepository.getEvents() as ApiResult.Success

        // Then repository should now return 3 events
        MatcherAssert.assertThat(refreshedEvents.data, IsEqual(remoteEvents))
    }

    @Test
    fun nonEmptyLocal_syncSchedules_updatedSchedules() = mainCoroutineRule.runBlockingTest {
        // Given the localDataSource has schedule1
        // Given the remoteDataSource has schedule1, schedule2, schedule3
        val initSchedules = defaultCachedRepository.getSchedule() as ApiResult.Success
        MatcherAssert.assertThat(initSchedules.data, IsEqual(localSchedules))

        // When repository refreshSchedule
        defaultCachedRepository.refreshSchedule()
        val refreshedSchedules = defaultCachedRepository.getSchedule() as ApiResult.Success

        // Then repository should now return 3 schedules
        MatcherAssert.assertThat(refreshedSchedules.data, IsEqual(remoteSchedules))
    }
}