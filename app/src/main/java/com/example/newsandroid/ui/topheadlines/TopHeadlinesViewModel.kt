package com.example.newsandroid.ui.topheadlines

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsandroid.database.DBProvider
import com.example.newsandroid.enums.Category
import com.example.newsandroid.enums.Country
import com.example.newsandroid.enums.NewsApiStatus
import com.example.newsandroid.repository.NewsRepository
import com.example.newsandroid.util.createChipCategoryList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


var currentCategory = Category.GENERAL.value

class TopHeadlinesViewModel(application: Application) : ViewModel() {

    var currentCountry = Country.FR.value
    var textCategory = currentCategory

    private val newsRepository = NewsRepository(DBProvider.getDatabase(application), DBProvider.getDatabaseEverything(application))

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

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    init {
        getTopHeadlinesProperties()
    }

    fun getTopHeadlinesProperties() {
        coroutineScope.launch {
            try {
                _status.value = NewsApiStatus.LOADING
                _categoryList.value = categories
                newsRepository.refreshNewsTopHeadlines(currentCountry, currentCategory)
                Log.d("refreshNews", "News refreshed")
                _status.value = NewsApiStatus.DONE
            }catch (e: Exception){
                if (property.value.isNullOrEmpty())
                    _status.value = NewsApiStatus.ERROR
                else
                    _status.value = NewsApiStatus.ERRORWITHCACHE
            }
        }
    }

    fun onFilterChanged(filter: String, isChecked: Boolean) {
        if (this.filter.update(filter, isChecked)) {
            textCategory = currentCategory
            refreshList()
        }
    }

    fun changeCurrentCountry(){
        currentCountry = if (currentCountry == Country.FR.value) Country.US.value else Country.FR.value
    }

    fun onCountryChanged(){
        refreshList()
    }

    fun onListRefreshed(){
        refreshList()
    }

     private fun refreshList(){
        coroutineScope.launch {
            try {
                _status.value = NewsApiStatus.LOADING
                newsRepository.refreshNewsTopHeadlines(currentCountry, currentCategory)
                Log.d("refreshNews", "News refreshed")
                _status.value = NewsApiStatus.DONE
            }catch (e: Exception){
                if (property.value.isNullOrEmpty())
                    _status.value = NewsApiStatus.ERROR
                else
                    _status.value = NewsApiStatus.ERRORWITHCACHEANDFILTERDISPLAYED
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private class FilterHolder {
        fun update(changedFilter: String, isChecked: Boolean): Boolean {
            if (isChecked) {
                currentCategory = changedFilter
                return true
            } else if (currentCategory == changedFilter) {
                currentCategory = Category.GENERAL.value
                return true
            }
            return false
        }
    }
}