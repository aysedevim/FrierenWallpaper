package com.example.myapplication.wallpaper.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.wallpaper.core.routes.AppRoute
import com.example.myapplication.wallpaper.domain.model.BottomNavItem

class Constants {
    val BottomNavItems = listOf(
        BottomNavItem(
            titleResId = R.string.home,
            icon = Icons.Filled.Home,
            route = AppRoute.Home.route
        ),
        BottomNavItem(
            titleResId = R.string.browse,
            icon = Icons.Filled.Explore,
            route = AppRoute.Browse.route
        ),
        BottomNavItem(
            titleResId = R.string.favorites,
            icon = Icons.Filled.Favorite,
            route = AppRoute.Favorites.route
        )
    )
}