/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.events

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.ui.components.EventListItem
import com.rwmobi.dazncodechallenge.ui.previewparameter.EventsProvider
import com.rwmobi.dazncodechallenge.ui.theme.DAZNCodeChallengeTheme
import com.rwmobi.dazncodechallenge.ui.theme.dazn_divider
import com.rwmobi.dazncodechallenge.ui.theme.getDimension

@Composable
internal fun EventsList(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    events: List<Event>,
    requestScrollToTop: Boolean,
    onScrolledToTop: () -> Unit,
    onPlayVideo: (videoUrl: String) -> Unit,
) {
    val dimension = LocalConfiguration.current.getDimension()
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = context.getString(R.string.content_description_events_list) },
        state = lazyListState,
    ) {
        itemsIndexed(
            items = events,
            key = { _, event -> event.eventId },
        ) { index, event ->
            EventListItem(
                modifier = Modifier.fillMaxWidth(),
                event = event,
                imageLoader = imageLoader,
                onItemClicked = { onPlayVideo(it) },
            )

            if (index < events.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimension.grid_0_5),
                    thickness = 1.dp,
                    color = dazn_divider,
                )
            }
        }
    }

    LaunchedEffect(requestScrollToTop) {
        if (requestScrollToTop) {
            lazyListState.scrollToItem(index = 0)
            onScrolledToTop()
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun Preview(
    @PreviewParameter(EventsProvider::class) events: List<Event>,
) {
    DAZNCodeChallengeTheme {
        Surface {
            EventsList(
                modifier = Modifier.fillMaxSize(),
                imageLoader = ImageLoader(LocalContext.current),
                events = events,
                requestScrollToTop = false,
                onScrolledToTop = {},
                onPlayVideo = {},
            )
        }
    }
}
