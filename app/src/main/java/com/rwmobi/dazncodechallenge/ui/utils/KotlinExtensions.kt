/*
 * Copyright (c) 2025. Ryan Wong
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
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun String.parseTimeStamp(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss"): Date {
    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    return simpleDateFormat.parse(this)!!
}

internal fun Date.toRelativeDateTimeString(context: Context): String {
    // Data formatting similar but not exactly the same as the mockups.
    // This is done using the Android library (in Java, so not KMP compatible)
    return DateUtils.getRelativeDateTimeString(
        /* c = */
        context,
        /* time = */
        time,
        /* minResolution = */
        DateUtils.DAY_IN_MILLIS,
        /* transitionResolution = */
        DateUtils.DAY_IN_MILLIS * 3,
        /* flags = */
        0,
    ).toString()
}

internal fun Activity.enterPIPMode(aspectRatio: Rational) {
    val params = PictureInPictureParams.Builder()
        .setAspectRatio(aspectRatio)
        .build()
    enterPictureInPictureMode(params)
}

@Suppress("DEPRECATION")
internal fun Activity.enterFullScreenMode() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = window.insetsController
        controller?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        controller?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        // For older versions, use legacy system UI flags
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
    }
}

@Suppress("DEPRECATION")
internal fun Activity.exitFullScreenMode() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = window.insetsController
        controller?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
    } else {
        // Reset system UI flags for older versions
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }
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
