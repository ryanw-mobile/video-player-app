/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.test

import android.net.Uri
import androidx.media3.common.PlaybackException
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.HttpDataSource
import java.io.IOException

object PlaybackExceptionSampleData {
    val invalidResponseCodeExceptionErrorCode = 403
    val invalidResponseCodeException = PlaybackException(
        "Connection Failed",
        HttpDataSource.InvalidResponseCodeException(
            invalidResponseCodeExceptionErrorCode,
            "Response message",
            IOException("Cause message"),
            emptyMap(),
            DataSpec(Uri.parse("https://someview.com/video.mp4")),
            ByteArray(0),
        ),
        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
    )

    val httpDataSourceExceptionMessage = "Some IO Exception"
    val httpDataSourceException = PlaybackException(
        "Connection Failed",
        HttpDataSource.HttpDataSourceException(
            "Error message",
            IOException(httpDataSourceExceptionMessage),
            DataSpec(Uri.parse("https://someview.com/video.mp4")),
            PlaybackException.ERROR_CODE_IO_UNSPECIFIED,
            HttpDataSource.HttpDataSourceException.TYPE_OPEN,
        ),
        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
    )

    val genericExceptionMessage = "Some generic exception"
    val genericException = PlaybackException(
        "Connection Failed",
        Exception(genericExceptionMessage),
        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
    )
}
