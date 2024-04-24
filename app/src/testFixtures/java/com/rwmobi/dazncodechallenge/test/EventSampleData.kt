/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.test

import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.ui.utils.parseTimeStamp

/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

object EventSampleData {
    val event1 =
        Event(
            eventId = 1,
            title = "Liverpool v Porto",
            subtitle = "UEFA Champions League",
            date = "2021-09-28T01:55:56.925Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310176837169_image-header_pDach_1554579780000.jpeg?alt=media&token=1777d26b-d051-4b5f-87a8-7633d3d6dd20",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        )

    val event1Modified =
        Event(
            eventId = 1,
            title = "Nîmes v Rennes",
            subtitle = "Ligue 1",
            date = "2021-09-28T02:55:56.925Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310381637057_image-header_pDach_1554664873000.jpeg?alt=media&token=53616931-55a8-476e-b1b7-d18fc22a2bf0",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        )

    val event2 =
        Event(
            eventId = 2,
            title = "Nîmes v Rennes",
            subtitle = "Ligue 1",
            date = "2021-09-28T02:55:56.925Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310381637057_image-header_pDach_1554664873000.jpeg?alt=media&token=53616931-55a8-476e-b1b7-d18fc22a2bf0",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        )

    val event3 =
        Event(
            eventId = 3,
            title = "Tottenham v Man City",
            subtitle = "UEFA Champions League",
            date = "2021-09-28T03:55:56.925Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310511685198_image-header_pDach_1554872450000.jpeg?alt=media&token=5524d719-261e-49e6-abf3-a74c30df3e27",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        )
}
