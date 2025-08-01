/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rwmobi.dazncodechallenge.R
import com.rwmobi.dazncodechallenge.ui.navigation.AppNavItem
import com.rwmobi.dazncodechallenge.ui.theme.VideoPlayerAppTheme
import com.rwmobi.dazncodechallenge.ui.theme.dazn_navigation_checked
import com.rwmobi.dazncodechallenge.ui.theme.dazn_navigation_unchecked

@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    onCurrentRouteSecondTapped: (item: AppNavItem) -> Unit,
) {
    val context = LocalContext.current

    NavigationBar(
        modifier = modifier.semantics {
            contentDescription = context.getString(R.string.content_description_navigation_bar)
        },
        tonalElevation = 0.dp,
        containerColor = VideoPlayerAppTheme.colorScheme.background,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        for (item in AppNavItem.navBarItems) {
            val selected = currentRoute == item.screenRoute

            NavigationBarItem(
                modifier = Modifier.semantics { contentDescription = context.getString(item.titleResId) },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.screenRoute) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    } else {
                        onCurrentRouteSecondTapped(item)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.titleResId).uppercase(),
                        style = VideoPlayerAppTheme.typography.labelMedium,
                    )
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = dazn_navigation_checked,
                    selectedTextColor = dazn_navigation_checked,
                    selectedIndicatorColor = Color.Transparent,
                    unselectedIconColor = dazn_navigation_unchecked,
                    unselectedTextColor = dazn_navigation_unchecked,
                    disabledIconColor = dazn_navigation_unchecked,
                    disabledTextColor = dazn_navigation_unchecked,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    VideoPlayerAppTheme {
        Surface {
            AppBottomNavigationBar(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(0.dp),
                navController = rememberNavController(),
                onCurrentRouteSecondTapped = {},
            )
        }
    }
}
