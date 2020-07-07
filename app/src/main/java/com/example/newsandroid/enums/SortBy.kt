package com.example.newsandroid.enums

enum class SortBy(val paramApi: String) {
    Pertinence( "relevancy"),
    Dernieres( "publishedAt"),
    Populaire("popularity")
}