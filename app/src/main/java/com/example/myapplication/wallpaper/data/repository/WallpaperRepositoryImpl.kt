package com.example.myapplication.wallpaper.data.repository

import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.repository.WallpaperRepository
import com.example.myapplication.wallpaper.data.remote.WallpaperApi


class WallpaperRepositoryImpl(
    private val api: WallpaperApi,
) : WallpaperRepository {

    override suspend fun getImages(
        index: String,
        limit: Int,
        page: Int
    ): List<Wallpaper> {
        return api.getImages(
                index = index,
                limit = limit,
                page = page
            )
    }

    override suspend fun getMostViewed(
        index: String,
        limit: Int,
        page: Int
    ): List<Wallpaper> {
        return api.getMostViewed(
                index = index,
                limit = limit,
                page = page
            )

    }

    override suspend fun getMostFavorited(
        index: String,
        limit: Int,
        page: Int
    ): List<Wallpaper> {
        return api.getMostFavorited(
                index = index,
                limit = limit,
                page = page
            )

    }

    override suspend fun getImageDetail(imageId: String): Wallpaper {
        return api.getImageDetail(imageId = imageId)

    }

    override suspend fun getBanner(index: String): Wallpaper {
        return api.getBanner(index = index)

    }


}