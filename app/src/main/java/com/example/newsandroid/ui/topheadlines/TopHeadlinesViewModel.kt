package com.example.newsandroid.ui.topheadlines

import android.app.Application
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

class TopHeadlinesViewModel() : ViewModel() {
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
                var listResult = getPropertiesDeferred.await()

                if (listResult.articles.size > 0) {
                    _property.value = listResult.articles
                }
                Log.d("getProperties", listResult.articles.size.toString())
            }catch (e: Exception){
                Log.d("getProperties", "Failure "+e.message)
                //_property.value = "Failure: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}