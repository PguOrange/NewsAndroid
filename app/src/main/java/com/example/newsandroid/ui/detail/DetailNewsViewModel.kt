package com.example.newsandroid.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsandroid.domain.NewsProperty

class DetailNewsViewModel(application: Application) : ViewModel() {

    private val _news = MutableLiveData<NewsProperty>()

    val news: LiveData<NewsProperty>
        get() = _news


    lateinit var uriImageString: String

    lateinit var uri: String

    fun getNewsDetail(news : NewsProperty) {
        _news.value = news
        uriImageString = news.urlToImage
        uri = news.url
    }
}