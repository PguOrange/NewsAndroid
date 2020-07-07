package com.example.newsandroid.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NewsDao {
    @Query("select * from databasenewstopheadlines")
    fun getNewsTopHeadlines(): LiveData<List<DatabaseNewsTopHeadlines>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTopHeadlines( news: List<DatabaseNewsTopHeadlines>)

    @Query("select * from databasenewseverything")
    fun getNewsEverything(): LiveData<List<DatabaseNewsEverything>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEverything( news: List<DatabaseNewsEverything>)

    @Query("DELETE FROM databasenewseverything")
    fun deleteAllEverything()
}

@Database(entities = [DatabaseNewsTopHeadlines::class, DatabaseNewsEverything::class], version = 2)
abstract class NewsDatabase: RoomDatabase() {
    abstract val newsDao: NewsDao
}