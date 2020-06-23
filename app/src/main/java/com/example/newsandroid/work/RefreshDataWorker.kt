package com.example.newsandroid.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.newsandroid.database.DBProvider
import com.example.newsandroid.enums.Country
import com.example.newsandroid.repository.NewsRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.example.newsandroid.work.RefreshDataWorker"
    }


    override suspend fun doWork(): Result {
        val database = DBProvider.getDatabase(applicationContext)
        val databaseEvery = DBProvider.getDatabaseEverything(applicationContext)
        val repository = NewsRepository(database, databaseEvery)
        try {
            repository.refreshNewsTopHeadlines(Country.FR.toString(), null)
        } catch (e: HttpException) {
            return Result.failure()
        }
        return Result.success()
    }
}