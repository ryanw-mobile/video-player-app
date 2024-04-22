/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.remote.entities

import com.rwmobi.dazncodechallenge.domain.model.Schedule
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class ScheduleNetworkEntity(
    @Json(name = "id")
    val scheduleId: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "subtitle")
    val subtitle: String,
    @Json(name = "date")
    val date: Date,
    @Json(name = "imageUrl")
    val imageUrl: String,
)

fun List<ScheduleNetworkEntity>.asDomainModel(): List<Schedule> {
    return map {
        Schedule(
            scheduleId = it.scheduleId,
            title = it.title,
            subtitle = it.subtitle,
            date = it.date,
            imageUrl = it.imageUrl,
        )
    }
}

fun List<Schedule>.asNetworkModel(): List<ScheduleNetworkEntity> {
    return map {
        ScheduleNetworkEntity(
            scheduleId = it.scheduleId,
            title = it.title,
            subtitle = it.subtitle,
            date = it.date,
            imageUrl = it.imageUrl,
        )
    }
}
