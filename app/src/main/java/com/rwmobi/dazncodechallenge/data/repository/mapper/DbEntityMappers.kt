/*
 * Copyright (c) 2025. Ryan Wong
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

fun Event.toDbEntity(): EventDbEntity {
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

fun EventNetworkDto.toDbEntity(): EventDbEntity {
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

fun Schedule.toDbEntity(): ScheduleDbEntity {
    return ScheduleDbEntity(
        scheduleId = this.scheduleId,
        title = this.title,
        subtitle = this.subtitle,
        date = this.date,
        imageUrl = this.imageUrl,
        dirty = false,
    )
}

fun ScheduleNetworkDto.toDbEntity(): ScheduleDbEntity {
    return ScheduleDbEntity(
        scheduleId = this.scheduleId,
        title = this.title,
        subtitle = this.subtitle,
        date = this.date,
        imageUrl = this.imageUrl,
        dirty = false,
    )
}
