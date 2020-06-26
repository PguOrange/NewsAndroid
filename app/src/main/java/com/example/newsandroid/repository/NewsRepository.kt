package com.example.newsandroid.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.newsandroid.database.NewsDatabase
import com.example.newsandroid.domain.NewsCollection
import com.example.newsandroid.domain.NewsProperty
import com.example.newsandroid.enums.Country
import com.example.newsandroid.network.NewsApiService
import com.example.newsandroid.network.ApiProvider
import com.example.newsandroid.ui.topheadlines.NewsApiStatus
import com.example.newsandroid.util.convertAPIArticleToDBArticle
import com.example.newsandroid.util.convertDBArticleToAPIArticle
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.FlexibleTypeDeserializer

class NewsRepository(private val database: NewsDatabase, private val newsApiService: NewsApiService? = ApiProvider.getInstance()) {
    val news: LiveData<List<NewsProperty>> = Transformations.map(database.newsDao.getNews()) {
        convertDBArticleToAPIArticle(it)
    }

    suspend fun refreshNews(country: String, category : String?) : Int {
        var size = 0
        withContext(Dispatchers.IO) {
            try {
                val newsCollection = newsApiService?.getProperties(country, category)?.await()
                if (newsCollection != null && newsCollection.totalResults > 0) {
                    database.newsDao.insertAll(convertAPIArticleToDBArticle(newsCollection.articles))
                    size = newsCollection.totalResults
                }else{
                    Log.d("refreshNews", "newsCollection is null")
                }
            }catch (NewsApiGetException : Exception){
                Log.d("refreshNews", NewsApiGetException.message)
                throw NewsApiGetException
            }
        }
        return size
    }

}