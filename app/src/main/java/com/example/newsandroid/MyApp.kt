package com.example.newsandroid

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.*
import com.example.newsandroid.enums.Language
import com.example.newsandroid.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MyApp: Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    companion object {
        var globalLanguage = Language.FR
    }

    override fun onCreate() {
            super.onCreate()
            delayedInit()
        }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }
            .build()
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(6, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }
    }