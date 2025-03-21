/*
 * Copyright (c) 2025. Ryan Wong
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

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `returns mapped event when single event network dto is provided`() {
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
    fun `returns mapped events when multiple event network dtos are provided`() {
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
    fun `returns mapped schedule when single schedule network dto is provided`() {
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
    fun `returns mapped schedules when multiple schedule network dtos are provided`() {
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
