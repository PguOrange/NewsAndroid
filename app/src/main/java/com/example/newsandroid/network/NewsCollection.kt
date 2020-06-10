package com.example.newsandroid.network

import com.google.gson.annotations.SerializedName

data class NewsCollection (
    val status: String,
    val total_results: Int,
@SerializedName("articles")
    val articles: ArrayList<NewsProperty>
)