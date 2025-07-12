/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.repository.mapper

import com.rwmobi.dazncodechallenge.data.source.network.dto.EventNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.dto.ScheduleNetworkDto
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

fun Schedule.toNetworkDto(): ScheduleNetworkDto = ScheduleNetworkDto(
    scheduleId = scheduleId,
    title = title,
    subtitle = subtitle,
    date = date,
    imageUrl = imageUrl,
)

fun Event.toNetworkDto(): EventNetworkDto = EventNetworkDto(
    eventId = eventId,
    title = title,
    subtitle = subtitle,
    date = date,
    imageUrl = imageUrl,
    videoUrl = videoUrl,
)
