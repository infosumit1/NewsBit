package com.example.android.newsbit.api

import com.example.android.newsbit.models.NewsResponse
import com.example.android.newsbit.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getTopNews(
        @Query("country")
        countryCode: String = "",
        @Query("category")
        category: String = "",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getCustomCategoryNews(
        @Query("q")
        query: String = "",
        @Query("sources")
        sources: String = "",
        @Query("language")
        language: String = "en",
        @Query("from")
        from: String = "",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY

    ): Response<NewsResponse>

}