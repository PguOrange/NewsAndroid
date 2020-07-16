package com.example.newsandroid.ui.everything

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsandroid.adapter.PaginationListener.Companion.PAGE_START
import com.example.newsandroid.database.DBProvider
import com.example.newsandroid.enums.NewsApiStatus
import com.example.newsandroid.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import com.example.newsandroid.MyApp.Companion.globalLanguage


class EverythingViewModel(application: Application) : ViewModel() {

    private val newsRepository = NewsRepository(DBProvider.getDatabase(application))

    private val _status = MutableLiveData<NewsApiStatus>()

    val status: LiveData<NewsApiStatus>
        get() = _status

    val property = newsRepository.newsEverything

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val sharedPreferences = application.getSharedPreferences("com.exemple.newsAndroid", Context.MODE_PRIVATE)

    private val dateDisplay = "--/--/----"

    //var currentLanguage = sharedPreferences.getString("Language", "FR")
    var currentSort = sharedPreferences.getString("Sort", "relevancy")
    var currentLanguagePosition = sharedPreferences.getInt("LanguagePosition", 0)
    var currentSortPosition = sharedPreferences.getInt("SortPosition", 0)
    var currentFromDate = sharedPreferences.getString("FromDate", "")
    var currentFromDateFR = sharedPreferences.getString("FromDateFR", dateDisplay)
    lateinit var tmpDate : Date
    var tmpDateUsed = false

    var currentToDate = sharedPreferences.getString("ToDate", "")
    var currentToDateFR = sharedPreferences.getString("ToDateFR", dateDisplay)
    lateinit var tmpToDate : Date
    var tmpToDateUsed = false

    var currentQuery = sharedPreferences.getString("Query", "bitcoin")


    var currentPage: Int = PAGE_START
    var isLastPage = false
    var isLoading = false
    var itemCount = 0
    var size = 0


    init {
        getEverythingProperties()
    }

    fun getEverythingProperties() {
        coroutineScope.launch {
            try {
                _status.value = NewsApiStatus.LOADING
                size = newsRepository.refreshNewsEverything(
                    currentQuery!!, currentPage, globalLanguage.language.toLowerCase(Locale.ROOT),
                    currentSort!!, currentFromDate!!, currentToDate!!
                )
                Log.d("refreshNews", "Everything News refreshed " + size)

                when {
                    size == 0 -> {
                        Log.d("refreshNews", "DONE EMPTY")
                        _status.value = NewsApiStatus.DONE_EMPTY
                    }
                    size < 20 -> {
                        Log.d("refreshNews", "EndPage")
                        _status.value = NewsApiStatus.END_PAGE
                    }
                    currentPage*20>=size -> {
                        Log.d("refreshNews", "EndPage")
                        _status.value = NewsApiStatus.END_PAGE
                    }
                    else -> {
                        _status.value = NewsApiStatus.DONE
                        Log.d("refreshNews", "DONE")
                    }
                }
            }catch (e: Exception){
                when {
                    currentPage>1 -> _status.value = NewsApiStatus.END_PAGE
                    property.value.isNullOrEmpty() -> {
                        _status.value = NewsApiStatus.ERROR
                    }
                    else -> {
                        _status.value = NewsApiStatus.ERROR_WITH_CACHE
                    }
                }
            }
        }
    }




    fun onFilterChanged(sort: String, sortPos: Int){
        currentSort = sort
        currentSortPosition = sortPos
        sharedPreferences.edit().putString("Sort", sort).apply()
        sharedPreferences.edit().putInt("SortPosition", sortPos).apply()

        itemCount = 0
        currentPage = PAGE_START
        isLastPage = false
    }

    fun onFromDateChanged(fromDate: String, fromDateFR: String){
        currentFromDate = fromDate
        currentFromDateFR = fromDateFR
        sharedPreferences.edit().putString("FromDate", fromDate).apply()
        sharedPreferences.edit().putString("FromDateFR", fromDateFR).apply()
        itemCount = 0
        currentPage = PAGE_START
        isLastPage = false
    }

    fun onToDateChanged(toDate: String, toDateFR: String){
        currentToDate = toDate
        currentToDateFR = toDateFR
        sharedPreferences.edit().putString("ToDate", toDate).apply()
        sharedPreferences.edit().putString("ToDateFR", toDateFR).apply()
        itemCount = 0
        currentPage = PAGE_START
        isLastPage = false
    }

    fun onFromDateCanceled(){
        currentFromDate = ""
        currentFromDateFR = dateDisplay
        sharedPreferences.edit().putString("FromDate", "").apply()
        sharedPreferences.edit().putString("FromDateFR", dateDisplay).apply()
    }

    fun onToDateCanceled(){
        currentToDate = ""
        currentToDateFR = dateDisplay
        sharedPreferences.edit().putString("ToDate", "").apply()
        sharedPreferences.edit().putString("ToDateFR", dateDisplay).apply()
    }

    fun onFilterReset(){
        currentSort = "relevancy"
        sharedPreferences.edit().putString("Sort", "relevancy").apply()
        currentLanguagePosition = 0
        sharedPreferences.edit().putInt("LanguagePosition", 0).apply()
        currentSortPosition = 0
        sharedPreferences.edit().putInt("SortPosition", 0).apply()
        onFromDateCanceled()
        onToDateCanceled()
        itemCount = 0
        currentPage = PAGE_START
        isLastPage = false
    }

    fun onDateSelected(dateSelected : Date) {
        tmpDate = dateSelected
        tmpDateUsed = true
    }

    fun onToDateSelected(dateSelected : Date) {
        tmpToDate = dateSelected
        tmpToDateUsed = true
    }

    fun onQueryChanged(query: String){
        currentQuery = query
        sharedPreferences.edit().putString("Query", query).apply()
        itemCount = 0
        currentPage = PAGE_START
        isLastPage = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}