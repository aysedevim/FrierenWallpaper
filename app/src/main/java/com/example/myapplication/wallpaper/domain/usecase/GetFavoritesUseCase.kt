package com.example.myapplication.wallpaper.domain.usecase

import com.example.myapplication.wallpaper.domain.repository.FavoriteRepository

class GetFavoritesUseCase(
    private val repository: FavoriteRepository
) {
    operator fun invoke(): List<String> = repository.getFavorites()
}