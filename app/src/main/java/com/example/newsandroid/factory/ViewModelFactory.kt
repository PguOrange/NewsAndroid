package com.example.newsandroid.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsandroid.ui.detail.DetailNewsViewModel
import com.example.newsandroid.ui.everything.EverythingViewModel
import com.example.newsandroid.ui.topheadlines.TopHeadlinesViewModel

class ViewModelFactory (private val application: Application) : ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TopHeadlinesViewModel::class.java)) {
                return TopHeadlinesViewModel(application) as T
            }
            if (modelClass.isAssignableFrom(EverythingViewModel::class.java)) {
                return EverythingViewModel(application) as T
            }
            if (modelClass.isAssignableFrom(DetailNewsViewModel::class.java)) {
                return DetailNewsViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}