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
import io.kotest.matchers.shouldBe
import org.junit.Test

internal class DomainModelMappersTest {

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun convertEventNetworkDtoToEvent_ShouldMapFieldsCorrectly_ForSingleDto() {
        val dtos = listOf(
            EventNetworkDtoSampleData.event1,
        )

        val events = dtos.map { it.toEvent() }

        with(events.first()) {
            eventId shouldBe EventNetworkDtoSampleData.event1.eventId
            title shouldBe EventNetworkDtoSampleData.event1.title
            subtitle shouldBe EventNetworkDtoSampleData.event1.subtitle
            date shouldBe EventNetworkDtoSampleData.event1.date
            imageUrl shouldBe EventNetworkDtoSampleData.event1.imageUrl
            videoUrl shouldBe EventNetworkDtoSampleData.event1.videoUrl
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

        events.size shouldBe 3
        dtos.zip(events).forEach { (dto, event) ->
            with(event) {
                eventId shouldBe dto.eventId
                title shouldBe dto.title
                subtitle shouldBe dto.subtitle
                date shouldBe dto.date
                imageUrl shouldBe dto.imageUrl
                videoUrl shouldBe dto.videoUrl
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
            scheduleId shouldBe ScheduleNetworkDtoSampleData.schedule1.scheduleId
            title shouldBe ScheduleNetworkDtoSampleData.schedule1.title
            subtitle shouldBe ScheduleNetworkDtoSampleData.schedule1.subtitle
            date shouldBe ScheduleNetworkDtoSampleData.schedule1.date
            imageUrl shouldBe ScheduleNetworkDtoSampleData.schedule1.imageUrl
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

        schedules.size shouldBe 3
        dtos.zip(schedules).forEach { (dto, schedule) ->
            with(schedule) {
                scheduleId shouldBe dto.scheduleId
                title shouldBe dto.title
                subtitle shouldBe dto.subtitle
                date shouldBe dto.date
                imageUrl shouldBe dto.imageUrl
            }
        }
    }
}
