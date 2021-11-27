/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.util.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import uk.ryanwong.dazn.codechallenge.R
import java.text.SimpleDateFormat
import java.util.*

fun String.parseTimeStamp(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss"): Date {
    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    return simpleDateFormat.parse(this)!!
}