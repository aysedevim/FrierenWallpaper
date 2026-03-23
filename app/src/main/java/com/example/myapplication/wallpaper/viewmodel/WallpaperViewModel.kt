package com.example.myapplication.wallpaper.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.usecase.GetBannerUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetFavoritesUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetImageDetailUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetMostFavoritedUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetMostViewedUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetWallpapersPagingUseCase
import com.example.myapplication.wallpaper.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class WallpaperViewModel(
    private val getWallpapersPagingUseCase: GetWallpapersPagingUseCase,
    private val getMostViewedUseCase: GetMostViewedUseCase,
    private val getMostFavoritedUseCase: GetMostFavoritedUseCase,
    private val getBannerUseCase: GetBannerUseCase,
    private val getImageDetailUseCase: GetImageDetailUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val wallpapersPagination = MutableStateFlow<PagingData<Wallpaper>>(PagingData.empty())
    val mostViewedWallpapers = MutableStateFlow<PagingData<Wallpaper>>(PagingData.empty())
    val mostFavoritedWallpapers = MutableStateFlow<PagingData<Wallpaper>>(PagingData.empty())

    val bannerImage = mutableStateOf<Wallpaper?>(null)
    val favoriteIds = MutableStateFlow<List<String>>(emptyList())
    val errorMessage = mutableStateOf<String?>(null)

    init {
        loadFavorites()
        getWallpapers()
        getMostViewed()
        getMostFavorited()
        loadBanner()
    }

    private fun loadFavorites() {
        favoriteIds.value = getFavoritesUseCase()
    }

    fun getWallpapers(index: String = "frieren") = launchPaging {
        getWallpapersPagingUseCase.execute(
            GetWallpapersPagingUseCase.Input(index = index, itemPerPage = 20)
        )
            .cachedIn(viewModelScope)
            .collect { wallpapersPagination.value = it }
    }

    fun getMostViewed(index: String = "frieren") = launchPaging {
        getMostViewedUseCase(
            GetMostViewedUseCase.Input(index = index, itemPerPage = 10)
        )
            .cachedIn(viewModelScope)
            .collect { mostViewedWallpapers.value = it }
    }

    fun getMostFavorited(index: String = "frieren") = launchPaging {
        getMostFavoritedUseCase(
            GetMostFavoritedUseCase.Input(index = index, itemPerPage = 10)
        )
            .cachedIn(viewModelScope)
            .collect { mostFavoritedWallpapers.value = it }
    }

    fun loadBanner(index: String = "frieren") = viewModelScope.launch {
        try {
            bannerImage.value = getBannerUseCase(index)
        } catch (e: Exception) {
            errorMessage.value = "Banner load error: ${e.message}"
        }
    }

    fun loadImageDetail(imageId: String, onResult: (Wallpaper?) -> Unit = {}) = viewModelScope.launch {
        try {
            onResult(getImageDetailUseCase(imageId))
        } catch (e: Exception) {
            errorMessage.value = "Detail load error: ${e.message}"
            onResult(null)
        }
    }

    fun toggleFavorite(imageId: String) = viewModelScope.launch {
        favoriteIds.value = toggleFavoriteUseCase(imageId)
    }

    private fun launchPaging(block: suspend () -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Exception) {
            errorMessage.value = "Paging error: ${e.message}"
        }
    }
}