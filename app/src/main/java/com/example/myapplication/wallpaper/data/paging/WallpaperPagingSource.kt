package com.example.myapplication.wallpaper.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.wallpaper.domain.model.Resource
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.repository.WallpaperRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WallpaperPagingSource(
    private val repository: WallpaperRepository,
    private val query: Query
) : PagingSource<Int, Wallpaper>() {

    data class Query(
        val index: String,
    )

    companion object {
        const val STARTING_PAGE_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wallpaper> {
        return withContext(Dispatchers.IO) {
            val page = params.key ?: STARTING_PAGE_INDEX
            val size = params.loadSize

            when (val result = repository.getImages(
                index = query.index,
                limit = size,
                page = page
            )) {
                is Resource.Success -> {
                    val data = result.data
                    val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                    val nextKey = if (data.isEmpty()) null else page + 1

                    if (params.placeholdersEnabled) {
                        val itemsBefore = page * size
                        val itemsAfter = itemsBefore + data.size
                        LoadResult.Page(
                            data = data,
                            prevKey = prevKey,
                            nextKey = nextKey,
                            itemsAfter = if (itemsAfter > size) size else itemsAfter,
                            itemsBefore = if (page == STARTING_PAGE_INDEX) 0 else itemsBefore,
                        )
                    } else {
                        LoadResult.Page(
                            data = data,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    }
                }
                is Resource.Error -> LoadResult.Error(Exception(result.error.toString()))
                Resource.Loading -> LoadResult.Error(IllegalStateException("Unexpected Loading"))
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