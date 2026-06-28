package com.example.chapter23.data.model

data class NewsResponse(
    val reason: String,
    val result: Result
) {
    data class Result(
        val stat: String,
        val data: List<News>
    )
}