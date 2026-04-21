package com.example.myapplication.wallpaper.domain.usecase

import androidx.paging.PagingData
import com.example.myapplication.wallpaper.core.constants.AppIndex
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow

class GetMostFavoritedUseCase(
    private val repository: WallpaperRepository
) {
    data class Input(
        val index: String = AppIndex.FRIEREN,
        val itemPerPage: Int = 10
    )

    operator fun invoke(input: Input): Flow<PagingData<Wallpaper>> =
        repository.getMostFavoritedPaged(input.index, input.itemPerPage)
}
