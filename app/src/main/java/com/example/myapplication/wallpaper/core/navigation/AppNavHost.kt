package com.example.myapplication.wallpaper.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.wallpaper.core.routes.AppRoute
import com.example.myapplication.wallpaper.screens.BrowseScreen
import com.example.myapplication.wallpaper.screens.DetailScreen
import com.example.myapplication.wallpaper.screens.FavoritesScreen
import com.example.myapplication.wallpaper.screens.HomeScreen
import com.example.myapplication.wallpaper.viewmodel.WallpaperViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: WallpaperViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Home.route,
        modifier = modifier
    ) {
        composable(AppRoute.Home.route) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }

        composable(AppRoute.Browse.route) {
            BrowseScreen(navController = navController, viewModel = viewModel)
        }

        composable(AppRoute.Favorites.route) {
            FavoritesScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            AppRoute.Detail.route,
            arguments = listOf(navArgument("wallpaperId") { defaultValue = "" })
        ) { backStackEntry ->
            val wallpaperId = backStackEntry.arguments?.getString("wallpaperId") ?: ""
            DetailScreen(
                navController = navController,
                wallpaperId = wallpaperId,
                viewModel = viewModel
            )
        }
    }
}