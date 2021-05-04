package com.example.news.Api

import com.google.gson.JsonElement

data class newsAPI_Json(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)