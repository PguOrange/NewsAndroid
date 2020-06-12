package com.example.newsandroid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.newsandroid.database.DatabaseNews
import com.example.newsandroid.database.NewsDatabase
import com.example.newsandroid.database.asDomainModel
import com.example.newsandroid.domain.NewsProperty
import com.example.newsandroid.network.NewsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(private val database: NewsDatabase) {
    val news: LiveData<List<NewsProperty>> = Transformations.map(database.newsDao.getNews()) {
        it.asDomainModel()
    }

    suspend fun refreshNews() {
        withContext(Dispatchers.IO) {
            val newsCollection =
                NewsApi.retrofitService.getProperties("fr", "dc62650aa1db4ea398e6dda8f39c2612")
                    .await()
            database.newsDao.insertAll(convertAPIArticleToDBArticle(newsCollection.articles))
        }
    }

    private fun convertAPIArticleToDBArticle(apiArticles: List<NewsProperty>): List<DatabaseNews> {
        return apiArticles.map {
            DatabaseNews(
                id = apiArticles.indexOf(it),
                title = it.title,
                author = if (it.author == null) "" else it.author,
                description = if (it.description == null) "" else it.description
            )
        }
    }
}