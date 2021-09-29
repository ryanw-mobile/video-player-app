/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package util

import java.text.SimpleDateFormat
import java.util.*

fun String.parseTimeStamp(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss"): Date {
    val dateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    return dateFormat.parse(this)
}
