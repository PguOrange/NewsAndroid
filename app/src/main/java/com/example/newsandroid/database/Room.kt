package com.example.newsandroid.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NewsTopHeadlinesDao {
    @Query("select * from databasenewstopheadlines")
    fun getNews(): LiveData<List<DatabaseNewsTopHeadlines>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( news: List<DatabaseNewsTopHeadlines>)
}

@Dao
interface NewsEverythingDao {
    @Query("select * from databasenewseverything")
    fun getNews(): LiveData<List<DatabaseNewsEverything>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( news: List<DatabaseNewsEverything>)

    @Query("DELETE FROM databasenewseverything")
    fun deleteAll()
}

@Database(entities = [DatabaseNewsTopHeadlines::class, DatabaseNewsEverything::class], version = 2)
abstract class NewsDatabase: RoomDatabase() {
    abstract val newsTopHeadlinesDao: NewsTopHeadlinesDao
    abstract val newsEverythingDao: NewsEverythingDao
}