package com.example.myapplication.wallpaper.domain.usecase

import com.example.myapplication.wallpaper.domain.model.Resource
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.domain.model.WallpaperDestination
import com.example.myapplication.wallpaper.domain.repository.SetWallpaperRepository
import javax.inject.Inject

class SetWallpaperUseCase @Inject constructor(
    private val wallpaperSetter: SetWallpaperRepository
){

    suspend operator fun invoke(
        wallpaper: Wallpaper,
        destination: WallpaperDestination
    ): Resource<Unit> {

        return wallpaperSetter.setWallpaper(
            wallpaper.image_url,
            destination
        )
    }
}