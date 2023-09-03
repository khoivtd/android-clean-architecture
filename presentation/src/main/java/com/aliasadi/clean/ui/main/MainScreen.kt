package com.aliasadi.clean.ui.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.aliasadi.clean.ui.navigation.MainGraph
import com.aliasadi.clean.ui.navigation.Screen
import com.aliasadi.clean.ui.widget.BottomNavigationBar
import com.aliasadi.clean.ui.widget.BottomNavigationBarItem
import com.aliasadi.clean.ui.widget.DefaultDivider
import com.aliasadi.clean.util.preview.PreviewContainer

@Composable
fun MainScreen(
    mainRouter: MainRouter,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit
) {
    val navController = rememberNavController()
    var appBarTitle by remember { mutableStateOf("Feed") }

    Scaffold(
        topBar = {
            TopBar(
                appBarTitle,
                darkMode,
                onThemeUpdated,
                onSearchClick = {
                    mainRouter.navigateToSearch()
                }
            )

        },
        bottomBar = {
            BottomNavigationBar(
                items = getBottomNavigationItems(),
                navController = navController,
                onItemClick = { bottomItem ->
                    val currentRoute = navController.currentDestination?.route
                    if (currentRoute != bottomItem.route) {
                        appBarTitle = bottomItem.tabName
                        navController.navigate(bottomItem.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.findStartDestination().id)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize(1f)
                .padding(paddingValues)
        ) {
            MainGraph(navController, mainRouter)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit,
    onSearchClick: () -> Unit
) {
    Column {
        TopAppBar(
            title = { Text(text = title) },
            actions = {
                IconButton(
                    onClick = { onSearchClick() }
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(
                    onClick = { onThemeUpdated() }
                ) {
                    val icon = if (darkMode) {
                        Icons.Filled.DarkMode
                    } else {
                        Icons.Outlined.DarkMode
                    }
                    Icon(imageVector = icon, contentDescription = "Dark Mode")
                }
            }
        )
        DefaultDivider()
    }
}

fun getBottomNavigationItems() = listOf(
    BottomNavigationBarItem("Feed", imageVector = Icons.Default.DynamicFeed, Screen.FeedScreen.route),
    BottomNavigationBarItem("Favorites", imageVector = Icons.Default.FavoriteBorder, Screen.FavoritesScreen.route)
)

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {
    PreviewContainer {
        Column {
            TopBar("Feed", true, {}) {}
            Spacer(modifier = Modifier.padding(10.dp))
            TopBar("Feed", false, {}) {}
        }
//        MainScreen(rememberNavController(), true) {}
    }
}