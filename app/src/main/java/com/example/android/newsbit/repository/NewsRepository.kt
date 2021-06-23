package com.example.android.newsbit.repository

import com.example.android.newsbit.api.RetrofitInstance
import com.example.android.newsbit.db.ArticleDatabase
import com.example.android.newsbit.models.Article

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getTopNews(countryCode: String,category:String, pageNumber: Int) =
        RetrofitInstance.api.getTopNews(countryCode, category, pageNumber)

    suspend fun getCustomCategoryNews(q: String,language:String,from: String, pageNumber: Int) =
        RetrofitInstance.api.getCustomCategoryNews(q,"",language, from, pageNumber)

    suspend fun getSourceNews(sources: String,language:String,from: String, pageNumber: Int) =
        RetrofitInstance.api.getCustomCategoryNews("",sources,language, from, pageNumber)


    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}
