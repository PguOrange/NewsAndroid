package com.example.newsandroid.database

import android.content.Context
import androidx.room.Room

class DBProvider {
    companion object{
        private lateinit var INSTANCE: NewsDatabase
        private lateinit var INSTANCE_EVERY: EverythingNewsDatabase
        private val dbName = "news"
        private val dbNameEverything = "newsEverything"

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

        fun getDatabaseEverything(context: Context): EverythingNewsDatabase {
            synchronized(EverythingNewsDatabase::class.java) {
                if (!::INSTANCE_EVERY.isInitialized) {
                    INSTANCE_EVERY = Room.databaseBuilder(context.applicationContext,
                        EverythingNewsDatabase::class.java,
                        dbNameEverything).build()
                }
            }
            return INSTANCE_EVERY
        }

    }
}