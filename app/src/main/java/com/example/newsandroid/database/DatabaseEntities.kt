package com.example.newsandroid.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseNewsTopHeadlines(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val author: String,
    val description: String,
    val url: String,
    val urlToImage : String,
    val publishedAt : String,
    val content : String
)

@Entity
data class DatabaseNewsEverything(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val author: String,
    val description: String,
    val url: String,
    val urlToImage : String,
    val publishedAt : String,
    val content : String
)