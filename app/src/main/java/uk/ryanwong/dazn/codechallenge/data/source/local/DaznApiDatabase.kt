package uk.ryanwong.dazn.codechallenge.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule

@Database(entities = [Event::class, Schedule::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DaznApiDatabase : RoomDatabase() {

    // DAOs
    abstract val eventsDao: EventsDao
    abstract val scheduleDao: ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: DaznApiDatabase? = null

        fun getInstance(context: Context): DaznApiDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DaznApiDatabase::class.java,
                        "dazn_api_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}