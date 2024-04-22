/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.previewparameter

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.ui.utils.parseTimeStamp

class EventProvider : PreviewParameterProvider<Event> {
    override val values: Sequence<Event>
        get() = sequenceOf(
            Event(
                eventId = 1,
                title = "Liverpool v Porto",
                subtitle = "UEFA Champions League",
                date = "2024-04-22T01:27:28.136Z".parseTimeStamp(),
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310176837169_image-header_pDach_1554579780000.jpeg?alt=media&token=1777d26b-d051-4b5f-87a8-7633d3d6dd20",
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media",
            ),
        )
}
