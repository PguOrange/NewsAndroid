package com.example.newsandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsandroid.ui.topheadlines.TopHeadlinesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TopHeadlinesFragment.newInstance())
                .commitNow()
        }

    }
}