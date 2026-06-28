package com.example.chapter23.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_news")
data class News(
    @PrimaryKey(autoGenerate = false)
    val url: String,
    val title: String,
    @SerializedName("author_name") val source: String,
    @SerializedName("thumbnail_pic_s") val imageUrl: String,
    val date: String
)