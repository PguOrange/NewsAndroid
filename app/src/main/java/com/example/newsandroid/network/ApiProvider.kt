package com.example.newsandroid.network

import com.example.newsandroid.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val BASE_URL = BuildConfig.BaseURL

class ApiProvider {

    fun getInstance(): Retrofit {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .build()

        return retrofit
    }
}