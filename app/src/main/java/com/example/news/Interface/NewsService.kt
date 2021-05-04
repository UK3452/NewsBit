package com.example.news.Interface

//import com.example.news.Model.News
import com.example.news.Model.News
import com.example.news.Model.Website
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsService {
    @get: GET("v2/sources?apiKey=b555abf6dd3145589a84fd76e00adf9d")
    val sources: Call<Website>

    @GET
    fun getNewsFromSource(@Url url: String): Call<News>
}