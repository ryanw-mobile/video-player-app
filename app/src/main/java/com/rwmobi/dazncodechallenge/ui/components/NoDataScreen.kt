/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.ui.theme.VideoPlayerAppTheme

@Composable
fun NoDataScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .size(size = 64.dp),
            painter = painterResource(id = R.drawable.ic_baseline_error_outline_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = VideoPlayerAppTheme.colorScheme.onBackground.copy(alpha = 0.68f)),
        )

        Spacer(modifier = Modifier.height(height = VideoPlayerAppTheme.dimens.defaultFullPadding))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = VideoPlayerAppTheme.dimens.defaultHalfPadding),
            textAlign = TextAlign.Center,
            style = VideoPlayerAppTheme.typography.titleMedium,
            text = stringResource(R.string.no_data),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NoDataScreenPreview() {
    VideoPlayerAppTheme {
        Surface {
            NoDataScreen(modifier = Modifier.fillMaxSize())
        }
    }
}
