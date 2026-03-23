package com.example.myapplication.wallpaper.data.repository

import com.example.myapplication.wallpaper.data.shared.FavoriteShared
import com.example.myapplication.wallpaper.domain.repository.FavoriteRepository


class FavoriteRepositoryImpl(
    private val favoriteShared: FavoriteShared
) : FavoriteRepository {

    override fun getFavorites(): List<String> {
        return favoriteShared.getFavorites()
    }

    override fun toggleFavorite(imageId: String): List<String> {
        return favoriteShared.toggleFavorite(imageId)
    }
}