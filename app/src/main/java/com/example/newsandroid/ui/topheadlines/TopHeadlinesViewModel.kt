package com.example.newsandroid.ui.topheadlines

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsandroid.R
import com.example.newsandroid.adapter.CustomAdapterSpinner
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




class TopHeadlinesViewModel(application: Application) : ViewModel() {

    val imageSpinner = arrayOf(
        R.drawable.fr,
        R.drawable.us
    )

    val imageNameSpinner = arrayOf(
        "FR", "US"
    )

    val spinnerCustomAdapter = CustomAdapterSpinner(application, imageSpinner, imageNameSpinner);


    val errorApiMessage = "HTTP 400 Bad Request"

    val sharedPreferences = application.getSharedPreferences("com.exemple.newsAndroid", Context.MODE_PRIVATE)

    var currentCountry = sharedPreferences.getString("Country", Country.FR.value)
    var currentCategory = sharedPreferences.getString("Category", Category.GENERAL.value)
    var currentPositionCountry = sharedPreferences.getInt("CountryPosition", Country.FR.position)

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
        _categoryList.value = categories
        getTopHeadlinesProperties()
    }

    fun getTopHeadlinesProperties() {
        refreshList()
    }

    fun onFilterChanged(filter: String, isChecked: Boolean) {
        if (this.filter.update(filter, isChecked)) {

            currentCategory = this.filter.currentValue.toString()//currentCategory
            sharedPreferences.edit().putString("Category",this.filter.currentValue.toString()).commit()
            refreshList()
        }
    }


    fun changeCurrentCountry() {
        if (currentCountry == Country.FR.value){
            currentCountry = Country.US.value
            currentPositionCountry = Country.US.position
        }
        else {
            currentCountry = Country.FR.value
            currentPositionCountry = Country.FR.position
        }
        sharedPreferences.edit().putString("Country", currentCountry).commit()
        sharedPreferences.edit().putInt("CountryPosition", currentPositionCountry).commit()
    }

    fun onCountryChanged(){
        refreshList()
    }

    private fun refreshList() {
        coroutineScope.launch {
            try {
                _status.value = NewsApiStatus.LOADING
                if(newsRepository.refreshNewsTopHeadlines(currentCountry!!, currentCategory) !=0){
                    _status.value = NewsApiStatus.DONE
                }
                else{
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