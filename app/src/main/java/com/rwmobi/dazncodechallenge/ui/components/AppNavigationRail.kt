/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.dazncodechallenge.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
import com.rwmobi.dazncodechallenge.ui.navigation.AppNavItem
import com.rwmobi.dazncodechallenge.ui.theme.DAZNCodeChallengeTheme
import com.rwmobi.dazncodechallenge.ui.theme.getDimension
import java.util.Locale

@Composable
fun AppNavigationRail(
    modifier: Modifier = Modifier,
    navController: NavController,
    onCurrentRouteSecondTapped: (item: AppNavItem) -> Unit,
) {
    NavigationRail(
        modifier = modifier,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val dimension = LocalConfiguration.current.getDimension()
        val context = LocalContext.current

        Spacer(Modifier.weight(1f))

        for (item in AppNavItem.navBarItems) {
            val selected = currentRoute == item.screenRoute

            NavigationRailItem(
                modifier = Modifier
                    .padding(vertical = dimension.defaultFullPadding)
                    .semantics { contentDescription = context.getString(item.titleResId) },
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
                        tint = if (selected) MaterialTheme.colorScheme.tertiary else LocalContentColor.current,
                    )
                },
                label = {
                    // Always show label to maintain the vertical position
                    Text(
                        text = stringResource(id = item.titleResId).replaceFirstChar {
                            if (it.isLowerCase()) {
                                it.titlecase(locale = Locale.ENGLISH)
                            } else {
                                it.toString()
                            }
                        },
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
            )
        }

        Spacer(Modifier.weight(1f))
    }
}

@PreviewLightDark
@Composable
private fun NavigationRailPreview() {
    DAZNCodeChallengeTheme {
        Surface {
            AppNavigationRail(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(0.dp),
                navController = rememberNavController(),
                onCurrentRouteSecondTapped = {},
            )
        }
    }
}
