/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

/*
 * Sample data courtesy of DAZN
 */

package com.rwmobi.dazncodechallenge.ui.test

import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.ui.utils.parseTimeStamp

internal object EventsListSampleData {
    val listOfSixteen: List<Event> = listOf(
        Event(
            eventId = 1,
            title = "Liverpool v Porto",
            subtitle = "UEFA Champions League",
            date = "2024-04-22T01:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310176837169_image-header_pDach_1554579780000.jpeg?alt=media&token=1777d26b-d051-4b5f-87a8-7633d3d6dd20",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 2,
            title = "Nîmes v Rennes",
            subtitle = "Ligue 1",
            date = "2024-04-22T02:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310381637057_image-header_pDach_1554664873000.jpeg?alt=media&token=53616931-55a8-476e-b1b7-d18fc22a2bf0",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 3,
            title = "Tottenham v Man City",
            subtitle = "UEFA Champions League",
            date = "2024-04-22T03:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310511685198_image-header_pDach_1554872450000.jpeg?alt=media&token=5524d719-261e-49e6-abf3-a74c30df3e27",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 4,
            title = "Suns @ Mavericks",
            subtitle = "NBA",
            date = "2024-04-22T04:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310572613233_image-header_pDach_1554812508000.jpeg?alt=media&token=4ee99b47-dcae-4016-b213-54e14a0d40d8",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 5,
            title = "Chelsea v West Ham",
            subtitle = "Premier League",
            date = "2024-04-22T05:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310632005268_image-header_pDach_1554838415000.jpeg?alt=media&token=5a512da9-d268-432c-9da2-088c8e6737e1",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 6,
            title = "Hamburger SV v 1. FC Magdeburg",
            subtitle = "2. Bundesliga",
            date = "2024-04-22T06:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310638661042_image-header_pDach_1554833728000.jpeg?alt=media&token=0415f88e-8d07-4b95-b5d6-4ee3efaa8e5c",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 7,
            title = "Virginia v Texas Tech",
            subtitle = "NCAA College Basketball",
            date = "2024-04-22T08:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310667845502_image-header_pDach_1554723738000.jpeg?alt=media&token=8c09ca4a-867c-4a26-9e56-463523808fa5",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 8,
            title = "Dodgers @ Cardinals",
            subtitle = "MLB Baseball",
            date = "2024-04-22T09:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310798917361_image-header_pDach_1554667420000.jpeg?alt=media&token=7c15b201-0842-4aaf-acb4-6bd2c6a5bb4d",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 9,
            title = "Interview Sadio Mane",
            subtitle = "#DAZNbreakfast",
            date = "2024-04-22T10:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311013957255_image-header_pDach_1554750958000.jpeg?alt=media&token=5bf9b5fd-52c1-4c54-8f7d-4671d9385f5c",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 10,
            title = "Pre-Match ITV: Jürgen Klopp",
            subtitle = "",
            date = "2024-04-22T10:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311354437259_image-header_pDach_1554838977000.jpeg?alt=media&token=8135fc30-3340-4449-9b45-daa9adc1bbc9",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 11,
            title = "CSKA Moskow v St Petersburg",
            subtitle = "KHL Ice Hockey",
            date = "2024-04-22T10:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311428677455_image-header_pDach_1554829417000.jpeg?alt=media&token=ea122c47-2a50-4cf2-a901-2be2ff94f3c4",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 12,
            title = "Rockets @ Thunder",
            subtitle = "NBA",
            date = "2024-04-22T10:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311471173073_image-header_pDach_1554571998000.jpeg?alt=media&token=a69da8e4-d2d1-45f0-a005-977311981d66",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 13,
            title = "PSG v Strasbourg",
            subtitle = "Ligue 1",
            date = "2024-04-22T10:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/311953989300_image-header_pDach_1554750608000.jpeg?alt=media&token=56f3a7a8-2f10-436c-8069-c762b37594cd",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 14,
            title = "Barcelona v Atlético Madrid",
            subtitle = "La Liga",
            date = "2024-04-22T10:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/313767493498_image-header_pDach_1554718914000.jpeg?alt=media&token=77861419-1012-4b1d-ab36-04c53488aee9",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 15,
            title = "Career of a legend",
            subtitle = "Documentary",
            date = "2024-04-22T10:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/314428485312_image-header_pDach_1554414738000.jpeg?alt=media&token=60571ae8-9fb0-4724-9b41-ce9f7bf36334",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
        Event(
            eventId = 16,
            title = "Mavericks @ Grizzlies",
            subtitle = "NBA",
            date = "2024-04-22T10:27:28.136Z".parseTimeStamp(),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/314686021006_image-header_pDach_1554739950000.jpeg?alt=media&token=bf82b1da-e965-44f5-ac08-2170b4625566",
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
        ),
    )
}
