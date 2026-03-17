package com.example.myapplication.wallpaper.data.shared

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteShared(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    fun saveFavorites(favorites: List<String>) {
        val json = gson.toJson(favorites)
        prefs.edit().putString("favorites_list", json).apply()
    }

    fun getFavorites(): List<String> {
        val json = prefs.getString("favorites_list", null)
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

    fun isFavorite(imageId: String): Boolean {
        return getFavorites().contains(imageId)
    }

    fun clearFavorites() {
        prefs.edit().clear().apply()
    }
}
