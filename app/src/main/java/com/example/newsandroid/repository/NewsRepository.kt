package com.example.newsandroid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.newsandroid.BuildConfig
import com.example.newsandroid.database.NewsDatabase
import com.example.newsandroid.domain.NewsProperty
import com.example.newsandroid.network.NewsApiService
import com.example.newsandroid.network.ApiProvider
import com.example.newsandroid.util.convertAPIArticleToDBArticle
import com.example.newsandroid.util.convertDBArticleToAPIArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val newsApiService : NewsApiService = ApiProvider().getInstance().create(NewsApiService::class.java)
private val api_key = BuildConfig.ApiKey

class NewsRepository(private val database: NewsDatabase) {
    val news: LiveData<List<NewsProperty>> = Transformations.map(database.newsDao.getNews()) {
        convertDBArticleToAPIArticle(it)
    }

    suspend fun refreshNews() {
        withContext(Dispatchers.IO) {

            val newsCollection =
                newsApiService.getProperties("fr", api_key)
                    .await()
            database.newsDao.insertAll(convertAPIArticleToDBArticle(newsCollection.articles))
        }
    }
}