package com.example.newsandroid.util

import com.example.newsandroid.database.DatabaseNewsEverything
import com.example.newsandroid.database.DatabaseNewsTopHeadlines
import com.example.newsandroid.domain.NewsProperty

fun convertAPIArticleToDBArticleTH(apiArticles: List<NewsProperty>): List<DatabaseNewsTopHeadlines> {
    return apiArticles.map {
        DatabaseNewsTopHeadlines(
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


fun convertDBArticleToAPIArticleTH(dbArticles: List<DatabaseNewsTopHeadlines>): List<NewsProperty> {
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

fun convertAPIArticleToDBArticleET(apiArticles: List<NewsProperty>): List<DatabaseNewsEverything> {
    return apiArticles.map {
        DatabaseNewsEverything(
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


fun convertDBArticleToAPIArticleET(dbArticles: List<DatabaseNewsEverything>): List<NewsProperty> {
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