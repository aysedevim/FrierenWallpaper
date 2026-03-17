package com.example.myapplication.wallpaper.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myapplication.wallpaper.data.paging.WallpaperPagingSource
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow

private const val LIST_ITEM_LOAD_MORE_THRESHOLD = 2

class GetWallpapersPagingUseCase(
    private val repository: WallpaperRepository
) {
    data class Input(
        val index: String,
        val category: String? = null,
        val itemPerPage: Int = 20
    )

    fun execute(input: Input): Flow<PagingData<Wallpaper>> {
        return with(input) {
            Pager(
                config = PagingConfig(
                    pageSize = itemPerPage,
                    enablePlaceholders = false,
                    prefetchDistance = LIST_ITEM_LOAD_MORE_THRESHOLD,
                    initialLoadSize = itemPerPage
                ),
                pagingSourceFactory = {
                    WallpaperPagingSource(
                        repository = repository,
                        query = WallpaperPagingSource.Query(
                            index = index,
                        )
                    )
                }
            ).flow
        }
    }
}