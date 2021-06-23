package com.example.android.newsbit.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.newsbit.models.Article
import com.example.android.newsbit.models.NewsResponse
import com.example.android.newsbit.repository.NewsRepository
import com.example.android.newsbit.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    val topNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var topNewsResponse: NewsResponse? = null

    val categoryNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var categoryNewsResponse: NewsResponse? = null

    val sourceNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var sourceNewsResponse: NewsResponse? = null




    val TAG = "lalalalalla ViewModel"

    init {
        getTopNews("in", 1)
    }
    /*
    The code inside the init block is the first to be executed when the class is instantiated.
    in the init block above we are calling getTopNews function i.e. we are making network call
    for retrieving topNews from here & not from fragment.
    */

    fun getTopNews(countryCode: String, topNewsPageTemp: Int) = viewModelScope.launch {
        topNews.postValue(Resource.Loading())
        val response = newsRepository.getTopNews(
            countryCode,
            "",
            topNewsPageTemp
        ) // in this line we receive network response
        topNews.postValue(handleTopNewsResponse(response)) // now we handle that response in handleTopNewsResponse function
    }

    fun getCategoryNews(
        countryCode: String,
        category: String,
        categoryNewsPageTemp: Int,

    ) = viewModelScope.launch {

        categoryNews.postValue(Resource.Loading())
        val response = newsRepository.getTopNews(
            countryCode,
            category,
            categoryNewsPageTemp
        ) // in this line we receive network response
        categoryNews.postValue(
            handleCategoryNewsResponse(
                response,
                categoryNewsPageTemp
            )
        ) // now we handle that response in handleTopNewsResponse function
    }

    fun getCustomCategoryNews(
        q: String,
        language: String,
        from: String,
        customCategoryNewsPageTemp: Int
        ) = viewModelScope.launch {
        categoryNews.postValue(Resource.Loading())
        val response = newsRepository.getCustomCategoryNews(
            q,
            language,
            from,
            customCategoryNewsPageTemp
        ) // in this line we receive network response
        categoryNews.postValue(
            handleCategoryNewsResponse(
                response,
                customCategoryNewsPageTemp
            )
        ) // now we handle that response in handleTopNewsResponse function
    }


    fun getSourceNews(
        sources: String,
        language: String,
        from: String,
        sourceNewsPageTemp: Int
    ) = viewModelScope.launch {
        sourceNews.postValue(Resource.Loading())
        val response = newsRepository.getSourceNews(
            sources,
            language,
            from,
            sourceNewsPageTemp
        ) // in this line we receive network response
        sourceNews.postValue(
            handleSourceNewsResponse(
                response,
                sourceNewsPageTemp
            )
        ) // now we handle that response in handleTopNewsResponse function
    }


    /* learn more about Response and resource
    response takes type Response but we have wrapper around NewsResponse
    Why so?
     */


    private fun handleTopNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if (topNewsResponse == null) {
                    //if first response then save it in topNewsResponse
                    topNewsResponse = resultResponse
                } else {
                    /*val oldArticles = topNewsResponse?.articles*/
                    val newArticles = resultResponse.articles
                    topNewsResponse?.articles?.addAll(newArticles) //add articles on next page to oldList
                }

                /* Explanation: How next line works
                *    return Resource.Success(topNewsResponse?:resultResponse)
                *
                * if topNewsResponse is null then we return resultResponse else
                * we return topNewsResponse.
                *
                * Normally I could return topNewsResponse directly i.e.
                *   return Resource.Success(topNewsResponse)
                *
                * because if topNewsResponse is null then
                * I am performing
                *  if(topNewsResponse == null) {
                *    //if first response then save it in topNewsResponse
                *    topNewsResponse = resultResponse
                *    }
                * in the code above this comment.
                * But Kotlin says
                * Smart cast to 'Type' is impossible, because 'variable' is a mutable property
                * that could have been changed by this time
                * because it says that some or the other reason between if code above
                * and returning topNewsResponse below,  topNewsResponse could have changed and become
                * null.
                * To read More can refer to this stack overflow question
                * https://stackoverflow.com/questions/44595529/
                */

                return Resource.Success(topNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleCategoryNewsResponse(
        categoryResponse: Response<NewsResponse>,
        categoryNewsPageTemp: Int
    ): Resource<NewsResponse> {
        if (categoryResponse.isSuccessful) {
            categoryResponse.body()?.let { resultResponse ->
                /*Log.e(TAG, resultResponse.articles.size.toString())*/
                /*categoryNewsPage++*/

                if(categoryNewsPageTemp == 1) {
                    categoryNewsResponse = resultResponse
                }
                else {
                    val newArticles = resultResponse.articles
                    categoryNewsResponse?.articles?.addAll(newArticles)
                }
              return Resource.Success(categoryNewsResponse?:resultResponse)

            }
        }
        return Resource.Error(categoryResponse.message())
    }


    private fun handleSourceNewsResponse(
        sourceResponse: Response<NewsResponse>,
        sourceNewsPageTemp: Int
    ): Resource<NewsResponse> {
        if (sourceResponse.isSuccessful) {
            sourceResponse.body()?.let { resultResponse ->
                /*Log.e(TAG, resultResponse.articles.size.toString())*/
                /*categoryNewsPage++*/

                if(sourceNewsPageTemp == 1) {
                    sourceNewsResponse = resultResponse
                }
                else {
                    val newArticles = resultResponse.articles
                    sourceNewsResponse?.articles?.addAll(newArticles)
                }
                return Resource.Success(sourceNewsResponse?:resultResponse)

            }
        }
        return Resource.Error(sourceResponse.message())
    }



    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}
