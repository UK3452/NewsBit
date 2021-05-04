package com.example.news.Interface

import com.example.news.Api.newsAPI_Json
import retrofit2.http.GET

//GET https://newsapi.org/v2/top-headlines?country=in&apiKey=b555abf6dd3145589a84fd76e00adf9d
interface apiRequest {

    @GET("top-headlines?country=in&apiKey=b555abf6dd3145589a84fd76e00adf9d")
    suspend fun getNews() : newsAPI_Json

    @GET("top-headlines?category=business&country=in&language=en&apiKey=b555abf6dd3145589a84fd76e00adf9d")
    suspend fun getBusinesNews() : newsAPI_Json

    @GET("top-headlines?category=entertainment&country=in&language=en&apiKey=b555abf6dd3145589a84fd76e00adf9d")
    suspend fun getEntertainmentNews() : newsAPI_Json

    @GET("top-headlines?category=general&country=in&language=en&apiKey=b555abf6dd3145589a84fd76e00adf9d")
    suspend fun getGeneralNews() : newsAPI_Json

    @GET("top-headlines?category=health&country=in&language=en&apiKey=b555abf6dd3145589a84fd76e00adf9d")
    suspend fun getHealthNews() : newsAPI_Json

    @GET("top-headlines?category=science&country=in&language=en&apiKey=b555abf6dd3145589a84fd76e00adf9d")
    suspend fun getScienceNews() : newsAPI_Json

    @GET("top-headlines?category=sports&country=in&language=en&apiKey=b555abf6dd3145589a84fd76e00adf9d")
    suspend fun getSportsNews() : newsAPI_Json

    @GET("top-headlines?category=technology&country=in&language=en&apiKey=b555abf6dd3145589a84fd76e00adf9d")
    suspend fun getTechNews() : newsAPI_Json

}