package com.example.newsandroid.network

import com.example.newsandroid.BuildConfig
import com.example.newsandroid.domain.NewsCollection
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    fun getProperties(@Query("country") country : String, @Query("category") category: String?, @Query("apiKey") apiKey : String = BuildConfig.ApiKey):
            Deferred<NewsCollection>

}