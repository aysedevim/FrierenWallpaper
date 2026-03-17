package com.example.myapplication.wallpaper.viewmodel

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.repository.WallpaperRepository
import com.example.myapplication.wallpaper.data.repository.WallpaperRepositoryImpl
import com.example.myapplication.wallpaper.data.remote.WallpaperApi
import com.example.myapplication.wallpaper.data.shared.FavoriteShared
import com.example.myapplication.wallpaper.domain.usecase.GetMostFavoritedUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetMostViewedUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetWallpapersPagingUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class WallpaperViewModel(
    private val favoriteShared: FavoriteShared
) : ViewModel() {
    val bannerImage = mutableStateOf<Wallpaper?>(null)
    val errorMessage = mutableStateOf<String?>(null)

    private val _wallpapersPagination = MutableStateFlow<PagingData<Wallpaper>>(PagingData.empty())
    val wallpapersPagination: StateFlow<PagingData<Wallpaper>> = _wallpapersPagination

    private val _mostFavoritedWallpapers = MutableStateFlow<PagingData<Wallpaper>>(PagingData.empty())

    val mostFavoritedWallpapers: StateFlow<PagingData<Wallpaper>> = _mostFavoritedWallpapers

    private val _mostViewedWallpapers = MutableStateFlow<PagingData<Wallpaper>>(PagingData.empty())

    val mostViewedWallpapers: StateFlow<PagingData<Wallpaper>> = _mostViewedWallpapers

    private val _wallpapersError = MutableStateFlow<Throwable?>(null)

    val wallpapersError: StateFlow<Throwable?> = _wallpapersError

    private val _mostViewedError = MutableStateFlow<Throwable?>(null)

    val mostViewedError: StateFlow<Throwable?> = _mostViewedError

    private val _mostFavoritedError = MutableStateFlow<Throwable?>(null)

    val mostFavoritedError: StateFlow<Throwable?> = _mostFavoritedError

    private val limit = 10
    private val _favoriteIds = MutableStateFlow<List<String>>(emptyList())
    val favoriteIds: StateFlow<List<String>> = _favoriteIds

    private val BASE_URL = "https://wallpaper-backend.applixus.com/"


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("X-API-Key", "applixus12345")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val api: WallpaperApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WallpaperApi::class.java)
    }

    private val repository: WallpaperRepository = WallpaperRepositoryImpl(api)
    private val getWallpapersPagingUseCase = GetWallpapersPagingUseCase(repository)
    private val getMostViewedUseCase = GetMostViewedUseCase(repository)
    private val getMostFavoritedUseCase = GetMostFavoritedUseCase(repository)

    init {
        _favoriteIds.value = favoriteShared.getFavorites()
        getWallpapers()
        getMostViewed()
        getMostFavorited()
        loadBanner()
    }

    fun getWallpapers(index: String = "frieren", category: String? = null) {
        viewModelScope.launch {
            getWallpapersPagingUseCase.execute(
                GetWallpapersPagingUseCase.Input(
                    index = index,
                    category = category,
                    itemPerPage = 20
                )
            ).cachedIn(viewModelScope)
                .collect { pagingData ->
                    _wallpapersPagination.value = pagingData
                }
        }
    }

    fun getMostFavorited(index: String = "frieren") {
        viewModelScope.launch {
            getMostFavoritedUseCase(
                GetMostFavoritedUseCase.Input(
                    index = index,
                    itemPerPage = 10
                )
            ).cachedIn(viewModelScope)
                .catch { e ->
                    _mostFavoritedError.emit(e)
                }
                .collect { pagingData ->
                    _mostFavoritedWallpapers.value = pagingData
                }
        }
    }


    fun getMostViewed(index: String = "frieren") {
        viewModelScope.launch {
            getMostViewedUseCase(
                GetMostViewedUseCase.Input(
                    index = index,
                    itemPerPage = 10
                )
            ).cachedIn(viewModelScope)
                .catch { e ->
                    _mostViewedError.emit(e)
                }
                .collect { pagingData ->
                    _mostViewedWallpapers.value = pagingData
                }
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun setWallpaper(
        context: Context,
        wallpaper: Wallpaper,
        destination: Int = WallpaperManager.FLAG_SYSTEM,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val bitmap = withContext(Dispatchers.IO) {
                    loadBitmapFromUrl(wallpaper.image_url)
                }

                bitmap?.let {
                    withContext(Dispatchers.Main) {
                        try {
                            val wallpaperManager = WallpaperManager.getInstance(context)
                                wallpaperManager.setBitmap(it)

                            onResult(true, "Wallpaper set successfully!")
                        } catch (e: IOException) {
                            e.printStackTrace()
                            onResult(false, "Failed to set wallpaper: ${e.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false, "Error: ${e.message}")
            }
        }
    }

    fun setWallpaperWithOptions(
        context: Context,
        wallpaper: Wallpaper,
        option: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val destination = when (option) {
            "home" -> WallpaperManager.FLAG_SYSTEM
            "lock" -> WallpaperManager.FLAG_LOCK
            "both" -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
            else -> WallpaperManager.FLAG_SYSTEM
        }

        setWallpaper(context, wallpaper, destination, onResult)
    }

    private suspend fun loadBitmapFromUrl(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val connection = java.net.URL(url).openConnection()
                connection.connect()
                val inputStream = connection.getInputStream()
                android.graphics.BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    fun loadBanner(index: String = "frieren") {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.getBanner(index)
                }
                bannerImage.value = response
            } catch (e: Exception) {
                errorMessage.value = "Hata: ${e.message}"
            }
        }
    }


    fun loadImageDetail(imageId: String, onResult: (Wallpaper?) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getImageDetail(imageId)
                }
                onResult(result)
            } catch (e: Exception) {
                errorMessage.value = "Hata: ${e.message}"
                onResult(null)
            }
        }
    }

    fun toggleFavorite(imageId: String) {
        viewModelScope.launch {
            val updatedFavorites = favoriteShared.toggleFavorite(imageId)
            _favoriteIds.value = updatedFavorites
        }
    }

}

