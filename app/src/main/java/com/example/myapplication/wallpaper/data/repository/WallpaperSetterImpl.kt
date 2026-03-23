package com.example.myapplication.wallpaper.data.repository

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.myapplication.wallpaper.domain.repository.WallpaperSetter
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class WallpaperSetterImpl : WallpaperSetter {

    override suspend fun setWallpaper(
        context: Context,
        wallpaper: Wallpaper,
        destination: Int
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val bitmap = loadBitmapFromUrl(wallpaper.image_url)
                    ?: return@withContext Result.failure(Exception("Bitmap yüklenemedi"))

                val wallpaperManager = WallpaperManager.getInstance(context)

                wallpaperManager.setBitmap(
                    bitmap,
                    null,
                    true,
                    destination
                )

                Result.success(Unit)
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun loadBitmapFromUrl(url: String): Bitmap? {
        return try {
            val connection = URL(url).openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }
}