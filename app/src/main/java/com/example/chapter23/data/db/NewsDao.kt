package com.example.chapter23.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chapter23.data.model.News

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(news: News)

    @Query("SELECT * FROM favorite_news ORDER BY date DESC")
    suspend fun getAllFavorites(): List<News>

    @Query("DELETE FROM favorite_news WHERE url = :url")
    suspend fun deleteFavorite(url: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_news WHERE url = :url)")
    suspend fun isFavorite(url: String): Boolean
    @Query("DELETE FROM favorite_news")
    suspend fun deleteAll()
}