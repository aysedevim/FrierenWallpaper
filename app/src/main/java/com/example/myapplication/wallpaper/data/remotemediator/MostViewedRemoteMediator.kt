package com.example.myapplication.wallpaper.data.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.wallpaper.data.local.WallpaperDatabase
import com.example.myapplication.wallpaper.data.local.entity.MostViewedEntity
import com.example.myapplication.wallpaper.data.local.entity.RemoteKeyEntity
import com.example.myapplication.wallpaper.data.mapper.toMostViewedEntity
import com.example.myapplication.wallpaper.data.remote.WallpaperApi
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalPagingApi::class)
class MostViewedRemoteMediator(
    private val api: WallpaperApi,
    private val db: WallpaperDatabase,
    private val indexKey: String
) : RemoteMediator<Int, MostViewedEntity>() {

    private val label = "most_viewed:$indexKey"
    private val dao = db.mostViewedDao()
    private val keyDao = db.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val key = keyDao.get(label) ?: return InitializeAction.LAUNCH_INITIAL_REFRESH
        val today = LocalDate.now(ZoneId.systemDefault())
        val savedDay = Instant.ofEpochMilli(key.lastUpdated)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        return if (today.isAfter(savedDay)) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MostViewedEntity>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val key = keyDao.get(label)
                key?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return try {
            val response = api.getMostViewed(
                index = indexKey,
                limit = state.config.pageSize,
                page = page
            )
            val endReached = response.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearByIndex(indexKey)
                    keyDao.delete(label)
                }
                val startPos = page * state.config.pageSize
                val entities = response.mapIndexed { i, dto ->
                    dto.toMostViewedEntity(position = startPos + i)
                }
                dao.insertAll(entities)
                keyDao.upsert(
                    RemoteKeyEntity(
                        label = label,
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (endReached) null else page + 1,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            }
            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
