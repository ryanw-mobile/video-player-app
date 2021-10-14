package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ryanwong.dazn.codechallenge.data.source.local.daos.EventsDao
import uk.ryanwong.dazn.codechallenge.data.source.local.daos.ScheduleDao
import uk.ryanwong.dazn.codechallenge.data.source.local.entities.EventDbEntity
import uk.ryanwong.dazn.codechallenge.data.source.local.entities.ScheduleDbEntity

@Database(entities = [EventDbEntity::class, ScheduleDbEntity::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DaznApiDatabase : RoomDatabase() {

    // DAOs
    abstract val eventsDao: EventsDao
    abstract val scheduleDao: ScheduleDao
}