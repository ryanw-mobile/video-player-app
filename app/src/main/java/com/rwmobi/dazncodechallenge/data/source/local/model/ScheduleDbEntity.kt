/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "schedule_table")
data class ScheduleDbEntity(
    @ColumnInfo(name = "schedule_id")
    @PrimaryKey
    val scheduleId: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "subtitle")
    val subtitle: String,
    @ColumnInfo(name = "date")
    val date: Date,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    // This field is not from the RestAPI
    // Every time we overwrite the DB with API data. To remove outdated data in the FB,
    // we use this extra dirty bit approach to simulate synchronization.
    @ColumnInfo(name = "dirty")
    val dirty: Boolean = false,
)
