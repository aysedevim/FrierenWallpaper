package com.example.myapplication.wallpaper.domain.repository

interface FavoriteRepository {
    fun getFavorites(): List<String>
    fun toggleFavorite(imageId: String): List<String>
}