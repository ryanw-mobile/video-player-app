/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rwmobi.dazncodechallenge.data.source.local.model.ScheduleDbEntity

@Dao
sealed interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scheduleDbEntity: ScheduleDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(scheduleDBEntities: List<ScheduleDbEntity>)

    @Query("SELECT * FROM schedule_table ORDER BY date ASC")
    suspend fun getSchedules(): List<ScheduleDbEntity>

    @Query("SELECT * FROM schedule_table WHERE schedule_id = :scheduleId LIMIT 1")
    suspend fun getScheduleById(scheduleId: Int): ScheduleDbEntity?

    // For unit tests
    @Query("SELECT * FROM schedule_table WHERE dirty = 1")
    suspend fun getDirtySchedules(): List<ScheduleDbEntity>

    // SQLite does not have a boolean data type.
    // Room maps it to an INTEGER column, mapping true to 1 and false to 0.
    @Query("UPDATE schedule_table SET dirty = 1")
    suspend fun markDirty()

    @Query("DELETE FROM schedule_table WHERE schedule_id = :scheduleId")
    suspend fun delete(scheduleId: Int)

    @Query("DELETE FROM schedule_table WHERE dirty = 1")
    suspend fun deleteDirty()

    @Query("DELETE FROM schedule_table")
    suspend fun clear()
}
