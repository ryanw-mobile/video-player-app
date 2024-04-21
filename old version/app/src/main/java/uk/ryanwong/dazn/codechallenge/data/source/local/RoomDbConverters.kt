package uk.ryanwong.dazn.codechallenge.data.source.local

import androidx.room.TypeConverter
import java.util.Date

class RoomDbConverters {
    // Moshi has set to convert UTC strings to Date objects,
    // but Room does not know how to store them without these converters
    // (that convert them between Date and Long)
    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}
