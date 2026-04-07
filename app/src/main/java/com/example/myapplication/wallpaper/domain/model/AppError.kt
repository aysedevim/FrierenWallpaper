package com.example.myapplication.wallpaper.domain.model

sealed class AppError {
    object Timeout : AppError()
    object Network : AppError()
    object NotFound : AppError()
    data class Unknown(val message: String?) : AppError()
}