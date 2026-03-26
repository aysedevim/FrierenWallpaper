package com.example.myapplication.wallpaper.data.shared

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteShared @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    private val gson = Gson()
    private val favoritesKey = "favorites_list"
    fun saveFavorites(favorites: List<String>) {
        val json = gson.toJson(favorites)
        sharedPreferences.edit().putString("favorites_list", json).apply()
    }

    fun getFavorites(): List<String> {
        val json = sharedPreferences.getString("favorites_list", null)
        return if (json != null) {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun toggleFavorite(imageId: String): List<String> {
        val currentFavorites = getFavorites().toMutableList()

        return if (currentFavorites.contains(imageId)) {
            currentFavorites.remove(imageId)
            currentFavorites.toList()
        } else {
            currentFavorites.add(imageId)
            currentFavorites.toList()
        }.also {
            saveFavorites(it)
        }
    }

}
