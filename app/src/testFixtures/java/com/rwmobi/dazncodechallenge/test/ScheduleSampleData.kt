/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.test

import com.rwmobi.dazncodechallenge.domain.model.Schedule
import com.rwmobi.dazncodechallenge.ui.utils.parseTimeStamp

object ScheduleSampleData {
    val schedule1 =
        Schedule(
            scheduleId = 10,
            title = "Pre-Match ITV: Jürgen Klopp",
            subtitle = "",
            date = "2021-09-29T14:56:29.101Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311354437259_image-header_pDach_1554838977000.jpeg?alt=media&token=8135fc30-3340-4449-9b45-daa9adc1bbc9",
        )

    val schedule1Modified =
        Schedule(
            scheduleId = 10,
            title = "CSKA Moskow v St Petersburg",
            subtitle = "KHL Ice Hockey",
            date = "2021-09-29T14:56:29.101Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311428677455_image-header_pDach_1554829417000.jpeg?alt=media&token=ea122c47-2a50-4cf2-a901-2be2ff94f3c4",
        )

    val schedule2 =
        Schedule(
            scheduleId = 12,
            title = "Rockets @ Thunder",
            subtitle = "NBA",
            date = "2021-09-29T14:56:29.101Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311471173073_image-header_pDach_1554571998000.jpeg?alt=media&token=a69da8e4-d2d1-45f0-a005-977311981d66",
        )

    val schedule3 =
        Schedule(
            scheduleId = 13,
            title = "PSG v Strasbourg",
            subtitle = "Ligue 1",
            date = "2021-09-29T14:56:29.101Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311953989300_image-header_pDach_1554750608000.jpeg?alt=media&token=56f3a7a8-2f10-436c-8069-c762b37594cd",
        )
}
