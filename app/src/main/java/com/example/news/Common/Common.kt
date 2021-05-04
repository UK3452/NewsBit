package com.example.news.Common

import com.example.news.Interface.NewsService
import com.example.news.Remote.RetrofitClient

//import javax.xml.transform.Source

object Common {
    val BASE_URL = "https://newsapi.org/"
    val API_KEY = "b555abf6dd3145589a84fd76e00adf9d"

    val newsService: NewsService
        get() = RetrofitClient.getClient(BASE_URL).create(NewsService::class.java)

    /*http://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=b555abf6dd3145589a84fd76e00adf9d*/
    fun getNewsAPI(source: String): String {
        val apiUrl = StringBuilder("https://newsapi.org/v2/top-headlines?sources=")
                .append(source)
                .append("&apiKey=")
                .append(API_KEY)
                .toString()

        return apiUrl
    }
    fun getBussinessNewsAPI(source: String): String {
        val apiUrl = StringBuilder("https://newsapi.org/v2/top-headlines?category=business&country=in&language=en")
                .append(source)
                .append("&apiKey=")
                .append(API_KEY)
                .toString()

        return apiUrl
    }
}