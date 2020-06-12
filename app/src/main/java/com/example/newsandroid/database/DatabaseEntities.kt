package com.example.newsandroid.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsandroid.domain.NewsProperty

@Entity
data class DatabaseNews constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val author: String,
    val description: String
)

fun List<DatabaseNews>.asDomainModel(): List<NewsProperty> {
    return map {
        NewsProperty(
            title = it.title,
            author = it.author,
            description = it.description
        )
    }
}