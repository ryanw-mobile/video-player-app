/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.rwmobi.dazncodechallenge.R

sealed class AppNavItem(var screenRoute: String, @StringRes var titleResId: Int, @DrawableRes var iconResId: Int) {

    data object Events : AppNavItem(
        titleResId = R.string.events,
        iconResId = R.drawable.ic_baseline_local_activity_24,
        screenRoute = "events",
    )

    data object Schedule : AppNavItem(
        titleResId = R.string.schedule,
        iconResId = R.drawable.ic_baseline_calendar_today_24,
        screenRoute = "schedule",
    )

    data object Exoplayer : AppNavItem(
        titleResId = R.string.events,
        iconResId = R.drawable.ic_baseline_local_activity_24,
        screenRoute = "exoplayer",
    )

    companion object {
        // Exoplayer won't be in the navigation bar or rail
        val navBarItems: List<AppNavItem>
            get() = listOf(Events, Schedule)
    }
}
