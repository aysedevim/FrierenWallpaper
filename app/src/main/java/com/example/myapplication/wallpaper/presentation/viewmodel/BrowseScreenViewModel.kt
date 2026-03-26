package com.example.myapplication.wallpaper.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.usecase.GetWallpapersPagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BrowseScreenViewModel @Inject constructor(
    private val getWallpapersPagingUseCase: GetWallpapersPagingUseCase
) : ViewModel() {

    val errorMessage = mutableStateOf<String?>(null)
    private val _wallpapersPagination = MutableStateFlow<PagingData<Wallpaper>>(PagingData.empty())
    val wallpapersPagination: StateFlow<PagingData<Wallpaper>> = _wallpapersPagination.asStateFlow()

    private var currentIndex = "frieren"

    fun getWallpapers(index: String = currentIndex) {
        currentIndex = index
        viewModelScope.launch {
            try {
                getWallpapersPagingUseCase.execute(
                    GetWallpapersPagingUseCase.Input(index = index, itemPerPage = 20)
                )
                    .cachedIn(viewModelScope)
                    .collect { _wallpapersPagination.value = it }
            } catch (e: Exception) {
                errorMessage.value=("Paging error: ${e.message}")
            }
        }
    }
}