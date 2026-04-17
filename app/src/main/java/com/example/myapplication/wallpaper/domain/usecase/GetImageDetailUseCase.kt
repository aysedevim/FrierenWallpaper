package com.example.myapplication.wallpaper.domain.usecase

import com.example.myapplication.wallpaper.domain.model.Resource
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.repository.WallpaperRepository

class GetImageDetailUseCase(
    private val repository: WallpaperRepository
) {
    suspend operator fun invoke(imageId: String): Resource<Wallpaper> {
        return repository.getImageDetail(imageId)
    }
}