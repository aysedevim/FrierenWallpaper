package com.example.myapplication.wallpaper.domain.usecase

import com.example.myapplication.wallpaper.domain.repository.FavoriteRepository

class ToggleFavoriteUseCase(
    private val repository: FavoriteRepository
) {
    operator fun invoke(imageId: String): List<String> = repository.toggleFavorite(imageId)
}