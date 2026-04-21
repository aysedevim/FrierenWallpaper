package com.example.myapplication.wallpaper.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.myapplication.wallpaper.core.utils.safeCall
import com.example.myapplication.wallpaper.data.local.WallpaperDatabase
import com.example.myapplication.wallpaper.data.mapper.toDomain
import com.example.myapplication.wallpaper.data.remote.WallpaperApi
import com.example.myapplication.wallpaper.data.remotemediator.MostFavoritedRemoteMediator
import com.example.myapplication.wallpaper.data.remotemediator.MostViewedRemoteMediator
import com.example.myapplication.wallpaper.domain.model.Resource
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WallpaperRepositoryImpl @Inject constructor(
    private val api: WallpaperApi,
    private val db: WallpaperDatabase,
) : WallpaperRepository {

    override suspend fun getImages(
        index: String,
        limit: Int,
        page: Int
    ): Resource<List<Wallpaper>> = safeCall {
        api.getImages(
            index = index,
            limit = limit,
            page = page
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMostViewedPaged(
        index: String,
        pageSize: Int
    ): Flow<PagingData<Wallpaper>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize,
            prefetchDistance = 2,
            enablePlaceholders = false
        ),
        remoteMediator = MostViewedRemoteMediator(api, db, index),
        pagingSourceFactory = { db.mostViewedDao().pagingSource(index) }
    ).flow.map { data -> data.map { it.toDomain() } }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMostFavoritedPaged(
        index: String,
        pageSize: Int
    ): Flow<PagingData<Wallpaper>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize,
            prefetchDistance = 2,
            enablePlaceholders = false
        ),
        remoteMediator = MostFavoritedRemoteMediator(api, db, index),
        pagingSourceFactory = { db.mostFavoritedDao().pagingSource(index) }
    ).flow.map { data -> data.map { it.toDomain() } }

    override suspend fun getImageDetail(imageId: String): Resource<Wallpaper> =
        safeCall { api.getImageDetail(imageId = imageId) }


    override suspend fun getBanner(index: String): Resource<Wallpaper> =
        safeCall { api.getBanner(index = index) }

}
