package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule

@Database(entities = [Event::class, Schedule::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DaznApiDatabase : RoomDatabase() {

    // DAOs
    abstract val eventsDao: EventsDao
    abstract val scheduleDao: ScheduleDao
}