package com.example.myapplication.wallpaper.domain.repository

import androidx.paging.PagingData
import com.example.myapplication.wallpaper.domain.model.Resource
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import kotlinx.coroutines.flow.Flow


interface WallpaperRepository {
        suspend fun getImages(index: String, limit: Int, page: Int): Resource<List<Wallpaper>>
        fun getMostViewedPaged(index: String, pageSize: Int): Flow<PagingData<Wallpaper>>
        fun getMostFavoritedPaged(index: String, pageSize: Int): Flow<PagingData<Wallpaper>>
        suspend fun getImageDetail(imageId: String): Resource<Wallpaper>
        suspend fun getBanner(index: String): Resource<Wallpaper>

}
