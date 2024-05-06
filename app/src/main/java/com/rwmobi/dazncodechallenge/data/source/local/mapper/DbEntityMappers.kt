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

fun Event.toEventDbEntity(): EventDbEntity {
    return EventDbEntity(
        eventId = this.eventId,
        title = this.title,
        subtitle = this.subtitle,
        date = this.date,
        imageUrl = this.imageUrl,
        videoUrl = this.videoUrl,
        dirty = false,
    )
}

fun List<Event>.toEventDbEntity(): List<EventDbEntity> {
    return map {
        it.toEventDbEntity()
    }
}

fun Schedule.toScheduleDbEntity(): ScheduleDbEntity {
    return ScheduleDbEntity(
        scheduleId = this.scheduleId,
        title = this.title,
        subtitle = this.subtitle,
        date = this.date,
        imageUrl = this.imageUrl,
        dirty = false,
    )
}

fun List<Schedule>.toScheduleDbEntity(): List<ScheduleDbEntity> {
    return map {
        it.toScheduleDbEntity()
    }
}
