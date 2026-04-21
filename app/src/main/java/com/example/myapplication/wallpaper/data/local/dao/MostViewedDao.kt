package com.example.myapplication.wallpaper.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.wallpaper.data.local.entity.MostViewedEntity

@Dao
interface MostViewedDao {

    @Query("SELECT * FROM most_viewed WHERE indexKey = :indexKey ORDER BY position ASC")
    fun pagingSource(indexKey: String): PagingSource<Int, MostViewedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MostViewedEntity>)

    @Query("DELETE FROM most_viewed WHERE indexKey = :indexKey")
    suspend fun clearByIndex(indexKey: String)
}
