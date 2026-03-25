package com.example.myapplication.wallpaper.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.usecase.GetImageDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailScreenViewModel(
    private val getImageDetailUseCase: GetImageDetailUseCase
) : ViewModel() {

    val errorMessage = mutableStateOf<String?>(null)
    private val _wallpaper = MutableStateFlow<Wallpaper?>(null)
    val wallpaper: StateFlow<Wallpaper?> = _wallpaper.asStateFlow()

    fun loadImageDetail(imageId: String) {
        viewModelScope.launch {
            try {
                _wallpaper.value = getImageDetailUseCase(imageId)
            } catch (e: Exception) {
                errorMessage.value=("Detail load error: ${e.message}")
            }
        }
    }
}