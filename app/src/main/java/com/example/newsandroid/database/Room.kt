package com.example.newsandroid.database

import android.content.Context
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