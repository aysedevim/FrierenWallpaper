package com.example.myapplication.wallpaper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.wallpaper.core.constants.AppIndex
import com.example.myapplication.wallpaper.data.mapper.toAppError
import com.example.myapplication.wallpaper.domain.model.AppError
import com.example.myapplication.wallpaper.domain.model.Resource
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


data class HomeScreenState(
    val bannerImage: Wallpaper? = null,
    val isBannerLoading: Boolean = false,
    val bannerError: AppError? = null
)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getMostViewedUseCase: GetMostViewedUseCase,
    private val getMostFavoritedUseCase: GetMostFavoritedUseCase,
    private val getBannerUseCase: GetBannerUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()
    private val _mostViewedWallpapers =
        MutableStateFlow<PagingData<Wallpaper>>(PagingData.empty())
    val mostViewedWallpapers = _mostViewedWallpapers.asStateFlow()

    private val _mostFavoritedWallpapers =
        MutableStateFlow<PagingData<Wallpaper>>(PagingData.empty())
    val mostFavoritedWallpapers = _mostFavoritedWallpapers.asStateFlow()

    fun loadBanner(index: String = AppIndex.FRIEREN) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isBannerLoading = true, bannerError = null)

            when (val result = getBannerUseCase(index)) {
                is Resource.Success -> _state.value = _state.value.copy(
                    bannerImage = result.data,
                    isBannerLoading = false
                )
                is Resource.Error -> _state.value = _state.value.copy(
                    bannerError = result.error,
                    isBannerLoading = false
                )
                Resource.Loading -> Unit
            }
        }
    }


    fun getMostViewed(index: String = AppIndex.FRIEREN) {
        viewModelScope.launch {
            getMostViewedUseCase(
                GetMostViewedUseCase.Input(index = index, itemPerPage = 10)
            )
                .cachedIn(viewModelScope)
                .collect { _mostViewedWallpapers.value = it }
        }
    }

    fun getMostFavorited(index: String = AppIndex.FRIEREN) {
        viewModelScope.launch {
            getMostFavoritedUseCase(
                GetMostFavoritedUseCase.Input(index = index, itemPerPage = 10)
            )
                .cachedIn(viewModelScope)
                .collect { _mostFavoritedWallpapers.value = it }
        }
    }
}