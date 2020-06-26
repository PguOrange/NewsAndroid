package com.example.newsandroid.database

import android.content.Context
import androidx.room.Room

class DBProvider {
    companion object{
        private lateinit var INSTANCE: NewsDatabase
        private val dbName = "news"

        fun getDatabase(context: Context): NewsDatabase {
            synchronized(NewsDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NewsDatabase::class.java,
                        dbName).build()
                }
            }
            return INSTANCE
        }
    }
}