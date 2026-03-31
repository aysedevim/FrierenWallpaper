package com.example.myapplication.wallpaper.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import android.content.Context
import com.example.myapplication.wallpaper.domain.usecase.GetImageDetailUseCase
import com.example.myapplication.wallpaper.domain.usecase.SetWallpaperUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val getImageDetailUseCase: GetImageDetailUseCase,
    private val setWallpaperUseCase: SetWallpaperUseCase,
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

    fun setWallpaper(
        destination: Int,
        onResult: (Result<Unit>) -> Unit
    ) {

        viewModelScope.launch {

            val currentWallpaper = _wallpaper.value

            if (currentWallpaper == null) {

                onResult(
                    Result.failure(
                        Exception("Wallpaper not loaded")
                    )
                )

                return@launch
            }

            val result =
                setWallpaperUseCase(
                    currentWallpaper,
                    destination
                )

            onResult(result)
        }
    }
}