/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.test

import com.rwmobi.dazncodechallenge.data.source.network.dto.ScheduleNetworkDto
import com.rwmobi.dazncodechallenge.ui.utils.parseTimeStamp

object ScheduleNetworkDtoSampleData {
    val scheduleNetworkDto1 = ScheduleNetworkDto(
        scheduleId = 10,
        title = "Pre-Match ITV: Jürgen Klopp",
        subtitle = "",
        date = "2021-09-29T14:56:29.101Z".parseTimeStamp(),
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311354437259_image-header_pDach_1554838977000.jpeg?alt=media&token=8135fc30-3340-4449-9b45-daa9adc1bbc9",
    )
    val scheduleNetworkDto2 = ScheduleNetworkDto(
        scheduleId = 12,
        title = "Rockets @ Thunder",
        subtitle = "NBA",
        date = "2021-09-29T14:56:29.101Z".parseTimeStamp(),
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311471173073_image-header_pDach_1554571998000.jpeg?alt=media&token=a69da8e4-d2d1-45f0-a005-977311981d66",
    )
    val scheduleNetworkDto3 = ScheduleNetworkDto(
        scheduleId = 13,
        title = "PSG v Strasbourg",
        subtitle = "Ligue 1",
        date = "2021-09-29T14:56:29.101Z".parseTimeStamp(),
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311953989300_image-header_pDach_1554750608000.jpeg?alt=media&token=56f3a7a8-2f10-436c-8069-c762b37594cd",
    )
}
