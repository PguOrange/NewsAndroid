package com.example.newsandroid.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NewsDao {
    @Query("select * from databasenews")
    fun getNews(): LiveData<List<DatabaseNews>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( news: List<DatabaseNews>)
}

@Database(entities = [DatabaseNews::class], version = 1)
abstract class NewsDatabase: RoomDatabase() {
    abstract val newsDao: NewsDao
}