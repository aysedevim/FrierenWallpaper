package com.example.myapplication.wallpaper.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "most_favorited")
data class MostFavoritedEntity(
    @PrimaryKey val id: String,
    val sourceUrl: String,
    val imageUrl: String,
    val crawledAt: String?,
    val width: Int?,
    val height: Int?,
    val indexKey: String,
    val viewCount: Int,
    val favoriteCount: Int,
    val position: Int
)
