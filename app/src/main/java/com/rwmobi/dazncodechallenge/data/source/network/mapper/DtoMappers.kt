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

fun List<Schedule>.asScheduleNetworkDto(): List<ScheduleNetworkDto> {
    return map {
        ScheduleNetworkDto(
            scheduleId = it.scheduleId,
            title = it.title,
            subtitle = it.subtitle,
            date = it.date,
            imageUrl = it.imageUrl,
        )
    }
}

fun List<Event>.asEventNetworkDto(): List<EventNetworkDto> {
    return map {
        EventNetworkDto(
            eventId = it.eventId,
            title = it.title,
            subtitle = it.subtitle,
            date = it.date,
            imageUrl = it.imageUrl,
            videoUrl = it.videoUrl,
        )
    }
}
