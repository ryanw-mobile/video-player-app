/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local.mapper

import com.rwmobi.dazncodechallenge.data.source.local.model.EventDbEntity
import com.rwmobi.dazncodechallenge.data.source.local.model.ScheduleDbEntity
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
