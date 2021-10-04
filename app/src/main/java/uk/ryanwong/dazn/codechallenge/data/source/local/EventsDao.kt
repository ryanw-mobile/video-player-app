package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.ryanwong.dazn.codechallenge.data.model.Event

@Dao
interface EventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<Event>)

    @Query("SELECT * FROM event_table ORDER BY date ASC")
    fun observeEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM event_table ORDER BY date ASC")
    fun getEvents(): List<Event>

    @Query("SELECT * FROM event_table WHERE event_id = :eventId")
    fun observeEventById(eventId: Int): LiveData<Event>

    @Query("SELECT * FROM event_table WHERE event_id = :eventId")
    fun getEventById(eventId: Int): Event

    // For unit tests
    @Query("SELECT * FROM event_table WHERE dirty = 1")
    fun getDirtyEvents(): List<Event>

    // SQLite does not have a boolean data type.
    // Room maps it to an INTEGER column, mapping true to 1 and false to 0.
    @Query("UPDATE event_table SET dirty = 1")
    suspend fun markDirty()

    @Query("DELETE FROM event_table WHERE event_id = :eventId")
    suspend fun delete(eventId: Int)

    @Query("DELETE FROM event_table WHERE dirty = 1")
    suspend fun deleteDirty()

    @Query("DELETE FROM event_table")
    suspend fun clear()
}