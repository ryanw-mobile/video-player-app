/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ryanwong.dazn.codechallenge.data.source.local.DaznApiDatabase
import util.parseTimeStamp

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class EventDaoTest {
    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: DaznApiDatabase

    private val event1 = Event(
        1,
        "Liverpool v Porto",
        "UEFA Champions League",
        "2021-09-28T01:55:56.925Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310176837169_image-header_pDach_1554579780000.jpeg?alt=media&token=1777d26b-d051-4b5f-87a8-7633d3d6dd20",
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
    )

    private val event1Modified = Event(
        1,
        "Nîmes v Rennes",
        "Ligue 1",
        "2021-09-28T02:55:56.925Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310381637057_image-header_pDach_1554664873000.jpeg?alt=media&token=53616931-55a8-476e-b1b7-d18fc22a2bf0",
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
    )

    private val event2 = Event(
        2,
        "Nîmes v Rennes",
        "Ligue 1",
        "2021-09-28T02:55:56.925Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310381637057_image-header_pDach_1554664873000.jpeg?alt=media&token=53616931-55a8-476e-b1b7-d18fc22a2bf0",
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
    )

    private val event3 = Event(
        3,
        "Tottenham v Man City",
        "UEFA Champions League",
        "2021-09-28T03:55:56.925Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310511685198_image-header_pDach_1554872450000.jpeg?alt=media&token=5524d719-261e-49e6-abf3-a74c30df3e27",
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
    )

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DaznApiDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun insertEvent_GetById_ExpectsSameEvent() = runBlockingTest {
        // GIVEN - empty database
        // Do Nothing

        // WHEN - Insert an event
        database.eventsDao.insert(event1)

        // THEN - Get the event by id from the database. The loaded event contains the expected values
        val loaded = database.eventsDao.getEventById(event1.id)
        MatcherAssert.assertThat<Event>(loaded as Event, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, `is`(event1.id))
        MatcherAssert.assertThat(loaded.title, `is`(event1.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(event1.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(event1.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(event1.imageUrl))
        MatcherAssert.assertThat(loaded.videoUrl, `is`(event1.videoUrl))
    }

    @Test
    fun insertAll_GetEvents_ExpectsCorrectListSize() = runBlockingTest {
        // GIVEN - Empty database
        // do nothing

        // WHEN - use insertAll to insert three events
        database.eventsDao.insertAll(listOf(event1, event2, event3))

        // THEN - When get the events, the list size should be 3
        val loaded = database.eventsDao.getEvents()
        MatcherAssert.assertThat(loaded.size, `is`(3))
    }

    @Test
    fun upsertEvent_GetById_ExpectsUpdatedEvent() = runBlockingTest {
        // GIVEN - Insert an event
        database.eventsDao.insert(event1)

        // WHEN - Update the event with a same Id, but with different data
        // get the event by id from the database
        database.eventsDao.insert(event1Modified)

        // THEN - When get the event by Id again, it should contain the new values
        val loaded = database.eventsDao.getEventById(event1Modified.id)
        MatcherAssert.assertThat<Event>(loaded as Event, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, `is`(event1Modified.id))
        MatcherAssert.assertThat(loaded.title, `is`(event1Modified.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(event1Modified.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(event1Modified.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(event1Modified.imageUrl))
        MatcherAssert.assertThat(loaded.videoUrl, `is`(event1Modified.videoUrl))
    }

    @Test
    fun deleteEvent_GetById_ExpectsNull() = runBlockingTest {
        // GIVEN - Insert an event
        database.eventsDao.insert(event1)

        // WHEN - delete the event by Id
        database.eventsDao.delete(event1.id)

        // THEN - When get the event by Id again, it should return null
        val loaded = database.eventsDao.getEventById(event1.id)
        MatcherAssert.assertThat<Event>(loaded as Event, CoreMatchers.nullValue())
    }

    @Test
    fun insertAll_DeleteOneAndGetById_ExpectsNull() = runBlockingTest {
        // GIVEN - Insert 3 events in a list
        database.eventsDao.insertAll(listOf(event1, event2, event3))

        // WHEN - clear the database
        database.eventsDao.delete(event1.id)

        // THEN - When get the events, the list should be empty
        val loaded = database.eventsDao.getEventById(event1.id)
        MatcherAssert.assertThat<Event>(loaded as Event, CoreMatchers.nullValue())
    }

    @Test
    fun insertAll_Clear_ExpectsEmptyList() = runBlockingTest {
        // GIVEN - Insert 3 events in a list
        database.eventsDao.insertAll(listOf(event1, event2, event3))

        // WHEN - clear the database
        database.eventsDao.clear()

        // THEN - When get the events, the list should be empty
        val loaded = database.eventsDao.getEvents()
        MatcherAssert.assertThat(loaded.size, `is`(0))
    }

    // Dirty bit testing

    @Test
    fun insertNewEvent_GetById_ExpectsDirtyFalse() = runBlockingTest {
        // GIVEN - empty database
        // Do Nothing

        // WHEN - Insert an event
        database.eventsDao.insert(event1)

        // THEN - Get the event by id from the database. The loaded event contains the expected values
        val loaded = database.eventsDao.getEventById(event1.id)
        MatcherAssert.assertThat<Event>(loaded as Event, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, `is`(event1.id))
        MatcherAssert.assertThat(loaded.title, `is`(event1.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(event1.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(event1.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(event1.imageUrl))
        MatcherAssert.assertThat(loaded.videoUrl, `is`(event1.videoUrl))
        MatcherAssert.assertThat(loaded.dirty, `is`(false))
    }

    @Test
    fun markDirty_GetById_ExpectsDirtyTrue() = runBlockingTest {
        // GIVEN - multiple events
        database.eventsDao.insertAll(listOf(event1, event2, event3))

        // WHEN - Insert an event and mark it as dirty
        database.eventsDao.markDirty()

        // THEN - Get the event by id from the database. The loaded event contains the expected values
        val loaded = database.eventsDao.getEventById(event1.id)
        MatcherAssert.assertThat<Event>(loaded as Event, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, `is`(event1.id))
        MatcherAssert.assertThat(loaded.title, `is`(event1.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(event1.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(event1.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(event1.imageUrl))
        MatcherAssert.assertThat(loaded.videoUrl, `is`(event1.videoUrl))
        MatcherAssert.assertThat(loaded.dirty, `is`(true))
    }

    @Test
    fun dirtyEvents_UpdateEvent_ExpectsDirtyFalse() = runBlockingTest {
        // GIVEN - multiple events and marked dirty
        database.eventsDao.insertAll(listOf(event1, event2, event3))
        database.eventsDao.markDirty()

        // WHEN - Upsert event 1
        database.eventsDao.insert(event1Modified)

        // THEN - Get the event by id from the database. The loaded event contains the expected values
        val loaded = database.eventsDao.getEventById(event1Modified.id)
        MatcherAssert.assertThat<Event>(loaded as Event, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, `is`(event1Modified.id))
        MatcherAssert.assertThat(loaded.title, `is`(event1Modified.title))
        MatcherAssert.assertThat(loaded.subtitle, `is`(event1Modified.subtitle))
        MatcherAssert.assertThat(loaded.date, `is`(event1Modified.date))
        MatcherAssert.assertThat(loaded.imageUrl, `is`(event1Modified.imageUrl))
        MatcherAssert.assertThat(loaded.videoUrl, `is`(event1Modified.videoUrl))
        MatcherAssert.assertThat(loaded.dirty, `is`(false))
    }

    @Test
    fun dirtyEvents_InsertOneAndDeleteDirty_ExpectsOneEvent() = runBlockingTest {
        // GIVEN - multiple events and marked dirty
        database.eventsDao.insertAll(listOf(event1, event3))
        database.eventsDao.markDirty()

        // WHEN - Insert event 2
        database.eventsDao.insert(event2)
        database.eventsDao.deleteDirty()

        // THEN - Get all events. Expect only event 2 is returned
        val loaded = database.eventsDao.getEvents()
        MatcherAssert.assertThat(loaded.size, `is`(1))
        val loadedEvent = loaded[0]
        MatcherAssert.assertThat<Event>(loadedEvent as Event, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loadedEvent.id, `is`(event2.id))
        MatcherAssert.assertThat(loadedEvent.title, `is`(event2.title))
        MatcherAssert.assertThat(loadedEvent.subtitle, `is`(event2.subtitle))
        MatcherAssert.assertThat(loadedEvent.date, `is`(event2.date))
        MatcherAssert.assertThat(loadedEvent.imageUrl, `is`(event2.imageUrl))
        MatcherAssert.assertThat(loadedEvent.videoUrl, `is`(event2.videoUrl))
        MatcherAssert.assertThat(loadedEvent.dirty, `is`(false))
    }


    @After
    fun closeDb() = database.close()
}