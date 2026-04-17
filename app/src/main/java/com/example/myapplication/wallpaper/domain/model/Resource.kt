package com.example.myapplication.wallpaper.domain.model

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()

    data class Error(val error: AppError) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}