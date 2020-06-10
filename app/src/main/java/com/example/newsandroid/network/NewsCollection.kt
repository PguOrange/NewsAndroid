package com.example.newsandroid.network

data class NewsCollection (
    val status: String,
    val total_results: Int,
    val articles: ArrayList<NewsProperty>
)