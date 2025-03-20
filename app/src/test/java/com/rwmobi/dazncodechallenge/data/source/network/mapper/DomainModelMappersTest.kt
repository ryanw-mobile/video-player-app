/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.network.mapper

import com.rwmobi.dazncodechallenge.data.repository.mapper.toEvent
import com.rwmobi.dazncodechallenge.data.repository.mapper.toSchedule
import com.rwmobi.dazncodechallenge.test.EventNetworkDtoSampleData
import com.rwmobi.dazncodechallenge.test.ScheduleNetworkDtoSampleData
import org.junit.Test
import kotlin.test.assertEquals

internal class DomainModelMappersTest {

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun convertEventNetworkDtoToEvent_ShouldMapFieldsCorrectly_ForSingleDto() {
        val dtos = listOf(
            EventNetworkDtoSampleData.event1,
        )

        val events = dtos.map { it.toEvent() }

        with(events.first()) {
            assertEquals(EventNetworkDtoSampleData.event1.eventId, eventId)
            assertEquals(EventNetworkDtoSampleData.event1.title, title)
            assertEquals(EventNetworkDtoSampleData.event1.subtitle, subtitle)
            assertEquals(EventNetworkDtoSampleData.event1.date, date)
            assertEquals(EventNetworkDtoSampleData.event1.imageUrl, imageUrl)
            assertEquals(EventNetworkDtoSampleData.event1.videoUrl, videoUrl)
        }
    }

    @Test
    fun convertEventNetworkDtosToEvents_ShouldMapFieldsCorrectly_ForMultipleDtos() {
        val dtos = listOf(
            EventNetworkDtoSampleData.event1,
            EventNetworkDtoSampleData.event2,
            EventNetworkDtoSampleData.event3,
        )

        val events = dtos.map { it.toEvent() }

        assertEquals(3, events.size)
        dtos.zip(events).forEach { (dto, event) ->
            with(event) {
                assertEquals(dto.eventId, eventId)
                assertEquals(dto.title, title)
                assertEquals(dto.subtitle, subtitle)
                assertEquals(dto.date, date)
                assertEquals(dto.imageUrl, imageUrl)
                assertEquals(dto.videoUrl, videoUrl)
            }
        }
    }

    @Test
    fun convertScheduleNetworkDtoToSchedule_ShouldMapFieldsCorrectly_ForSingleDto() {
        val dtos = listOf(
            ScheduleNetworkDtoSampleData.schedule1,
        )

        val schedules = dtos.map { it.toSchedule() }

        with(schedules.first()) {
            assertEquals(ScheduleNetworkDtoSampleData.schedule1.scheduleId, scheduleId)
            assertEquals(ScheduleNetworkDtoSampleData.schedule1.title, title)
            assertEquals(ScheduleNetworkDtoSampleData.schedule1.subtitle, subtitle)
            assertEquals(ScheduleNetworkDtoSampleData.schedule1.date, date)
            assertEquals(ScheduleNetworkDtoSampleData.schedule1.imageUrl, imageUrl)
        }
    }

    @Test
    fun convertScheduleNetworkDtosToSchedules_ShouldMapFieldsCorrectly_ForMultipleDtos() {
        val dtos = listOf(
            ScheduleNetworkDtoSampleData.schedule1,
            ScheduleNetworkDtoSampleData.schedule2,
            ScheduleNetworkDtoSampleData.schedule3,
        )

        val schedules = dtos.map { it.toSchedule() }

        assertEquals(3, schedules.size)
        dtos.zip(schedules).forEach { (dto, schedule) ->
            with(schedule) {
                assertEquals(dto.scheduleId, scheduleId)
                assertEquals(dto.title, title)
                assertEquals(dto.subtitle, subtitle)
                assertEquals(dto.date, date)
                assertEquals(dto.imageUrl, imageUrl)
            }
        }
    }
}
