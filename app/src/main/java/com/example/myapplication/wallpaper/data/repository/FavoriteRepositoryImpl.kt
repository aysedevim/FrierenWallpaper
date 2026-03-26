package com.example.myapplication.wallpaper.data.repository

import com.example.myapplication.wallpaper.data.shared.FavoriteShared
import com.example.myapplication.wallpaper.domain.repository.FavoriteRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteShared: FavoriteShared
) : FavoriteRepository {

    override fun getFavorites(): List<String> {
        return favoriteShared.getFavorites()
    }

    override fun toggleFavorite(imageId: String): List<String> {
        return favoriteShared.toggleFavorite(imageId)
    }
}