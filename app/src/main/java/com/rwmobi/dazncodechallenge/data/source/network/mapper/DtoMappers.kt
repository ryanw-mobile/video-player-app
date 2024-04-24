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

fun Schedule.asScheduleNetworkDto(): ScheduleNetworkDto {
    return ScheduleNetworkDto(
        scheduleId = scheduleId,
        title = title,
        subtitle = subtitle,
        date = date,
        imageUrl = imageUrl,
    )
}

fun List<Schedule>.asScheduleNetworkDto(): List<ScheduleNetworkDto> {
    return map {
        it.asScheduleNetworkDto()
    }
}

fun Event.asEventNetworkDto(): EventNetworkDto {
    return EventNetworkDto(
        eventId = eventId,
        title = title,
        subtitle = subtitle,
        date = date,
        imageUrl = imageUrl,
        videoUrl = videoUrl,
    )
}

fun List<Event>.asEventNetworkDto(): List<EventNetworkDto> {
    return map {
        it.asEventNetworkDto()
    }
}
