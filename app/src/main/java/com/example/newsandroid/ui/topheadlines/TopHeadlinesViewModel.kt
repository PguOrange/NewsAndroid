package com.example.newsandroid.ui.topheadlines

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsandroid.database.DBProvider
import com.example.newsandroid.enums.Category
import com.example.newsandroid.enums.Country
import com.example.newsandroid.repository.NewsRepository
import com.example.newsandroid.util.createChipCategoryList
import com.example.newsandroid.util.createChipList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


enum class NewsApiStatus { LOADING, ERROR, ERROR_WITH_CACHE, ERROR_API, ERROR_API_WITH_CACHE, DONE }

class TopHeadlinesViewModel(application: Application) : ViewModel() {

    val errorApiMessage = "HTTP 400 Bad Request"

    var currentCountry = Country.FR.value
    var currentCategory = Category.GENERAL.value

    private val newsRepository = NewsRepository(DBProvider.getDatabase(application))

    private var filter = FilterHolder()

    private val _status = MutableLiveData<NewsApiStatus>()

    private val _categoryList = MutableLiveData<List<String>>()

    val categoryList: LiveData<List<String>>
        get() = _categoryList

    val status: LiveData<NewsApiStatus>
        get() = _status

    private val categories = createChipCategoryList()

    val property = newsRepository.news

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getTopHeadlinesProperties()
        _categoryList.value = categories
    }

    fun getTopHeadlinesProperties() {
        refreshList()
    }

    fun onFilterChanged(filter: String, isChecked: Boolean) {
        if (this.filter.update(filter, isChecked)) {
            currentCategory = this.filter.currentValue.toString()//currentCategory
            refreshList()
        }
    }

    fun changeCurrentCountry() {
        currentCountry =
            if (currentCountry == Country.FR.value) Country.US.value else Country.FR.value
    }

    fun onCountryChanged() {
        refreshList()
    }

    private fun refreshList() {
        coroutineScope.launch {
            try {
                _status.value = NewsApiStatus.LOADING
                if(newsRepository.refreshNews(currentCountry, currentCategory) !=0){
                    Log.d("refreshNews", "News refreshed")
                    _status.value = NewsApiStatus.DONE
                }
                else{
                    Log.d("refreshNews", "News is empty")
                    if (property.value.isNullOrEmpty())
                        _status.value = NewsApiStatus.ERROR_API
                    else
                        _status.value = NewsApiStatus.ERROR_API_WITH_CACHE
                }
            } catch (e: Exception) {
                if (property.value.isNullOrEmpty())
                    if(e.message == errorApiMessage)
                        _status.value = NewsApiStatus.ERROR_API
                    else
                        _status.value = NewsApiStatus.ERROR
                else
                    if(e.message == errorApiMessage)
                        _status.value = NewsApiStatus.ERROR_API_WITH_CACHE
                    else
                        _status.value = NewsApiStatus.ERROR_WITH_CACHE
            }
        }
    }

    private class FilterHolder {
        var currentValue: String? = null
            private set

        fun update(changedFilter: String, isChecked: Boolean): Boolean {
            when {
                isChecked -> {
                    currentValue = changedFilter
                    return true
                }
                currentValue == changedFilter -> {
                    currentValue = Category.GENERAL.value
                    return true
                }
                else ->
                    return false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}


