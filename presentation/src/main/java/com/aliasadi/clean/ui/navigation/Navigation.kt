package com.aliasadi.clean.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aliasadi.clean.ui.favorites.FavoritesPage
import com.aliasadi.clean.ui.favorites.FavoritesViewModel
import com.aliasadi.clean.ui.feed.FeedPage
import com.aliasadi.clean.ui.feed.FeedViewModel

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.FeedScreen.route) {
        composable(route = Screen.FeedScreen.route) {
            val viewModel = hiltViewModel<FeedViewModel>()
            FeedPage(
                viewModel = viewModel,
                loadStateListener = viewModel::onLoadStateUpdate,
                onMovieClick = viewModel::onMovieClicked
            )
        }
        composable(route = Screen.FavoritesScreen.route) {
            val viewModel = hiltViewModel<FavoritesViewModel>()
            FavoritesPage(
                viewModel = viewModel,
                loadStateListener = viewModel::onLoadStateUpdate,
                onMovieClick = viewModel::onMovieClicked
            )
        }
    }
}

sealed class Screen(val route: String) {
    object FeedScreen : Screen("feed_screen")
    object FavoritesScreen : Screen("favorites_screen")
}