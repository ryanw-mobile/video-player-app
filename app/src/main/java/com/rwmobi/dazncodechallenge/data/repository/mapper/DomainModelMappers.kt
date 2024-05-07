/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.repository.mapper

import com.rwmobi.dazncodechallenge.data.source.local.model.EventDbEntity
import com.rwmobi.dazncodechallenge.data.source.local.model.ScheduleDbEntity
import com.rwmobi.dazncodechallenge.data.source.network.dto.EventNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.dto.ScheduleNetworkDto
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

fun EventDbEntity.toEvent(): Event {
    return Event(
        eventId = this.eventId,
        title = this.title,
        subtitle = this.subtitle,
        date = this.date,
        imageUrl = this.imageUrl,
        videoUrl = this.videoUrl,
    )
}

fun List<EventDbEntity>.toEvent(): List<Event> {
    return map {
        it.toEvent()
    }
}

fun ScheduleDbEntity.toSchedule(): Schedule {
    return Schedule(
        scheduleId = this.scheduleId,
        title = this.title,
        subtitle = this.subtitle,
        date = this.date,
        imageUrl = this.imageUrl,
    )
}

fun List<ScheduleDbEntity>.toSchedule(): List<Schedule> {
    return map {
        it.toSchedule()
    }
}

fun EventNetworkDto.toEvent(): Event {
    return Event(
        eventId = eventId,
        title = title,
        subtitle = subtitle,
        date = date,
        imageUrl = imageUrl,
        videoUrl = videoUrl,
    )
}

fun ScheduleNetworkDto.toSchedule(): Schedule {
    return Schedule(
        scheduleId = scheduleId,
        title = title,
        subtitle = subtitle,
        date = date,
        imageUrl = imageUrl,
    )
}
