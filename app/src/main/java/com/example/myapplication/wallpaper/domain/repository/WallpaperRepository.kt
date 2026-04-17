package com.example.myapplication.wallpaper.domain.repository

import com.example.myapplication.wallpaper.domain.model.Resource
import com.example.myapplication.wallpaper.domain.model.Wallpaper


interface WallpaperRepository {
        suspend fun getImages(index: String, limit: Int, page: Int): Resource<List<Wallpaper>>
        suspend fun getMostViewed(index: String, limit: Int, page: Int): List<Wallpaper>
        suspend fun getMostFavorited(index: String, limit: Int, page: Int): List<Wallpaper>
        suspend fun getImageDetail(imageId: String): Resource<Wallpaper>
        suspend fun getBanner(index: String): Resource<Wallpaper>

}
