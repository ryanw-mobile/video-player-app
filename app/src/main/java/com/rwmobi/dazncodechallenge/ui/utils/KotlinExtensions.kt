/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AppOpsManager
import android.app.PictureInPictureParams
import android.content.Context
import android.os.Build
import android.text.format.DateUtils
import android.util.Rational
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun String.parseTimeStamp(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss"): Date {
    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    return simpleDateFormat.parse(this)!!
}

internal fun Date.asNiceString(context: Context): String {
    // Data formatting similar but not exactly the same as the mockups.
    // This is done without using any 3rd party library
    return DateUtils.getRelativeDateTimeString(
        context,
        time,
        DateUtils.DAY_IN_MILLIS,
        DateUtils.DAY_IN_MILLIS * 3,
        0,
    ).toString()
}

internal fun Activity.enterPIPMode(aspectRatio: Rational) {
    val params = PictureInPictureParams.Builder()
        .setAspectRatio(aspectRatio)
        .build()
    enterPictureInPictureMode(params)
}

@SuppressLint("ObsoleteSdkInt")
internal fun Context.hasPictureInPicturePermission(): Boolean {
    val appOps = getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager?
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps?.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                android.os.Process.myUid(),
                packageName,
            )
        } else {
            @Suppress("DEPRECATION")
            appOps?.checkOpNoThrow(
                AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                android.os.Process.myUid(),
                packageName,
            )
        }
        mode == AppOpsManager.MODE_ALLOWED
    } else {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }
}
