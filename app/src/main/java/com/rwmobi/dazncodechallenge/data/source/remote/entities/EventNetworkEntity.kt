/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.remote.entities

import com.rwmobi.dazncodechallenge.domain.model.Event
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class EventNetworkEntity(
    @Json(name = "id")
    val eventId: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "subtitle")
    val subtitle: String,
    @Json(name = "date")
    val date: Date,
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "videoUrl")
    val videoUrl: String,
)

fun List<EventNetworkEntity>.asDomainModel(): List<Event> {
    return map {
        Event(
            eventId = it.eventId,
            title = it.title,
            subtitle = it.subtitle,
            date = it.date,
            imageUrl = it.imageUrl,
            videoUrl = it.videoUrl,
        )
    }
}

fun List<Event>.asNetworkModel(): List<EventNetworkEntity> {
    return map {
        EventNetworkEntity(
            eventId = it.eventId,
            title = it.title,
            subtitle = it.subtitle,
            date = it.date,
            imageUrl = it.imageUrl,
            videoUrl = it.videoUrl,
        )
    }
}
