package com.example.myapplication.wallpaper.domain.repository

interface SetWallpaperRepository {
        suspend fun setWallpaper(
            imageUrl: String,
            destination: Int
        ): Result<Unit>
}
