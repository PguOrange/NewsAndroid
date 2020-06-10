package com.example.newsandroid.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "http://newsapi.org/v2/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface NewsApiService {
    @GET("top-headlines")
    fun getProperties(@Query("country") country : String, @Query("apiKey") apiKey : String):
            Deferred<NewsCollection>
}

object NewsApi {
    val retrofitService : NewsApiService by lazy { retrofit.create(NewsApiService::class.java) }
}