package com.example.myapplication.wallpaper.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myapplication.wallpaper.data.paging.MostFavoritedPagingSource
import com.example.myapplication.wallpaper.data.paging.MostViewedPagingSource
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow

private const val LIST_ITEM_LOAD_MORE_THRESHOLD = 2

class GetMostViewedUseCase(
    private val repository: WallpaperRepository
) {
    data class Input(
        val index: String = "frieren",
        val itemPerPage: Int = 10
    )

    operator fun invoke(input: Input): Flow<PagingData<Wallpaper>> {
        return Pager(
            config = PagingConfig(
                pageSize = input.itemPerPage,
                enablePlaceholders = false,
                prefetchDistance = LIST_ITEM_LOAD_MORE_THRESHOLD,
                initialLoadSize = input.itemPerPage
            )
        ) {
            MostViewedPagingSource(
                repository = repository,
                index = input.index
            )
        }.flow
    }
}
