/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.network.mapper

import com.rwmobi.dazncodechallenge.data.source.network.dto.EventNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.dto.ScheduleNetworkDto
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

fun List<EventNetworkDto>.asEvent(): List<Event> {
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

fun List<ScheduleNetworkDto>.asSchedule(): List<Schedule> {
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
