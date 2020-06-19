package com.example.newsandroid.ui.topheadlines

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsandroid.database.DBProvider
import com.example.newsandroid.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


enum class NewsApiStatus { LOADING, ERROR, DONE }

class TopHeadlinesViewModel(application: Application) : ViewModel() {

    private val newsRepository = NewsRepository(DBProvider.getDatabase(application))

    private val _status = MutableLiveData<NewsApiStatus>()

    val status: LiveData<NewsApiStatus>
        get() = _status
    val property = newsRepository.news

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    init {
        getTopHeadlinesProperties()
    }

    private fun getTopHeadlinesProperties() {
        coroutineScope.launch {
            try {
                _status.value = NewsApiStatus.LOADING
                newsRepository.refreshNews()
                Log.d("refreshNews", "News refreshed")
                _status.value = NewsApiStatus.DONE
            }catch (e: Exception){
                if (property.value.isNullOrEmpty())
                _status.value = NewsApiStatus.ERROR
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}