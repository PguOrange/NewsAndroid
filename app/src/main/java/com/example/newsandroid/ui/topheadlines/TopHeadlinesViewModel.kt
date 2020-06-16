package com.example.newsandroid.ui.topheadlines

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsandroid.database.getDatabase
import com.example.newsandroid.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


enum class NewsApiStatus { LOADING, ERROR, DONE }

class TopHeadlinesViewModel(application: Application) : ViewModel() {

    private var filter = FilterHolder()

    private val newsRepository = NewsRepository(getDatabase(application))

    private val _status = MutableLiveData<NewsApiStatus>()

    private val _countryList = MutableLiveData<List<String>>()

    //private var _countryList: MutableLiveData<List<String>>? = null

    val countryList: LiveData<List<String>>
        get() = _countryList

  /*  fun getCountryList(): LiveData<List<String>>? {
        if (_countryList == null) {
            _countryList = MutableLiveData()
            loadFruits()
        }
        return _countryList
    }
*/
    private fun loadFruits() {
        // do async operation to fetch users
        val myHandler = Handler()
        myHandler.postDelayed({
            val fruitsStringList: MutableList<String> = ArrayList()
            fruitsStringList.add("Mango")
            fruitsStringList.add("Apple")
            fruitsStringList.add("Orange")
            fruitsStringList.add("Banana")
            fruitsStringList.add("Grapes")
            _countryList.value = fruitsStringList
        }, 5000)
    }
    val status: LiveData<NewsApiStatus>
        get() = _status

    val countries : String = "FR"


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
                loadFruits()
            }catch (e: Exception){
                if (property.value.isNullOrEmpty())
                _status.value = NewsApiStatus.ERROR
            }
        }
    }

    fun onFilterChanged(filter: String, isChecked: Boolean) {
        if (this.filter.update(filter, isChecked)) {
            getTopHeadlinesProperties()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private class FilterHolder {
        var currentValue: String? = null
            private set

        fun update(changedFilter: String, isChecked: Boolean): Boolean {
            if (isChecked) {
                currentValue = changedFilter
                return true
            } else if (currentValue == changedFilter) {
                currentValue = null
                return true
            }
            return false
        }
    }
}