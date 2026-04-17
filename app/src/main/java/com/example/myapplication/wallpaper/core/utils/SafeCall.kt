package com.example.myapplication.wallpaper.core.utils
import com.example.myapplication.wallpaper.data.mapper.toAppError
import com.example.myapplication.wallpaper.domain.model.Resource
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> safeCall(
    crossinline block: suspend () -> T
): Resource<T> = try {
    Resource.Success(block())
} catch (e: CancellationException) {
    throw e                                
} catch (e: Exception) {
    Resource.Error(e.toAppError())
}