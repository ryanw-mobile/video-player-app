/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.utils

import android.content.Context
import com.rwmobi.dazncodechallenge.BuildConfig
import com.rwmobi.dazncodechallenge.R

/***
 * Depends on the user experience, sometimes we may want to just hide the technical details
 * when an error occurs, or we may want to provide different high level error messages
 * depends on the known exception types. On debug builds we may want show the errors.
 */
fun filterErrorMessage(
    context: Context,
    message: String,
): String = when (BuildConfig.DEBUG) {
    true -> "${context.getString(R.string.generic_error_string)} ($message)"
    false -> context.getString(R.string.generic_error_string)
}
