/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.previewparameter

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rwmobi.dazncodechallenge.domain.model.Schedule
import com.rwmobi.dazncodechallenge.ui.utils.parseTimeStamp

@Suppress("MaxLineLength")
class ScheduleProvider : PreviewParameterProvider<Schedule> {
    override val values: Sequence<Schedule>
        get() = sequenceOf(
            Schedule(
                scheduleId = 1,
                title = "Liverpool v Porto",
                subtitle = "UEFA Champions League",
                date = "2024-04-23T06:28:14.604Z".parseTimeStamp(),
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310176837169_image-header_pDach_1554579780000.jpeg?alt=media&token=1777d26b-d051-4b5f-87a8-7633d3d6dd20",
            ),
        )
}
