/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.dazncodechallenge.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    onCurrentRouteSecondTapped: (item: AppNavItem) -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        tonalElevation = 0.dp,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val context = LocalContext.current

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
                        tint = if (selected) MaterialTheme.colorScheme.tertiary else LocalContentColor.current,
                    )
                },
                label = {
                    AnimatedVisibility(visible = selected) {
                        Text(
                            text = stringResource(id = item.titleResId).uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    DAZNCodeChallengeTheme {
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
