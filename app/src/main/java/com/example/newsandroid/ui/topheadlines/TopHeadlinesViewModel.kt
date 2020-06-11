package com.example.newsandroid.ui.topheadlines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsandroid.network.NewsApi
import com.example.newsandroid.network.NewsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


enum class NewsApiStatus { LOADING, ERROR, DONE }

class TopHeadlinesViewModel() : ViewModel() {

    private val _status = MutableLiveData<NewsApiStatus>()

    val status: LiveData<NewsApiStatus>
        get() = _status

    private val _property = MutableLiveData<List<NewsProperty>>()

    val property: LiveData<List<NewsProperty>>
        get() = _property

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    init {
        getTopHeadlinesProperties()
    }

    private fun getTopHeadlinesProperties() {
        //_response.value = "Set the News API Response here!"
        coroutineScope.launch {
            var getPropertiesDeferred = NewsApi.retrofitService.getProperties("FR","dc62650aa1db4ea398e6dda8f39c2612")
            try {
                _status.value = NewsApiStatus.LOADING
                var listResult = getPropertiesDeferred.await()
                _status.value = NewsApiStatus.DONE
                _property.value = listResult.articles
            }catch (e: Exception){
                _status.value = NewsApiStatus.ERROR
                _property.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}