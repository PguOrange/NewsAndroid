package com.example.newsandroid.ui.everything

import android.app.Application
import android.content.Context
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
import java.util.*


class EverythingViewModel(application: Application) : ViewModel() {

    private val newsRepository = NewsRepository(DBProvider.getDatabase(application))

    private val _status = MutableLiveData<NewsApiStatus>()

    val status: LiveData<NewsApiStatus>
        get() = _status

    val property = newsRepository.newsEverything

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    val sharedPreferences = application.getSharedPreferences("com.exemple.newsAndroid", Context.MODE_PRIVATE)

    var currentLanguage = sharedPreferences.getString("Language", "ALL")
    var currentSort = sharedPreferences.getString("Sort", "relevancy")
    var currentLanguagePosition = sharedPreferences.getInt("LanguagePosition", 0)
    var currentSortPosition = sharedPreferences.getInt("SortPosition", 0)
    var currentFromDate = sharedPreferences.getString("FromDate", "")
    var currentFromDateFR = sharedPreferences.getString("FromDateFR", "--/--/----")
    lateinit var tmpDate : Date
    var tmpDateUsed = false

    var currentToDate = sharedPreferences.getString("ToDate", "")
    var currentToDateFR = sharedPreferences.getString("ToDateFR", "--/--/----")
    lateinit var tmpToDate : Date
    var tmpToDateUsed = false

    var currentQuery = sharedPreferences.getString("Query", "bitcoin")

    init {
        getEverythingProperties()
    }

    fun getEverythingProperties() {
        coroutineScope.launch {
            try {
                _status.value = NewsApiStatus.LOADING
                val size = if(currentLanguage=="ALL") newsRepository.refreshNewsEverything(query = currentQuery!!, language = null, sort = currentSort!!, dateFrom = currentFromDate!!, dateTo = currentToDate!!)
                else newsRepository.refreshNewsEverything(currentQuery!!, currentLanguage!!.toLowerCase(Locale.ROOT),
                    currentSort!!, currentFromDate!!, currentToDate!!)
                Log.d("refreshNews", "Everything News refreshed")
                if(size==0) {
                    Log.d("refreshNews", "DONE EMPTY")
                    _status.value = NewsApiStatus.DONE_EMPTY
                }
                else {
                    _status.value = NewsApiStatus.DONE
                    Log.d("refreshNews", "DONE")
                }
            }catch (e: Exception){
                if (property.value.isNullOrEmpty())
                    _status.value = NewsApiStatus.ERROR
                else
                    _status.value = NewsApiStatus.ERROR_WITH_CACHE
            }
        }
    }

    fun onFilterChanged(language: String, sort: String, languagePos: Int, sortPos: Int){
        currentLanguage = language
        currentSort = sort
        currentLanguagePosition = languagePos
        currentSortPosition = sortPos

        sharedPreferences.edit().putString("Language", language).apply()
        sharedPreferences.edit().putString("Sort", sort).apply()
        sharedPreferences.edit().putInt("LanguagePosition", languagePos).apply()
        sharedPreferences.edit().putInt("SortPosition", sortPos).apply()
    }

    fun onFromDateChanged(fromDate: String, fromDateFR: String){
        currentFromDate = fromDate
        currentFromDateFR = fromDateFR
        sharedPreferences.edit().putString("FromDate", fromDate).apply()
        sharedPreferences.edit().putString("FromDateFR", fromDateFR).apply()
    }

    fun onToDateChanged(toDate: String, toDateFR: String){
        currentToDate = toDate
        currentToDateFR = toDateFR
        sharedPreferences.edit().putString("ToDate", toDate).apply()
        sharedPreferences.edit().putString("ToDateFR", toDateFR).apply()
    }

    fun onFromDateCanceled(){
        currentFromDate = ""
        currentFromDateFR = "--/--/----"
        sharedPreferences.edit().putString("FromDate", "").apply()
        sharedPreferences.edit().putString("FromDateFR", "--/--/----").apply()
    }

    fun onToDateCanceled(){
        currentToDate = ""
        currentToDateFR = "--/--/----"
        sharedPreferences.edit().putString("ToDate", "").apply()
        sharedPreferences.edit().putString("ToDateFR", "--/--/----").apply()
    }

    fun onFilterReset(){
        currentLanguage = "ALL"
        sharedPreferences.edit().putString("Language", "ALL").apply()
        currentSort = "relevancy"
        sharedPreferences.edit().putString("Sort", "relevancy").apply()
        currentLanguagePosition = 0
        sharedPreferences.edit().putInt("LanguagePosition", 0).apply()
        currentSortPosition = 0
        sharedPreferences.edit().putInt("SortPosition", 0).apply()
        onFromDateCanceled()
        onToDateCanceled()
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
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}