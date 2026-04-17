package com.example.myapplication.wallpaper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.model.AppError
import com.example.myapplication.wallpaper.domain.model.Resource
import com.example.myapplication.wallpaper.domain.model.WallpaperDestination
import com.example.myapplication.wallpaper.domain.usecase.GetImageDetailUseCase
import com.example.myapplication.wallpaper.domain.usecase.SetWallpaperUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailScreenState(
    val wallpaper: Wallpaper? = null,
    val isLoading: Boolean = false,
    val error: AppError? = null
)
@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val getImageDetailUseCase: GetImageDetailUseCase,
    private val setWallpaperUseCase: SetWallpaperUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailScreenState())
    val state: StateFlow<DetailScreenState> = _state.asStateFlow()

    fun loadImageDetail(imageId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = getImageDetailUseCase(imageId)) {
                is Resource.Success -> _state.value = _state.value.copy(
                    wallpaper = result.data, isLoading = false
                )
                is Resource.Error -> _state.value = _state.value.copy(
                    error = result.error, isLoading = false
                )
                Resource.Loading -> Unit
            }
        }
    }
    fun setWallpaper(
        destination: WallpaperDestination,
        onResult: (Result<Unit>) -> Unit
    ) {

        viewModelScope.launch {

            val currentWallpaper = _state.value.wallpaper

            if (currentWallpaper == null) {

                onResult(
                    Result.failure(
                        Exception("Wallpaper not loaded")
                    )
                )

                return@launch
            }

            when (val resource = setWallpaperUseCase(currentWallpaper, destination)) {
                is Resource.Success -> onResult(Result.success(Unit))
                is Resource.Error -> onResult(Result.failure(Exception(resource.error.toString())))
                Resource.Loading -> Unit
            }
        }
    }
}