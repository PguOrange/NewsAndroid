package com.example.newsandroid.network

import android.util.Log
import com.example.newsandroid.BuildConfig
import com.example.newsandroid.domain.NewsCollection
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val BASE_URL = BuildConfig.BaseURL

class ApiProvider {

    companion object {
        @Volatile
        private var retrofit: Retrofit? = null

        @Synchronized
        fun getInstance(): NewsApiService? {
            retrofit ?: synchronized(this) {
                retrofit = buildRetrofit()
            }
            return retrofit?.create(NewsApiService::class.java)
        }

        private fun buildRetrofit() = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .build()

    }


}