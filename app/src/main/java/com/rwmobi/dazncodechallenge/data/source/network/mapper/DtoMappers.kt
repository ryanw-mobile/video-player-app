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

fun Schedule.toScheduleNetworkDto(): ScheduleNetworkDto {
    return ScheduleNetworkDto(
        scheduleId = scheduleId,
        title = title,
        subtitle = subtitle,
        date = date,
        imageUrl = imageUrl,
    )
}

fun List<Schedule>.toScheduleNetworkDto(): List<ScheduleNetworkDto> {
    return map {
        it.toScheduleNetworkDto()
    }
}

fun Event.toEventNetworkDto(): EventNetworkDto {
    return EventNetworkDto(
        eventId = eventId,
        title = title,
        subtitle = subtitle,
        date = date,
        imageUrl = imageUrl,
        videoUrl = videoUrl,
    )
}

fun List<Event>.toEventNetworkDto(): List<EventNetworkDto> {
    return map {
        it.toEventNetworkDto()
    }
}
