package com.example.myapplication.wallpaper.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

class SetWallpaperUseCase @Inject constructor() {

    suspend operator fun invoke(
        context: Context,
        wallpaper: Wallpaper,
        destination: Int
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val bitmap = loadBitmapFromUrl(wallpaper.image_url)
                ?: return@withContext Result.failure(Exception("Bitmap yüklenemedi"))

            android.app.WallpaperManager.getInstance(context).setBitmap(
                bitmap,
                null,
                true,
                destination
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
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