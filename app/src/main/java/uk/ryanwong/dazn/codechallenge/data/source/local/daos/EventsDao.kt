/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.ryanwong.dazn.codechallenge.data.source.local.entities.EventDbEntity

@Dao
sealed interface EventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(eventDbEntity: EventDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(eventDBEntities: List<EventDbEntity>)

    @Query("SELECT * FROM event_table ORDER BY date ASC")
    fun observeEvents(): LiveData<List<EventDbEntity>>

    @Query("SELECT * FROM event_table ORDER BY date ASC")
    suspend fun getEvents(): List<EventDbEntity>

    @Query("SELECT * FROM event_table WHERE event_id = :eventId")
    fun observeEventById(eventId: Int): LiveData<EventDbEntity>

    @Query("SELECT * FROM event_table WHERE event_id = :eventId")
    suspend fun getEventById(eventId: Int): EventDbEntity

    // For unit tests
    @Query("SELECT * FROM event_table WHERE dirty = 1")
    suspend fun getDirtyEvents(): List<EventDbEntity>

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