package com.example.myapplication.wallpaper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.wallpaper.data.local.dao.MostFavoritedDao
import com.example.myapplication.wallpaper.data.local.dao.MostViewedDao
import com.example.myapplication.wallpaper.data.local.dao.RemoteKeyDao
import com.example.myapplication.wallpaper.data.local.entity.MostFavoritedEntity
import com.example.myapplication.wallpaper.data.local.entity.MostViewedEntity
import com.example.myapplication.wallpaper.data.local.entity.RemoteKeyEntity

@Database(
    entities = [
        MostViewedEntity::class,
        MostFavoritedEntity::class,
        RemoteKeyEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract fun mostViewedDao(): MostViewedDao
    abstract fun mostFavoritedDao(): MostFavoritedDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
