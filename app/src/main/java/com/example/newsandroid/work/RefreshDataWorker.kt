package com.example.newsandroid.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.newsandroid.database.getDatabase
import com.example.newsandroid.repository.NewsRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.example.newsandroid.work.RefreshDataWorker"
    }


    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = NewsRepository(database)
        try {
            repository.refreshNews()
        } catch (e: HttpException) {
            return Result.retry()
        }
        return Result.success()
    }
}