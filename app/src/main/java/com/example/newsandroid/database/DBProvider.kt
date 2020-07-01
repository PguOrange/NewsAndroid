package com.example.newsandroid.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


class DBProvider {
    companion object{
        private lateinit var INSTANCE: NewsDatabase
        private val dbName = "news"

        fun getDatabase(context: Context): NewsDatabase {
            synchronized(NewsDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NewsDatabase::class.java,
                        dbName)
                        .addMigrations(MIGRATION_0_1, MIGRATION_1_2)
                        .build()
                }
            }
            return INSTANCE
        }

        private val MIGRATION_0_1: Migration = object : Migration(0, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE databasenewstopheadlines (id INTEGER, title TEXT, author TEXT, description TEXT, url TEXT, urlToImage TEXT, publishedAt TEXT, content TEXT, PRIMARY KEY(id))")
            }
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE databasenewseverything (id INTEGER, title TEXT, author TEXT, description TEXT, url TEXT, urlToImage TEXT, publishedAt TEXT, content TEXT, PRIMARY KEY(id))")
            }
        }
    }
}