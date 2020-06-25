package com.example.newsandroid.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.newsandroid.database.EverythingNewsDatabase
import com.example.newsandroid.database.NewsDatabase
import com.example.newsandroid.domain.NewsProperty
import com.example.newsandroid.network.NewsApiService
import com.example.newsandroid.network.ApiProvider
import com.example.newsandroid.util.convertAPIArticleToDBArticle
import com.example.newsandroid.util.convertDBArticleToAPIArticle
import kotlinx.coroutines.*

class NewsRepository(private val database: NewsDatabase, private val databaseEverything: EverythingNewsDatabase, private val newsApiService: NewsApiService? = ApiProvider.getInstance()) {
    val news: LiveData<List<NewsProperty>> = Transformations.map(database.newsDao.getNews()) {
        convertDBArticleToAPIArticle(it)
    }

    val newsEverything: LiveData<List<NewsProperty>> = Transformations.map(databaseEverything.newsDao.getNews()) {
        convertDBArticleToAPIArticle(it)
    }

    suspend fun refreshNewsTopHeadlines(country: String, category : String?) {
        withContext(Dispatchers.IO) {
            try {
                val newsCollection = newsApiService?.getTopHeadlines(country, category)?.await()
                if (newsCollection != null) {
                    database.newsDao.insertAll(convertAPIArticleToDBArticle(newsCollection.articles))
                }else{
                    Log.d("refreshNews", "newsCollection is null")
                }
            }catch (NewsApiGetException : Exception){
                Log.d("refreshNews", NewsApiGetException.message)
                throw NewsApiGetException

            }
        }
    }

    suspend fun refreshNewsEverything(language: String?, sort: String) {
        withContext(Dispatchers.IO) {
            try {
                val newsCollection = newsApiService?.getEverything("bitcoin", language, sort)?.await()
                if (newsCollection != null) {
                    databaseEverything.newsDao.insertAll(convertAPIArticleToDBArticle(newsCollection.articles))
                }else{
                    Log.d("refreshNews", "newsCollection is null")
                }
            }catch (NewsApiGetException : Exception){
                Log.d("refreshNews", NewsApiGetException.message)
                throw NewsApiGetException

            }
        }
    }

}