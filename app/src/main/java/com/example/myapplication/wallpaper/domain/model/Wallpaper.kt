package com.example.myapplication.wallpaper.domain.model


data class Wallpaper(
    val id: String,
    val source_url: String,
    val image_url: String,
    val crawledAt: String,
    val width: Int? = null,
    val height: Int? = null,
    val index: String,
    val view_count: Int = 0,
    val favorite_count: Int = 0
)

