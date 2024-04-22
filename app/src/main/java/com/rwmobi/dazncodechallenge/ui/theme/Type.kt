/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.dazncodechallenge.R

val averageSansFontFamily = FontFamily(
    Font(R.font.average_sans_regular, FontWeight.Light),
    Font(R.font.average_sans_regular, FontWeight.Light, FontStyle.Italic),
    Font(R.font.average_sans_regular, FontWeight.Normal),
    Font(R.font.average_sans_regular, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.average_sans_regular, FontWeight.SemiBold),
    Font(R.font.average_sans_regular, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.average_sans_regular, FontWeight.Bold),
    Font(R.font.average_sans_regular, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.average_sans_regular, FontWeight.ExtraBold),
    Font(R.font.average_sans_regular, FontWeight.ExtraBold, FontStyle.Italic),
)

val DefaultTypography = Typography()
val appTypography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = averageSansFontFamily),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = averageSansFontFamily),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = averageSansFontFamily),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = averageSansFontFamily),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = averageSansFontFamily),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = averageSansFontFamily),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = averageSansFontFamily),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = averageSansFontFamily),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = averageSansFontFamily),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = averageSansFontFamily),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = averageSansFontFamily),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = averageSansFontFamily),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = averageSansFontFamily),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = averageSansFontFamily),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = averageSansFontFamily),
)
