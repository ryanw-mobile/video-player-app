/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.network.mapper

import com.rwmobi.dazncodechallenge.data.repository.mapper.toNetworkDto
import com.rwmobi.dazncodechallenge.test.EventSampleData
import com.rwmobi.dazncodechallenge.test.ScheduleSampleData
import io.kotest.matchers.shouldBe
import org.junit.Test

internal class DtoMappersTest {

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun convertEventToEventNetworkDto_ShouldMapFieldsCorrectly_ForSingleEvent() {
        val event = EventSampleData.event1
        val dto = event.toNetworkDto()
        with(dto) {
            eventId shouldBe EventSampleData.event1.eventId
            title shouldBe EventSampleData.event1.title
            subtitle shouldBe EventSampleData.event1.subtitle
            date shouldBe EventSampleData.event1.date
            imageUrl shouldBe EventSampleData.event1.imageUrl
            videoUrl shouldBe EventSampleData.event1.videoUrl
        }
    }

    @Test
    fun convertEventsToEventNetworkDtos_ShouldMapFieldsCorrectly_ForMultipleEvents() {
        val events = listOf(
            EventSampleData.event1,
            EventSampleData.event2,
            EventSampleData.event3,
        )
        val dtos = events.map { it.toNetworkDto() }

        dtos.size shouldBe events.size
        events.zip(dtos).forEach { (event, dto) ->
            with(dto) {
                eventId shouldBe event.eventId
                title shouldBe event.title
                subtitle shouldBe event.subtitle
                date shouldBe event.date
                imageUrl shouldBe event.imageUrl
                videoUrl shouldBe event.videoUrl
            }
        }
    }

    @Test
    fun convertScheduleToScheduleNetworkDto_ShouldMapFieldsCorrectly_ForSingleSchedule() {
        val schedule = ScheduleSampleData.schedule1
        val dto = schedule.toNetworkDto()
        with(dto) {
            scheduleId shouldBe ScheduleSampleData.schedule1.scheduleId
            title shouldBe ScheduleSampleData.schedule1.title
            subtitle shouldBe ScheduleSampleData.schedule1.subtitle
            date shouldBe ScheduleSampleData.schedule1.date
            imageUrl shouldBe ScheduleSampleData.schedule1.imageUrl
        }
    }

    @Test
    fun convertSchedulesToScheduleNetworkDtos_ShouldMapFieldsCorrectly_ForMultipleSchedules() {
        val schedules = listOf(
            ScheduleSampleData.schedule1,
            ScheduleSampleData.schedule2,
            ScheduleSampleData.schedule3,
        )
        val dtos = schedules.map { it.toNetworkDto() }

        dtos.size shouldBe schedules.size
        schedules.zip(dtos).forEach { (schedule, dto) ->
            with(dto) {
                scheduleId shouldBe schedule.scheduleId
                title shouldBe schedule.title
                subtitle shouldBe schedule.subtitle
                date shouldBe schedule.date
                imageUrl shouldBe schedule.imageUrl
            }
        }
    }
}
