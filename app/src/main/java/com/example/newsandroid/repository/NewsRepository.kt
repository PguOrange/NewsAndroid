package com.example.newsandroid.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.newsandroid.database.NewsDatabase
import com.example.newsandroid.domain.NewsProperty
import com.example.newsandroid.network.NewsApiService
import com.example.newsandroid.network.ApiProvider
import com.example.newsandroid.util.convertAPIArticleToDBArticleET
import com.example.newsandroid.util.convertAPIArticleToDBArticleTH
import com.example.newsandroid.util.convertDBArticleToAPIArticleET
import com.example.newsandroid.util.convertDBArticleToAPIArticleTH
import kotlinx.coroutines.*

class NewsRepository(private val database: NewsDatabase, private val newsApiService: NewsApiService? = ApiProvider.getInstance()) {
    val news: LiveData<List<NewsProperty>> = Transformations.map(database.newsDao.getNewsTopHeadlines()) {
        convertDBArticleToAPIArticleTH(it)
    }

    val newsEverything: LiveData<List<NewsProperty>> = Transformations.map(database.newsDao.getNewsEverything()) {
        convertDBArticleToAPIArticleET(it)
    }

    suspend fun refreshNewsTopHeadlines(country: String, category : String?): Int {
        var size = 0
        withContext(Dispatchers.IO) {
            try {
                val newsCollection = newsApiService?.getTopHeadlines(country, category)?.await()
                if (newsCollection != null && newsCollection.totalResults > 0) {
                    database.newsDao.insertAllTopHeadlines(convertAPIArticleToDBArticleTH(newsCollection.articles))
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

    suspend fun refreshNewsEverything(query: String, language: String?, sort: String, dateFrom: String, dateTo: String): Int {
        var size = 0
        withContext(Dispatchers.IO) {
            try {
                val newsCollection = newsApiService?.getEverything(query, 1, language, sort, dateFrom, dateTo)?.await()
                if (newsCollection != null) {
                    database.newsDao.deleteAllEverything()
                    database.newsDao.insertAllEverything(convertAPIArticleToDBArticleET(newsCollection.articles))
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