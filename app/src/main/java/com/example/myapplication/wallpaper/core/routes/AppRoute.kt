package com.example.myapplication.wallpaper.core.routes

sealed class AppRoute(val route: String) {
    object Home : AppRoute("home")
    object Browse : AppRoute("browse")
    object Favorites : AppRoute("favorites")

    object Detail : AppRoute("detail/{wallpaperId}") {
        fun createRoute(wallpaperId: String) = "detail/$wallpaperId"


        fun withId(wallpaperId: String): String = createRoute(wallpaperId)
    }

    companion object {
        val items = listOf(Home, Browse, Favorites)
    }
}