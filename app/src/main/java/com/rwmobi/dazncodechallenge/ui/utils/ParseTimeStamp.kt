/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package uk.ryanwong.dazn.codechallenge.util.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.parseTimeStamp(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss"): Date {
    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    return simpleDateFormat.parse(this)!!
}
