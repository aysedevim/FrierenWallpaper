package com.example.myapplication.wallpaper.domain.repository

import android.content.Context
import com.example.myapplication.wallpaper.domain.model.Wallpaper

interface WallpaperSetter {
    suspend fun setWallpaper(
        context: Context,
        wallpaper: Wallpaper,
        destination: Int
    ): Result<Unit>
}