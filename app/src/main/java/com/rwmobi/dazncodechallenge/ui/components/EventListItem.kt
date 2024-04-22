/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.ui.previewparameter.EventProvider
import com.rwmobi.dazncodechallenge.ui.theme.DAZNCodeChallengeTheme
import com.rwmobi.dazncodechallenge.ui.theme.dazn_divider
import com.rwmobi.dazncodechallenge.ui.theme.getDimension
import com.rwmobi.dazncodechallenge.ui.utils.asNiceString

@Composable
fun EventListItem(
    modifier: Modifier = Modifier,
    event: Event,
    imageLoader: ImageLoader,
) {
    val dimension = LocalConfiguration.current.getDimension()
    val context = LocalContext.current

    Row(
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Min),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(ratio = (4f / 3f))
                .background(color = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.3f)),
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(event.imageUrl)
                .crossfade(true)
                .build(),
            fallback = ColorPainter(dazn_divider),
            error = ColorPainter(dazn_divider),
            placeholder = ColorPainter(dazn_divider),
            contentDescription = event.title,
            contentScale = ContentScale.Crop,
            imageLoader = imageLoader,
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(weight = 1f),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = dimension.minListItemHeight)
                    .padding(
                        horizontal = dimension.defaultFullPadding,
                        vertical = dimension.defaultHalfPadding,
                    ),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = event.title,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = dimension.minListItemHeight)
                    .padding(
                        horizontal = dimension.defaultFullPadding,
                        vertical = dimension.defaultHalfPadding,
                    ),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = event.subtitle,
            )

            Spacer(Modifier.weight(weight = 1f))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = dimension.minListItemHeight)
                    .padding(
                        horizontal = dimension.defaultFullPadding,
                        vertical = dimension.defaultHalfPadding,
                    ),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = event.date.asNiceString(context = context),
            )
        }
    }
}

@PreviewFontScale
@Composable
private fun Preview(
    @PreviewParameter(EventProvider::class) event: Event,
) {
    DAZNCodeChallengeTheme {
        Surface {
            EventListItem(
                modifier = Modifier.fillMaxWidth(),
                event = event,
                imageLoader = ImageLoader(LocalContext.current),
            )
        }
    }
}
