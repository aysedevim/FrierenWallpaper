package com.example.myapplication.wallpaper.domain.model

import android.app.WallpaperManager


enum class WallpaperDestination(val flag: Int) {
    HOME(WallpaperManager.FLAG_SYSTEM),
    LOCK(WallpaperManager.FLAG_LOCK),
    BOTH(WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK);
}
