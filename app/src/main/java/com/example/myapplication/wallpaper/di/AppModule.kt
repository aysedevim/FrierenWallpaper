package com.example.myapplication.wallpaper.di

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.wallpaper.data.repository.FavoriteRepositoryImpl
import com.example.myapplication.wallpaper.data.repository.SetWallpaperRepositoryImpl
import com.example.myapplication.wallpaper.data.repository.WallpaperRepositoryImpl
import com.example.myapplication.wallpaper.domain.repository.FavoriteRepository
import com.example.myapplication.wallpaper.domain.repository.SetWallpaperRepository
import com.example.myapplication.wallpaper.domain.repository.WallpaperRepository
import com.example.myapplication.wallpaper.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
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
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetWallpapersPagingUseCase(
        repository: WallpaperRepository
    ) = GetWallpapersPagingUseCase(repository)

    @Provides
    fun provideGetMostViewedUseCase(
        repository: WallpaperRepository
    ) = GetMostViewedUseCase(repository)

    @Provides
    fun provideGetMostFavoritedUseCase(
        repository: WallpaperRepository
    ) = GetMostFavoritedUseCase(repository)

    @Provides
    fun provideGetBannerUseCase(
        repository: WallpaperRepository
    ) = GetBannerUseCase(repository)

    @Provides
    fun provideGetImageDetailUseCase(
        repository: WallpaperRepository
    ) = GetImageDetailUseCase(repository)

    @Provides
    fun provideGetFavoritesUseCase(
        repository: FavoriteRepository
    ) = GetFavoritesUseCase(repository)

    @Provides
    fun provideToggleFavoriteUseCase(
        repository: FavoriteRepository
    ) = ToggleFavoriteUseCase(repository)

    @Provides
    fun provideSetWallpaperUseCase(
        repository: SetWallpaperRepository
    )= SetWallpaperUseCase(repository)
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

    @Binds
    @Singleton
    abstract fun bindSetWallpaperRepository(
        impl: SetWallpaperRepositoryImpl
    ): SetWallpaperRepository

}

