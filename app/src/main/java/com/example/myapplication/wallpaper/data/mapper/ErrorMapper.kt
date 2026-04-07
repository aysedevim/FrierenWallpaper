package com.example.myapplication.wallpaper.data.mapper

import com.example.myapplication.wallpaper.domain.model.AppError
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toAppError(): AppError {
    return when (this) {
        is SocketTimeoutException -> AppError.Timeout
        is HttpException -> {
            when (code()) {
                408 -> AppError.Timeout
                404 -> AppError.NotFound
                else -> AppError.Unknown(message())
            }
        }
        is UnknownHostException -> AppError.Network
        else -> AppError.Unknown(message)
    }
}
