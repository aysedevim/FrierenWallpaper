package com.example.myapplication.wallpaper.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.wallpaper.core.routes.AppRoute
import com.example.myapplication.wallpaper.presentation.screens.BrowseScreen
import com.example.myapplication.wallpaper.presentation.screens.FavoritesScreen
import com.example.myapplication.wallpaper.presentation.screens.HomeScreen
import com.example.myapplication.wallpaper.presentation.screens.DetailScreen
import com.example.myapplication.wallpaper.presentation.viewmodel.BrowseScreenViewModel
import com.example.myapplication.wallpaper.presentation.viewmodel.DetailScreenViewModel
import com.example.myapplication.wallpaper.presentation.viewmodel.FavoritesViewModel
import com.example.myapplication.wallpaper.presentation.viewmodel.HomeScreenViewModel
import com.example.myapplication.wallpaper.presentation.viewmodel.PagingViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    favoriteViewModel: FavoritesViewModel,
    browseViewModel: BrowseScreenViewModel,
    homeViewModel: HomeScreenViewModel,
    detailViewModel: DetailScreenViewModel,
    pagingViewModel: PagingViewModel,
    modifier: Modifier = Modifier,
    startDestination: String = AppRoute.Home.route

) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(AppRoute.Home.route) {
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel,
            )
        }

        composable(AppRoute.Browse.route) {
            BrowseScreen(
                navController = navController,
                browseViewModel = browseViewModel,
                favoriteViewModel = favoriteViewModel
            )  }

        composable(AppRoute.Favorites.route) {
            FavoritesScreen(
                navController = navController,
                favoriteViewModel = favoriteViewModel,
                pagingViewModel = pagingViewModel
            )  }

        composable(
            AppRoute.Detail.route,
            arguments = listOf(navArgument("wallpaperId") { defaultValue = "" })
        ) { backStackEntry ->
            val wallpaperId = backStackEntry.arguments?.getString("wallpaperId") ?: ""
            DetailScreen(
                navController = navController,
                wallpaperId = wallpaperId,
                detailViewModel = detailViewModel,
                favoriteViewModel = favoriteViewModel
            )
        }
    }
}