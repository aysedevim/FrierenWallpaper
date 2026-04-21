package com.example.myapplication.wallpaper.data.mapper

import com.example.myapplication.wallpaper.data.local.entity.MostFavoritedEntity
import com.example.myapplication.wallpaper.data.local.entity.MostViewedEntity
import com.example.myapplication.wallpaper.domain.model.Wallpaper

fun Wallpaper.toMostViewedEntity(position: Int) = MostViewedEntity(
    id = id,
    sourceUrl = source_url,
    imageUrl = image_url,
    crawledAt = crawledAt,
    width = width,
    height = height,
    indexKey = index,
    viewCount = view_count,
    favoriteCount = favorite_count,
    position = position
)

fun Wallpaper.toMostFavoritedEntity(position: Int) = MostFavoritedEntity(
    id = id,
    sourceUrl = source_url,
    imageUrl = image_url,
    crawledAt = crawledAt,
    width = width,
    height = height,
    indexKey = index,
    viewCount = view_count,
    favoriteCount = favorite_count,
    position = position
)

fun MostViewedEntity.toDomain() = Wallpaper(
    id = id,
    source_url = sourceUrl,
    image_url = imageUrl,
    crawledAt = crawledAt,
    width = width,
    height = height,
    index = indexKey,
    view_count = viewCount,
    favorite_count = favoriteCount
)

fun MostFavoritedEntity.toDomain() = Wallpaper(
    id = id,
    source_url = sourceUrl,
    image_url = imageUrl,
    crawledAt = crawledAt,
    width = width,
    height = height,
    index = indexKey,
    view_count = viewCount,
    favorite_count = favoriteCount
)
