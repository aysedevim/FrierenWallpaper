package com.example.myapplication.wallpaper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.usecase.GetFavoritesUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetWallpapersPagingUseCase
import com.example.myapplication.wallpaper.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Collections.emptyList

class FavoritesViewModel (
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
   ) : ViewModel() {
    val favoriteIds = MutableStateFlow<List<String>>(emptyList())

    init {
        loadFavorites()
    }
    private fun loadFavorites() {
        favoriteIds.value = getFavoritesUseCase()
    }

    fun toggleFavorite(imageId: String) = viewModelScope.launch {
        favoriteIds.value = toggleFavoriteUseCase(imageId)
    }

}