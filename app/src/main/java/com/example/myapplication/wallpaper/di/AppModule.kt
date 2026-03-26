package com.example.myapplication.wallpaper.di

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.wallpaper.data.repository.FavoriteRepositoryImpl
import com.example.myapplication.wallpaper.data.repository.WallpaperRepositoryImpl
import com.example.myapplication.wallpaper.domain.repository.FavoriteRepository
import com.example.myapplication.wallpaper.domain.repository.WallpaperRepository
import com.example.myapplication.wallpaper.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("wallpaper_prefs", Context.MODE_PRIVATE)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetWallpapersPagingUseCase(
        repository: WallpaperRepository
    ): GetWallpapersPagingUseCase = GetWallpapersPagingUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMostViewedUseCase(
        repository: WallpaperRepository
    ): GetMostViewedUseCase = GetMostViewedUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMostFavoritedUseCase(
        repository: WallpaperRepository
    ): GetMostFavoritedUseCase = GetMostFavoritedUseCase(repository)

    @Provides
    @Singleton
    fun provideGetBannerUseCase(
        repository: WallpaperRepository
    ): GetBannerUseCase = GetBannerUseCase(repository)

    @Provides
    @Singleton
    fun provideGetImageDetailUseCase(
        repository: WallpaperRepository
    ): GetImageDetailUseCase = GetImageDetailUseCase(repository)

    @Provides
    @Singleton
    fun provideGetFavoritesUseCase(
        repository: FavoriteRepository
    ): GetFavoritesUseCase = GetFavoritesUseCase(repository)

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(
        repository: FavoriteRepository
    ): ToggleFavoriteUseCase = ToggleFavoriteUseCase(repository)

    @Provides
    @Singleton
    fun provideSetWallpaperUseCase(): SetWallpaperUseCase = SetWallpaperUseCase()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWallpaperRepository(
        impl: WallpaperRepositoryImpl
    ): WallpaperRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        impl: FavoriteRepositoryImpl
    ): FavoriteRepository
}