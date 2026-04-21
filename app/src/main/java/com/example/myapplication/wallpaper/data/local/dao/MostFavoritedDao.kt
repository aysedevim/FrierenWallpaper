package com.example.myapplication.wallpaper.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.wallpaper.data.local.entity.MostFavoritedEntity

@Dao
interface MostFavoritedDao {

    @Query("SELECT * FROM most_favorited WHERE indexKey = :indexKey ORDER BY position ASC")
    fun pagingSource(indexKey: String): PagingSource<Int, MostFavoritedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MostFavoritedEntity>)

    @Query("DELETE FROM most_favorited WHERE indexKey = :indexKey")
    suspend fun clearByIndex(indexKey: String)
}
