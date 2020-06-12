package com.example.newsandroid.domain

data class NewsCollection (
    val status: String,
    val total_results: Int,
    val articles: List<NewsProperty>
)