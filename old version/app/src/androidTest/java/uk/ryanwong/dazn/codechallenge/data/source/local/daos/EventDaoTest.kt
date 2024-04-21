/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
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
import uk.ryanwong.dazn.codechallenge.data.source.local.DaznApiDatabase
import uk.ryanwong.dazn.codechallenge.data.source.local.entities.asDatabaseModel
import uk.ryanwong.dazn.codechallenge.data.source.local.entities.asDomainModel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@SmallTest
internal class EventDaoTest {
    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    // Use in-memory database for testing
    @Inject
    lateinit var database: DaznApiDatabase

    @Test
    fun insertEvent_GetById_ReturnSameEvent() =
        runTest {
            // GIVEN - empty database
            // Do Nothing

            // WHEN - Insert an event
            database.eventsDao.insert(event1.asDatabaseModel())

            // THEN - Get the event by id from the database. The loaded event contains the expected values
            val loaded = database.eventsDao.getEventById(event1.eventId)
            assertThat(loaded.asDomainModel()).isEqualTo(event1)
        }

    @Test
    fun insertAll_GetEvents_ReturnCorrectListSize() =
        runTest {
            // GIVEN - Empty database
            // do nothing

            // WHEN - use insertAll to insert three events
            database.eventsDao.insertAll(listOf(event1, event2, event3).asDatabaseModel())

            // THEN - When get the events, the list size should be 3
            val loaded = database.eventsDao.getEvents()
            assertThat(loaded).hasSize(3)
        }

    @Test
    fun upsertEvent_GetById_ReturnUpdatedEvent() =
        runTest {
            // GIVEN - Insert an event
            database.eventsDao.insert(event1.asDatabaseModel())

            // WHEN - Update the event with a same Id, but with different data
            // get the event by id from the database
            database.eventsDao.insert(event1Modified.asDatabaseModel())

            // THEN - When get the event by Id again, it should contain the new values
            val loaded = database.eventsDao.getEventById(event1Modified.eventId)
            assertThat(loaded.asDomainModel()).isEqualTo(event1Modified)
        }

    @Test
    fun deleteEvent_GetById_ReturnNull() =
        runTest {
            // GIVEN - Insert an event
            database.eventsDao.insert(event1.asDatabaseModel())

            // WHEN - delete the event by Id
            database.eventsDao.delete(event1.eventId)

            // THEN - When get the event by Id again, it should return null
            val loaded = database.eventsDao.getEventById(event1.eventId)
            assertThat(loaded).isNull()
        }

    @Test
    fun insertAll_DeleteOneAndGetById_ReturnNull() =
        runTest {
            // GIVEN - Insert 3 events in a list
            database.eventsDao.insertAll(
                listOf(event1, event2, event3).asDatabaseModel(),
            )

            // WHEN - clear the database
            database.eventsDao.delete(event1.eventId)

            // THEN - When get the events, the list should be empty
            val loaded = database.eventsDao.getEventById(event1.eventId)
            assertThat(loaded).isNull()
        }

    @Test
    fun insertAll_Clear_ReturnEmptyList() =
        runTest {
            // GIVEN - Insert 3 events in a list
            database.eventsDao.insertAll(listOf(event1, event2, event3).asDatabaseModel())

            // WHEN - clear the database
            database.eventsDao.clear()

            // THEN - When get the events, the list should be empty
            val loaded = database.eventsDao.getEvents()
            assertThat(loaded).isEmpty()
        }

    // Dirty bit testing
    @Test
    fun insertNewEvent_GetById_ReturnDirtyFalse() =
        runTest {
            // GIVEN - empty database
            // Do Nothing

            // WHEN - Insert an event
            database.eventsDao.insert(event1.asDatabaseModel())

            // THEN - Get the event by id from the database. The loaded event contains the expected values
            val loaded = database.eventsDao.getEventById(event1.eventId)
            assertThat(loaded.asDomainModel()).isEqualTo(event1)
        }

    @Test
    fun markDirty_GetById_ReturnDirtyTrue() =
        runTest {
            // GIVEN - multiple events
            database.eventsDao.insertAll(listOf(event1, event2, event3).asDatabaseModel())

            // WHEN - Insert an event and mark it as dirty
            database.eventsDao.markDirty()

            // THEN - Get the event by id from the database. The loaded event contains the expected values
            val loaded = database.eventsDao.getEventById(event1.eventId)
            assertThat(loaded.dirty).isTrue()
        }

    @Test
    fun dirtyEvents_UpdateEvent_ReturnDirtyFalse() =
        runTest {
            // GIVEN - multiple events and marked dirty
            database.eventsDao.insertAll(listOf(event1, event2, event3).asDatabaseModel())
            database.eventsDao.markDirty()

            // WHEN - Upsert event 1
            database.eventsDao.insert(event1Modified.asDatabaseModel())

            // THEN - Get the event by id from the database. The loaded event contains the expected values
            val loaded = database.eventsDao.getEventById(event1Modified.eventId)
            assertThat(loaded.dirty).isFalse()
        }

    @Test
    fun dirtyEvents_InsertOneAndDeleteDirty_ReturnOneEvent() =
        runTest {
            // GIVEN - multiple events and marked dirty
            database.eventsDao.insertAll(listOf(event1, event3).asDatabaseModel())
            database.eventsDao.markDirty()

            // WHEN - Insert event 2
            database.eventsDao.insert(event2.asDatabaseModel())
            database.eventsDao.deleteDirty()

            // THEN - Get all events. Expect only event 2 is returned
            val loaded = database.eventsDao.getEvents()
            assertThat(loaded.asDomainModel()).containsExactly(event2)
        }

    @After
    fun closeDb() = database.close()
}
