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

val daznAppFontFamily = FontFamily(
    Font(R.font.average_sans_regular, FontWeight.Light),
    Font(R.font.average_sans_regular, FontWeight.Light, FontStyle.Italic),
    Font(R.font.average_sans_regular, FontWeight.Normal),
    Font(R.font.average_sans_regular, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.average_sans_regular, FontWeight.SemiBold),
    Font(R.font.average_sans_regular, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.merriweather_sans_bold, FontWeight.Bold),
    Font(R.font.merriweather_sans_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.merriweather_sans_extrabold, FontWeight.ExtraBold),
    Font(R.font.merriweather_sans_extrabold_italic, FontWeight.ExtraBold, FontStyle.Italic),
)

val DefaultTypography = Typography()
val appTypography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = daznAppFontFamily),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = daznAppFontFamily),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = daznAppFontFamily),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = daznAppFontFamily),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = daznAppFontFamily),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = daznAppFontFamily),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = daznAppFontFamily),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = daznAppFontFamily),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = daznAppFontFamily),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = daznAppFontFamily),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = daznAppFontFamily),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = daznAppFontFamily),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = daznAppFontFamily),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = daznAppFontFamily),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = daznAppFontFamily),
)
