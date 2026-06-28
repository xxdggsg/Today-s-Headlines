package com.example.chapter23.data.api

import com.example.chapter23.data.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("toutiao/index")
    fun getTopNews(
        @Query("type") type: String,
        @Query("key") apiKey: String
    ): Call<NewsResponse>
}