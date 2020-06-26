package com.example.newsandroid.domain

data class NewsCollection (
    val status: String,
    val totalResults: Int,
    val articles: List<NewsProperty>
)