package com.example.myapplication.wallpaper.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.usecase.GetBannerUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetMostFavoritedUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetMostViewedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getMostViewedUseCase: GetMostViewedUseCase,
    private val getMostFavoritedUseCase: GetMostFavoritedUseCase,
    private val getBannerUseCase: GetBannerUseCase
) : ViewModel() {

    val errorMessage = mutableStateOf<String?>(null)
    private val _mostViewedWallpapers = MutableStateFlow<PagingData<Wallpaper>>(PagingData.empty())
    val mostViewedWallpapers: StateFlow<PagingData<Wallpaper>> = _mostViewedWallpapers.asStateFlow()

    private val _mostFavoritedWallpapers = MutableStateFlow<PagingData<Wallpaper>>(PagingData.empty())
    val mostFavoritedWallpapers: StateFlow<PagingData<Wallpaper>> = _mostFavoritedWallpapers.asStateFlow()

    private val _bannerImage = MutableStateFlow<Wallpaper?>(null)
    val bannerImage: StateFlow<Wallpaper?> = _bannerImage.asStateFlow()

    fun loadBanner(index: String = "frieren") {
        viewModelScope.launch {
            try {
                _bannerImage.value = getBannerUseCase(index)
            } catch (e: Exception) {
                errorMessage.value=("Banner load error: ${e.message}")
            }
        }
    }

    fun getMostViewed(index: String = "frieren") {
        viewModelScope.launch {
            try {
                getMostViewedUseCase(
                    GetMostViewedUseCase.Input(index = index, itemPerPage = 10)
                )
                    .cachedIn(viewModelScope)
                    .collect { _mostViewedWallpapers.value = it }
            } catch (e: Exception) {
                errorMessage.value=("Most viewed error: ${e.message}")
            }
        }
    }

    fun getMostFavorited(index: String = "frieren") {
        viewModelScope.launch {
            try {
                getMostFavoritedUseCase(
                    GetMostFavoritedUseCase.Input(index = index, itemPerPage = 10)
                )
                    .cachedIn(viewModelScope)
                    .collect { _mostFavoritedWallpapers.value = it }
            } catch (e: Exception) {
                errorMessage.value=("Most favorited error: ${e.message}")
            }
        }
    }
}