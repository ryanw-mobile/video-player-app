package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.ryanwong.dazn.codechallenge.data.model.Schedule

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(schedule: Schedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(schedules: List<Schedule>)

    @Query("SELECT * FROM schedule_table ORDER BY date ASC")
    fun observeSchedules(): LiveData<List<Schedule>>

    @Query("SELECT * FROM schedule_table ORDER BY date ASC")
    suspend fun getSchedules(): List<Schedule>

    @Query("SELECT * FROM schedule_table WHERE id = :scheduleId")
    fun observeScheduleById(scheduleId: Int): LiveData<Schedule>

    @Query("SELECT * FROM schedule_table WHERE id = :scheduleId")
    suspend fun getScheduleById(scheduleId: Int): Schedule

    // For unit tests
    @Query("SELECT * FROM schedule_table WHERE dirty = 1")
    suspend fun getDirtySchedules(): List<Schedule>

    // SQLite does not have a boolean data type.
    // Room maps it to an INTEGER column, mapping true to 1 and false to 0.
    @Query("UPDATE schedule_table SET dirty = 1")
    suspend fun markDirty()

    @Query("DELETE FROM schedule_table WHERE id = :scheduleId")
    suspend fun delete(scheduleId: Int)

    @Query("DELETE FROM schedule_table WHERE dirty = 1")
    suspend fun deleteDirty()

    @Query("DELETE FROM schedule_table")
    suspend fun clear()
}