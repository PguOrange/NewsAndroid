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

    private val newsRepository = NewsRepository(DBProvider.getDatabase(application))

    private val _status = MutableLiveData<NewsApiStatus>()

    val status: LiveData<NewsApiStatus>
        get() = _status

    val property = newsRepository.newsEverything

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    val currentLanguage = "ALL"
    val currentSort = "publishedAt"

    init {
        getEverythingProperties(currentLanguage, currentSort)
    }

    fun getEverythingProperties(language: String, sort: String) {
        coroutineScope.launch {
            try {
                _status.value = NewsApiStatus.LOADING
                if(language=="ALL") newsRepository.refreshNewsEverything(language = null, sort = sort)
                else newsRepository.refreshNewsEverything(language, sort)
                Log.d("refreshNews", "Everything News refreshed")
                _status.value = NewsApiStatus.DONE
            }catch (e: Exception){
                if (property.value.isNullOrEmpty())
                    _status.value = NewsApiStatus.ERROR
                else
                    _status.value = NewsApiStatus.ERROR_WITH_CACHE
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}