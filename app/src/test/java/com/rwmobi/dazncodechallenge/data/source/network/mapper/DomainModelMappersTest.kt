/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.network.mapper

import com.rwmobi.dazncodechallenge.test.EventNetworkDtoSampleData
import com.rwmobi.dazncodechallenge.test.ScheduleNetworkDtoSampleData
import io.kotest.matchers.shouldBe
import org.junit.Test

internal class DomainModelMappersTest {

    @Test
    fun `Should convert EventNetworkDto to Event correctly`() {
        val dtos = listOf(
            EventNetworkDtoSampleData.eventNetworkDto1,
        )

        val events = dtos.asEvent()

        with(events.first()) {
            eventId shouldBe EventNetworkDtoSampleData.eventNetworkDto1.eventId
            title shouldBe EventNetworkDtoSampleData.eventNetworkDto1.title
            subtitle shouldBe EventNetworkDtoSampleData.eventNetworkDto1.subtitle
            date shouldBe EventNetworkDtoSampleData.eventNetworkDto1.date
            imageUrl shouldBe EventNetworkDtoSampleData.eventNetworkDto1.imageUrl
            videoUrl shouldBe EventNetworkDtoSampleData.eventNetworkDto1.videoUrl
        }
    }

    @Test
    fun `Should convert multiple EventNetworkDtos to Events correctly`() {
        val dtos = listOf(
            EventNetworkDtoSampleData.eventNetworkDto1,
            EventNetworkDtoSampleData.eventNetworkDto2,
            EventNetworkDtoSampleData.eventNetworkDto3,
        )

        val events = dtos.asEvent()

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
    fun `Should convert ScheduleNetworkDto to Schedule correctly`() {
        val dtos = listOf(
            ScheduleNetworkDtoSampleData.scheduleNetworkDto1,
        )

        val schedules = dtos.asSchedule()

        with(schedules.first()) {
            scheduleId shouldBe ScheduleNetworkDtoSampleData.scheduleNetworkDto1.scheduleId
            title shouldBe ScheduleNetworkDtoSampleData.scheduleNetworkDto1.title
            subtitle shouldBe ScheduleNetworkDtoSampleData.scheduleNetworkDto1.subtitle
            date shouldBe ScheduleNetworkDtoSampleData.scheduleNetworkDto1.date
            imageUrl shouldBe ScheduleNetworkDtoSampleData.scheduleNetworkDto1.imageUrl
        }
    }

    @Test
    fun `Should convert multiple ScheduleNetworkDtos to Schedules correctly`() {
        val dtos = listOf(
            ScheduleNetworkDtoSampleData.scheduleNetworkDto1,
            ScheduleNetworkDtoSampleData.scheduleNetworkDto2,
            ScheduleNetworkDtoSampleData.scheduleNetworkDto3,
        )

        val schedules = dtos.asSchedule()

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
