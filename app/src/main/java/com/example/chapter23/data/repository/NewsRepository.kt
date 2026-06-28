package com.example.chapter23.data.repository

import com.example.chapter23.data.api.NewsApiService
import com.example.chapter23.data.api.RetrofitClient
import com.example.chapter23.data.db.AppDatabase
import com.example.chapter23.data.model.News

class NewsRepository(
    private val apiService: NewsApiService = RetrofitClient.apiService
) {

    /** 从网络获取新闻列表 */
    suspend fun fetchNewsFromNetwork(): List<News>? {
        return try {
            val call = apiService.getTopNews("top", "8898852cfcb012b814b2a04155703f2a")
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()?.result?.data
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /** 获取所有收藏 */
    suspend fun getFavoriteNews(db: AppDatabase): List<News> {
        return db.newsDao().getAllFavorites()
    }

    /** 添加收藏 */
    suspend fun addFavorite(db: AppDatabase, news: News) {
        db.newsDao().insertFavorite(news)
    }

    /** 取消收藏 */
    suspend fun removeFavorite(db: AppDatabase, url: String) {
        db.newsDao().deleteFavorite(url)
    }

    /** 检查是否收藏 */
    suspend fun isFavorite(db: AppDatabase, url: String): Boolean {
        return db.newsDao().isFavorite(url)
    }

    /** 清除所有收藏 */
    suspend fun clearFavorites(db: AppDatabase) {
        db.newsDao().deleteAll()
    }
}