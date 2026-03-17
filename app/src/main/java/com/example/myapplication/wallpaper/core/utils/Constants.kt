package com.example.myapplication.wallpaper.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import com.example.myapplication.wallpaper.core.routes.AppRoute
import com.example.myapplication.wallpaper.domain.model.BottomNavItem

class Constants {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = AppRoute.Home.route
        ),
        BottomNavItem(
            label = "Browse",
            icon = Icons.Filled.Explore,
            route = AppRoute.Browse.route
        ),
        BottomNavItem(
            label = "Favorites",
            icon = Icons.Filled.Favorite,
            route = AppRoute.Favorites.route
        )
    )
}