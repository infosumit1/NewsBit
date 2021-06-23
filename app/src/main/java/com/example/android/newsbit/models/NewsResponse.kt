package com.example.android.newsbit.models

data class NewsResponse(
    /*
     * made articles Mutable List to add articles on new page in it.(addAll)(Pagination)
     * addAll in newsViewModel will not work on List(because List is Immutable)
    */

    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)