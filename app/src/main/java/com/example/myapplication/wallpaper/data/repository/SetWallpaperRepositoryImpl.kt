package com.example.myapplication.wallpaper.data.repository

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.myapplication.wallpaper.domain.repository.SetWallpaperRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

class SetWallpaperRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SetWallpaperRepository {

    override suspend fun setWallpaper(
        imageUrl: String,
        destination: Int
    ): Result<Unit> = withContext(Dispatchers.IO) {

        try {

            val bitmap =
                loadBitmapFromUrl(imageUrl)
                    ?: return@withContext Result.failure(
                        Exception("Bitmap yuklenemedi")
                    )

            WallpaperManager
                .getInstance(context)
                .setBitmap(
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

    private fun loadBitmapFromUrl(
        url: String
    ): Bitmap? {

        return try {

            val connection =
                URL(url).openConnection()

            connection.connect()

            val inputStream =
                connection.getInputStream()

            BitmapFactory.decodeStream(inputStream)

        } catch (e: Exception) {

            null
        }
    }
}