/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverters {
    // Moshi has set to convert UTC strings to Date objects,
    // but Room does not know how to store them without these converters
    // (that convert them between Date and Long)
    @TypeConverter
    fun toDate(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time
}
