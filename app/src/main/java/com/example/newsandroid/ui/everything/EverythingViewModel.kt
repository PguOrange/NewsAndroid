package com.example.newsandroid.ui.everything

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsandroid.database.DBProvider
import com.example.newsandroid.enums.NewsApiStatus
import com.example.newsandroid.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class EverythingViewModel(application: Application) : ViewModel() {

    private val newsRepository = NewsRepository(DBProvider.getDatabase(application), DBProvider.getDatabaseEverything(application))

    private val _status = MutableLiveData<NewsApiStatus>()

    val status: LiveData<NewsApiStatus>
        get() = _status

    val property = newsRepository.newsEverything

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    init {
        getEverythingProperties()
    }

    fun getEverythingProperties() {
        coroutineScope.launch {
            try {
                _status.value = NewsApiStatus.LOADING
                newsRepository.refreshNewsEverything()
                Log.d("refreshNews", "Everything News refreshed")
                _status.value = NewsApiStatus.DONE
            }catch (e: Exception){
                if (property.value.isNullOrEmpty())
                    _status.value = NewsApiStatus.ERROR
                else
                    _status.value = NewsApiStatus.ERRORWITHCACHE
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}