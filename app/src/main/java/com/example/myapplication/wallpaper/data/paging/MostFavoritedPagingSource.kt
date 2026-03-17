package com.example.myapplication.wallpaper.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.repository.WallpaperRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MostFavoritedPagingSource (
    private val repository: WallpaperRepository,
    private val index: String
) : PagingSource<Int, Wallpaper>() {

    companion object {
        const val STARTING_PAGE_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wallpaper> {
        return withContext(Dispatchers.IO) {
            try {
                val page = params.key ?: STARTING_PAGE_INDEX
                val size = params.loadSize

                val response = repository.getMostFavorited(
                    index = index,
                    limit = size,
                    page = page
                )

                LoadResult.Page(
                    data = response,
                    prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if (response.isEmpty()) null else page + 1
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Wallpaper>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}