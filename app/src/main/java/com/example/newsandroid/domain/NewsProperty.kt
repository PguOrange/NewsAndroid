package com.example.newsandroid.domain

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import java.io.Serializable

data class NewsProperty (
    val title: String,
    val author: String,
    val description: String,
    val url: String,
    val urlToImage : String,
    val publishedAt : String,
    val content : String
) : Serializable {
    constructor() : this("", "",
        "", "", "",
        "", ""
    )
}
