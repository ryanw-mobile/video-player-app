/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rwmobi.dazncodechallenge.data.source.local.dao.EventsDao
import com.rwmobi.dazncodechallenge.data.source.local.dao.ScheduleDao
import com.rwmobi.dazncodechallenge.data.source.local.model.EventDbEntity
import com.rwmobi.dazncodechallenge.data.source.local.model.ScheduleDbEntity

@Database(
    entities = [
        EventDbEntity::class,
        ScheduleDbEntity::class,
    ],
    version = 5,
    exportSchema = false,
)
@TypeConverters(DateTypeConverters::class)
abstract class DaznApiDatabase : RoomDatabase() {
    abstract val eventsDao: EventsDao
    abstract val scheduleDao: ScheduleDao
}
