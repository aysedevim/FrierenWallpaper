package com.example.myapplication.wallpaper.data.remote

import com.example.myapplication.wallpaper.domain.model.Wallpaper
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface WallpaperApi {

    @GET("images")
    suspend fun getImages(
        @Header("X-API-Key") apiKey: String = "applixus12345",
        @Query("index") index: String,
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 0
    ): List<Wallpaper>


    @GET("images/most-viewed")
    suspend fun getMostViewed(
        @Header("X-API-Key") apiKey: String = "applixus12345",
        @Query("index") index: String,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 0
    ): List<Wallpaper>


    @GET("images/most-favorited")
    suspend fun getMostFavorited(
        @Header("X-API-Key") apiKey: String = "applixus12345",
        @Query("index") index: String,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 0
    ): List<Wallpaper>


    @GET("images/{image_id}")
    suspend fun getImageDetail(
        @Header("X-API-Key") apiKey: String = "applixus12345",
        @Path("image_id") imageId: String
    ): Wallpaper


    @GET("banner")
    suspend fun getBanner(
        @Header("X-API-Key") apiKey: String = "applixus12345",
        @Query("index") index: String
    ): Wallpaper
}