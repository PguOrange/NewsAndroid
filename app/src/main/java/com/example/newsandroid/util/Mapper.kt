package com.example.newsandroid.util

import com.example.newsandroid.database.DatabaseNews
import com.example.newsandroid.domain.NewsProperty

fun convertAPIArticleToDBArticle(apiArticles: List<NewsProperty>): List<DatabaseNews> {
    return apiArticles.map {
        DatabaseNews(
            id = apiArticles.indexOf(it),
            title = it.title,
            author = if (it.author == null) "" else it.author,
            description = if (it.description == null) "" else it.description,
            url = it.url,
            urlToImage = if(it.urlToImage == null) "" else it.urlToImage,
            publishedAt = it.publishedAt,
            content = if(it.content ==  null) "" else it.content
        )
    }
}


fun convertDBArticleToAPIArticle(dbArticles: List<DatabaseNews>): List<NewsProperty> {
    return dbArticles.map {
        NewsProperty(
            title = it.title,
            author = it.author,
            description = it.description,
            url = it.url,
            urlToImage = it.urlToImage,
            publishedAt = it.publishedAt,
            content = it.content
        )
    }
}