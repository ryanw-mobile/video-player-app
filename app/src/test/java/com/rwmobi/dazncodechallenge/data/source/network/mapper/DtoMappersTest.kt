/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.network.mapper

import com.rwmobi.dazncodechallenge.data.repository.mapper.toNetworkDto
import com.rwmobi.dazncodechallenge.test.EventSampleData
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData
import org.junit.Test
import kotlin.test.assertEquals

internal class DtoMappersTest {

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `maps event fields correctly when converting a single event to event network DTO`() {
        val event = EventSampleData.event1
        val dto = event.toNetworkDto()
        with(dto) {
            assertEquals(EventSampleData.event1.eventId, eventId)
            assertEquals(EventSampleData.event1.title, title)
            assertEquals(EventSampleData.event1.subtitle, subtitle)
            assertEquals(EventSampleData.event1.date, date)
            assertEquals(EventSampleData.event1.imageUrl, imageUrl)
            assertEquals(EventSampleData.event1.videoUrl, videoUrl)
        }
    }

    @Test
    fun `maps event fields correctly when converting multiple events to event network DTOs`() {
        val events = listOf(
            EventSampleData.event1,
            EventSampleData.event2,
            EventSampleData.event3,
        )
        val dtos = events.map { it.toNetworkDto() }

        assertEquals(events.size, dtos.size)
        events.zip(dtos).forEach { (event, dto) ->
            with(dto) {
                assertEquals(event.eventId, eventId)
                assertEquals(event.title, title)
                assertEquals(event.subtitle, subtitle)
                assertEquals(event.date, date)
                assertEquals(event.imageUrl, imageUrl)
                assertEquals(event.videoUrl, videoUrl)
            }
        }
    }

    @Test
    fun `maps schedule fields correctly when converting a single schedule to schedule network DTO`() {
        val schedule = ScheduleSampleData.schedule1
        val dto = schedule.toNetworkDto()
        with(dto) {
            assertEquals(ScheduleSampleData.schedule1.scheduleId, scheduleId)
            assertEquals(ScheduleSampleData.schedule1.title, title)
            assertEquals(ScheduleSampleData.schedule1.subtitle, subtitle)
            assertEquals(ScheduleSampleData.schedule1.date, date)
            assertEquals(ScheduleSampleData.schedule1.imageUrl, imageUrl)
        }
    }

    @Test
    fun `maps schedule fields correctly when converting multiple schedules to schedule network DTOs`() {
        val schedules = listOf(
            ScheduleSampleData.schedule1,
            ScheduleSampleData.schedule2,
            ScheduleSampleData.schedule3,
        )
        val dtos = schedules.map { it.toNetworkDto() }

        assertEquals(schedules.size, dtos.size)
        schedules.zip(dtos).forEach { (schedule, dto) ->
            with(dto) {
                assertEquals(schedule.scheduleId, scheduleId)
                assertEquals(schedule.title, title)
                assertEquals(schedule.subtitle, subtitle)
                assertEquals(schedule.date, date)
                assertEquals(schedule.imageUrl, imageUrl)
            }
        }
    }
}
