/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.util

import android.content.Context
import uk.ryanwong.dazn.codechallenge.BuildConfig
import uk.ryanwong.dazn.codechallenge.R

/***
 * Depends on the user experience, sometimes we may want to just hide the technical details
 * when an error occurs, or we may want to provide different high level error messages
 * depends on the known exception types. On debug builds we may want show the errors.
 */
fun filterErrorMessage(
    context: Context,
    message: String,
): String =
    when (BuildConfig.DEBUG) {
        true -> "${context.getString(R.string.generic_error_string)} ($message)"
        false -> context.getString(R.string.generic_error_string)
    }
