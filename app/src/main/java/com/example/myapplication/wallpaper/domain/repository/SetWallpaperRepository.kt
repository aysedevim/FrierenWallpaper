package com.example.myapplication.wallpaper.domain.repository

import com.example.myapplication.wallpaper.domain.model.Resource
import com.example.myapplication.wallpaper.domain.model.WallpaperDestination

interface SetWallpaperRepository {
        suspend fun setWallpaper(
            imageUrl: String,
            destination: WallpaperDestination
        ): Resource<Unit>
}
