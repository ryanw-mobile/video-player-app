/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge

import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule
import uk.ryanwong.dazn.codechallenge.util.extensions.parseTimeStamp

/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

object TestData {
    val event1 = Event(
        1,
        "Liverpool v Porto",
        "UEFA Champions League",
        "2021-09-28T01:55:56.925Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310176837169_image-header_pDach_1554579780000.jpeg?alt=media&token=1777d26b-d051-4b5f-87a8-7633d3d6dd20",
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
    )

    val event1Modified = Event(
        1,
        "Nîmes v Rennes",
        "Ligue 1",
        "2021-09-28T02:55:56.925Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310381637057_image-header_pDach_1554664873000.jpeg?alt=media&token=53616931-55a8-476e-b1b7-d18fc22a2bf0",
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
    )

    val event2 = Event(
        2,
        "Nîmes v Rennes",
        "Ligue 1",
        "2021-09-28T02:55:56.925Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310381637057_image-header_pDach_1554664873000.jpeg?alt=media&token=53616931-55a8-476e-b1b7-d18fc22a2bf0",
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
    )

    val event3 = Event(
        3,
        "Tottenham v Man City",
        "UEFA Champions League",
        "2021-09-28T03:55:56.925Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310511685198_image-header_pDach_1554872450000.jpeg?alt=media&token=5524d719-261e-49e6-abf3-a74c30df3e27",
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
    )

    val schedule1 = Schedule(
        10,
        "Pre-Match ITV: Jürgen Klopp",
        "",
        "2021-09-29T14:56:29.101Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311354437259_image-header_pDach_1554838977000.jpeg?alt=media&token=8135fc30-3340-4449-9b45-daa9adc1bbc9"
    )

    val schedule1Modified = Schedule(
        10,
        "CSKA Moskow v St Petersburg",
        "KHL Ice Hockey",
        "2021-09-29T14:56:29.101Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311428677455_image-header_pDach_1554829417000.jpeg?alt=media&token=ea122c47-2a50-4cf2-a901-2be2ff94f3c4"
    )

    val schedule2 = Schedule(
        12,
        "Rockets @ Thunder",
        "NBA",
        "2021-09-29T14:56:29.101Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311471173073_image-header_pDach_1554571998000.jpeg?alt=media&token=a69da8e4-d2d1-45f0-a005-977311981d66"
    )

    val schedule3 = Schedule(
        13,
        "PSG v Strasbourg",
        "Ligue 1",
        "2021-09-29T14:56:29.101Z".parseTimeStamp(),
        "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311953989300_image-header_pDach_1554750608000.jpeg?alt=media&token=56f3a7a8-2f10-436c-8069-c762b37594cd"
    )
}