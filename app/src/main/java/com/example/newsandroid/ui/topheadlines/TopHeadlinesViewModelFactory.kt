package com.example.newsandroid.ui.topheadlines

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TopHeadlinesViewModelFactory () : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TopHeadlinesViewModel::class.java)) {
                return TopHeadlinesViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}