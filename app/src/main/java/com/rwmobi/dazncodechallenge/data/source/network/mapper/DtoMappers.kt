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

fun Schedule.toNetworkDto(): ScheduleNetworkDto {
    return ScheduleNetworkDto(
        scheduleId = scheduleId,
        title = title,
        subtitle = subtitle,
        date = date,
        imageUrl = imageUrl,
    )
}

fun List<Schedule>.toNetworkDto(): List<ScheduleNetworkDto> {
    return map {
        it.toNetworkDto()
    }
}

fun Event.toNetworkDto(): EventNetworkDto {
    return EventNetworkDto(
        eventId = eventId,
        title = title,
        subtitle = subtitle,
        date = date,
        imageUrl = imageUrl,
        videoUrl = videoUrl,
    )
}

fun List<Event>.toNetworkDto(): List<EventNetworkDto> {
    return map {
        it.toNetworkDto()
    }
}
