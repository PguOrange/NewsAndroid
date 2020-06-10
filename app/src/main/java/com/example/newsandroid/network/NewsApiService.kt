package com.example.newsandroid.network

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "http://newsapi.org/v2/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface NewsApiService {
    @GET("top-headlines")
    fun getProperties(@Query("country") country : String, @Query("apiKey") apiKey : String):
            Call<String>
}

object NewsApi {
    val retrofitService : NewsApiService by lazy { retrofit.create(NewsApiService::class.java) }
}